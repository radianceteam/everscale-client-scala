package context

import java.nio.file.{Files, Path}

import _root_.types.ApiNew
import io.circe._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.{Success, Try}

object TonContextScala {

//  Try { System.load(createTempDll("/libTonClientScalaBridge.dll")) }.fold(
//    t => throw new RuntimeException(t),
//    _ => ()
//  )
//
//  private def createTempDll(fileName: String) = {
//    val inputStream = classOf[TonContextScala].getResourceAsStream(fileName)
//    if (inputStream == null) throw new IOException("Cannot find resource '" + fileName + "'")
//    val tempDll = File.createTempFile("TONLibrary", ".dll")
//    val outputStream = new FileOutputStream(tempDll)
//    val array = new Array[Byte](8192)
//    var i = inputStream.read(array)
//    while ( { i != -1 }) {
//      outputStream.write(array, 0, i)
//      i = inputStream.read(array)
//    }
//    inputStream.close()
//    outputStream.close()
//    tempDll.deleteOnExit()
//    tempDll.getAbsolutePath
//  }

  load()

  private def loadPackaged(): Unit = {
    val lib: String = System.mapLibraryName("libTonClientScalaBridge.dll")
    val tmp: Path = Files.createTempDirectory("jni-")

    val plat: String = {
      val line = try {
        scala.sys.process.Process("uname -sm").lazyLines.head
      } catch {
        case ex: Exception => sys.error("Error running `uname` command")
      }
      val parts = line.split(" ")
      if (parts.length != 2) {
        sys.error("Could not determine platform: 'uname -sm' returned unexpected string: " + line)
      } else {
        val arch = parts(1).toLowerCase.replaceAll("\\s", "")
        val kernel = parts(0).toLowerCase.replaceAll("\\s", "")
        arch + "-" + kernel
      }
    }

    val resourcePath: String = "/native/" + plat + "/" + lib
    val resourceStream = Option(getClass.getResourceAsStream(resourcePath)) match {
      case Some(s) => s
      case None => throw new UnsatisfiedLinkError(
        "Native library " + lib + " (" + resourcePath + ") cannot be found on the classpath.")
    }

    val extractedPath = tmp.resolve(lib)

    try {
      Files.copy(resourceStream, extractedPath)
    } catch {
      case ex: Exception => throw new UnsatisfiedLinkError(
        "Error while extracting native library: " + ex)
    }

    System.load(extractedPath.toAbsolutePath.toString)
  }

  def load(): Unit = try {
    System.loadLibrary("libTonClientScalaBridge.dll")
  } catch {
    case ex: UnsatisfiedLinkError => loadPackaged()
  }

  def apply(config: String)(implicit ec: ExecutionContext): TonContextScala = {
    val ctx = new TonContextScala(-1)
    val create = ctx.createContext(config)
    val cursor = parse(create).getOrElse(Json.Null).hcursor
    cursor.downField("error").success.map(e => {
      val code = e.downField("code").as[Int]
      val message = e.downField("message").as[String]
      throw new RuntimeException(s"Code: $code; Message: $message")
    }).getOrElse({
      cursor.downField("result").as[Int].map(i => ctx.setCtxId(i)).fold(
        t => throw new RuntimeException(t),
        r => r
      )
    })
  }

  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.global
    val ctx = TonContextScala("{}")
    println(ctx.requestSync("client.version", ""))
    //val crypto = new Crypto(ctx)(ExecutionContext.global)
    //println(util.Arrays.asList(crypto.factorize("EE").get))
    //ctx.requestSync("client.version", "")
    println(Await.result(ctx.requestAsync("client.version", ""), 1.second))
  }
}

class TonContextScala private(var contextId: Int)(implicit val ec: ExecutionContext) {

  @native def createContext(config: String): String
  @native def destroyContext(context: Int): Unit
  @native def asyncRequest(context: Int, functionName: String, params: String, promise: Promise[String]): Unit
  @native def syncRequest(context: Int, functionName: String, params: String): String

  def asyncHandler(code: Int, params: String, promise: Promise[String]): Unit = {
    if (code == 1) {
      val cursor = parse(params).getOrElse(Json.Null).hcursor
      val code: Int = cursor.downField("code").as[Int].getOrElse(-1)
      val message = cursor.downField("message").as[String].getOrElse("")
      promise.failure(new RuntimeException(s"Code: $code; Message: $message"))
    } else {
      promise.complete(Success(params))
    }
  }

  private[TonContextScala] def setCtxId(i: Int): TonContextScala = {
    contextId = i
    this
  }

  def destroy(): Unit = {
    if (contextId >= 0) {
      destroyContext(contextId)
      contextId = -1
    }
  }

  def requestSync(functionName: String, params: String) = syncRequest(contextId, functionName, params)

  def requestAsync(functionName: String, params: String): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    asyncRequest(contextId, functionName, params, promise)
    promise.future
  }

  def execAsyncNew[In <: ApiNew : Encoder](functionName: String, arg: In): Future[Either[Throwable, arg.Out]] =
    requestAsync(functionName, arg.asJson.noSpaces).map(r => parse(r).fold(
      t => Left(t),
      a => a.as[arg.Out](arg.decoder)
    ))

  def execAsyncVoidNew[Out : Decoder](functionName: String): Future[Either[Throwable, Out]] =
    requestAsync(functionName, "{}").map(r => parse(r).fold(
      t => Left(t),
      a => a.as[Out]
    ))

  def execSync[In <: ApiNew : Encoder](functionName: String, arg: In): Either[Throwable, arg.Out] =
    Try { requestSync(functionName, arg.asJson.noSpaces) }.flatMap(r => parse(r).toTry)
      .map(a => a.as[arg.Out](arg.decoder))
      .fold(t => Left(t), r => r.fold(t => Left(t), r => Right(r)))
}

