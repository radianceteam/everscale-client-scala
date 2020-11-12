package com.radiance.scala.tonclient

import java.io.{File, FileOutputStream}
import java.nio.file.{Files, Path}

import com.radiance.scala.types.ApiNew
import com.radiance.scala.types.ClientTypes.ClientConfig
import io.circe._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.{Success, Try}

object TonContextScala {

  val tempDir = Files.createTempDirectory("ton_bridge").toAbsolutePath
  copyFileFromClasspath(tempDir, "TonClientScalaBridge.dll")
  addDirToJavaLibPath(tempDir.toString)
  System.loadLibrary("TonClientScalaBridge")

  private def addDirToJavaLibPath(s: String): Unit = {
    val javaLibPath = "java.library.path"
    val field = classOf[ClassLoader].getDeclaredField("usr_paths")
    field.setAccessible(true)
    val paths = field.get(null).asInstanceOf[Array[String]]
    if (!paths.toSet.contains(s)) {
      val updatedArray = (paths.toList ::: s :: Nil).toArray
      field.set(null, updatedArray)
      System.setProperty(javaLibPath, s"${System.getProperty(javaLibPath)}${File.pathSeparator}$s")
    }
  }

  private def copyFileFromClasspath(tempDir: Path, fileName: String): Unit = {
    val is = classOf[TonContextScala].getResourceAsStream(s"/$fileName")
    val path = tempDir.resolve(fileName)
    val tempDll = Files.createFile(path).toFile
    val os = new FileOutputStream(tempDll)
    val array = new Array[Byte](8192)
    var i = is.read(array)
    while ({ i != -1 }) {
      os.write(array, 0, i)
      i = is.read(array)
    }
    os.close()
    is.close()
    tempDir.toFile.deleteOnExit()
  }

  def apply(config: ClientConfig)(implicit ec: ExecutionContext): TonContextScala = {
    val ctx = new TonContextScala(-1)
    val create = ctx.createContext(config.asJson.noSpaces)
    parse(create).toTry.fold(
      t => throw t,
      (u: Json) => u.as[CreateContextResponse].fold(
        t => throw t,
        r => if (r.error.isEmpty) ctx.setCtxId(r.result)
        else throw new RuntimeException(r.error.asJson.spaces2)
      )
    )
  }

  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.global
    val ctx = TonContextScala(ClientConfig(None, None, None))
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

  def execAsync[In <: ApiNew : Encoder](functionName: String, arg: In): Future[Either[Throwable, arg.Out]] =
    requestAsync(functionName, arg.asJson.noSpaces).map(r => parse(r).fold(
      t => Left(t),
      a => a.as[arg.Out](arg.decoder)
    ))

  def execAsyncVoid[Out : Decoder](functionName: String): Future[Either[Throwable, Out]] =
    requestAsync(functionName, "{}").map(r => parse(r).fold(
      t => Left(t),
      a => a.as[Out]
    ))

  def execSync[In <: ApiNew : Encoder](functionName: String, arg: In): Either[Throwable, arg.Out] =
    Try { requestSync(functionName, arg.asJson.noSpaces) }.flatMap(r => parse(r).toTry)
      .map(a => a.as[arg.Out](arg.decoder))
      .fold(t => Left(t), r => r.fold(t => Left(t), r => Right(r)))
}

