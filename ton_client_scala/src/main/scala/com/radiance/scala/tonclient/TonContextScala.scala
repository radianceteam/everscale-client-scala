package com.radiance.scala.tonclient

import java.nio.file.{Files, Path, Paths}
import java.util.concurrent.ConcurrentHashMap

import com.radiance.scala.types.Bind
import com.radiance.scala.types.ClientTypes.ClientConfig
import io.circe._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Success, Try}

object TonContextScala {

  type Request = Json => ()

  sealed trait OperationSystem
  case object Windows extends OperationSystem
  case object Linux extends OperationSystem
  case object MacOs extends OperationSystem
  case object UndefinedOs extends OperationSystem

  def operationSystem: OperationSystem = System.getProperty("os.name").toLowerCase match {
    case x if x.contains("win") => Windows
    case x if x.contains("nix") || x.contains("nux") || x.contains("aix") => Linux
    case x if x.contains("mac") => MacOs
  }

  private def trimExtension(name: String): String = name.split("\\.").head

  private def init(): Unit = {
    val libName: String = (operationSystem match {
      case Windows => List(
        "libton_client_scala_bridge.dll",   //mingw64
        "cygton_client_scala_bridge.dll",   //cygwin
        "ton_client_scala_bridge.dll",      // win

      )
      case Linux => List(
        "libton_client_scala_bridge.so"     // linux
      )
      case MacOs => List()
      case UndefinedOs => throw new IllegalStateException("Can't define current operation system")
    }).find(n => getClass.getResource(s"/$n") != null)
      .getOrElse(throw new IllegalStateException("Can't find compiled bridge library"))

    try {
      System.loadLibrary(trimExtension(libName))
    } catch {
      case _: Throwable => loadFromPath(libName)
    }
  }

  private def loadFromPath(name: String): Unit = {
    val tempDir = Files.createTempDirectory("ton_bridge")
    val tempDirPath = tempDir.toAbsolutePath
    copyFileFromClasspath(tempDirPath, name)
    System.load(Paths.get(tempDirPath.toString).resolve(name).toString)
  }

  private def copyFileFromClasspath(tempDir: Path, fileName: String): Unit = {
    val byteArr = Files.readAllBytes(Paths.get(getClass.getResource(s"/$fileName").toURI))
    Files.write(tempDir.resolve(fileName), byteArr)
  }

  def apply(config: ClientConfig)(implicit ec: ExecutionContext): TonContextScala = {
    val ctx = new TonContextScala(-1)
    val create = ctx.createContext(config.asJson.deepDropNullValues.noSpaces)
    parse(create).toTry.fold(
      t => throw t,
      (u: Json) => u.as[CreateContextResponse].fold(
        t => throw t,
        r => if (r.error.isEmpty) ctx.setCtxId(r.result)
        else throw new RuntimeException(r.error.asJson.deepDropNullValues.spaces2)
      )
    )
  }
  init()
}

class TonContextScala private(var contextId: Int)(implicit val ec: ExecutionContext) {
  import TonContextScala._
  private val callbacksMap: ConcurrentHashMap[Promise[String], Request] =
    new ConcurrentHashMap[Promise[String], Request]()

  @native private[tonclient] def createContext(config: String): String

  @native private[tonclient] def destroyContext(context: Int): Unit

  @native private[tonclient] def asyncRequest(context: Int, functionName: String, params: String, promise: Promise[String]): Unit

  @native private[tonclient] def syncRequest(context: Int, functionName: String, params: String): String

  private[tonclient] def asyncHandler(code: Int, params: String, promise: Promise[String], finished: Boolean): Unit = {
    OperationCode.fromInt(code) match {
      case SuccessCode =>
        println(SuccessCode)
        promise.tryComplete(Success(Option(params).getOrElse("")))
      case ErrorCode =>
        callbacksMap.remove(promise)
        val cursor = parse(params).getOrElse(Json.Null).hcursor
        val code: Int = cursor.downField("code").as[Int].getOrElse(-1)
        val message = cursor.downField("message").as[String].getOrElse("")
        promise.tryFailure(new IllegalStateException(s"Code: $code; Message: $message"))
      case NopCode =>

      case CustomCode =>
        Option(callbacksMap.get(promise))
          .foreach(callback => callback(parse(params).getOrElse(Json.fromString(s"Can't parse params in callback:\n$params"))))
    }
    if (finished) {
      callbacksMap.remove(promise)
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

  private def callNativeAsync(functionName: String, params: String): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    asyncRequest(contextId, functionName, params, promise)
    promise.future
  }

  private def callNativeAsyncWithCallback(functionName: String, params: String, callback: Request): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    callbacksMap.put(promise, callback)
    asyncRequest(contextId, functionName, params, promise)
    promise.future
  }

  private[scala] def execAsync[In <: Bind : Encoder](functionName: String, arg: In): Future[Either[Throwable, arg.Out]] =
    callNativeAsync(functionName, arg.asJson.deepDropNullValues.noSpaces).map(r => parse(r).fold(
      t => Left(t),
      a => a.as[arg.Out](arg.decoder)
    ))

  private[scala] def execAsyncWithCallback[In <: Bind : Encoder](functionName: String, arg: In, callback: Request): Future[Either[Throwable, arg.Out]] =
    callNativeAsyncWithCallback(functionName, arg.asJson.deepDropNullValues.noSpaces, callback)
      .map(r => parse(r).fold(t => Left(t), a => a.as[arg.Out](arg.decoder)))

  private[scala] def execAsyncVoid[Out : Decoder](functionName: String): Future[Either[Throwable, Out]] =
    callNativeAsync(functionName, "").map(r => parse(r).fold(t => Left(t), a => a.as[Out]))

  private[scala] def execSync[In <: Bind : Encoder](functionName: String, arg: In): Either[Throwable, arg.Out] = {
    implicit val d: Decoder[arg.Out] = arg.decoder
    Try { syncRequest(contextId, functionName, arg.asJson.deepDropNullValues.noSpaces) }.toEither
      .flatMap(r => parse(r))
      .flatMap(a => a.as[OperationResponse[arg.Out]])
      .flatMap {
        case OperationResponse(Right(r)) => Right(r)
        case OperationResponse(Left(t)) => Left(new Exception(t.asJson.spaces2))
      }
    }

  private[scala] def execSyncVoid[Out: Decoder](functionName: String): Either[Throwable, Out] =
    Try { syncRequest(contextId, functionName, "") }.toEither
      .flatMap(r => parse(r))
      .flatMap(a => a.as[OperationResponse[Out]])
      .flatMap {
        case OperationResponse(Right(r)) => Right(r)
        case OperationResponse(Left(t)) => Left(new Exception(t.asJson.spaces2))
      }
}

