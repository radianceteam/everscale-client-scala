package com.radiance.scala.tonclient

import java.io.{File, FileOutputStream}
import java.nio.file.{Files, Path, Paths}
import java.util.concurrent.ConcurrentHashMap

import com.radiance.scala.tonclient.TonContextScala.Request
import com.radiance.scala.types.ApiNew
import com.radiance.scala.types.ClientTypes.ClientConfig
import io.circe._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.io.Source
import scala.util.{Success, Try}

object TonContextScala {

  type Request = Json => ()

  private val javaLibPath = "java.library.path"
  private val bridgeWinName = "libTonClientScalaBridge"

  private val tempDir = Files.createTempDirectory("ton_bridge")
  private val tempDirPath = tempDir.toAbsolutePath

  copyFileFromClasspath(tempDirPath, s"$bridgeWinName.dll")
  addDirToJavaLibPath(tempDirPath.toString)
  System.loadLibrary(bridgeWinName)

  private def addDirToJavaLibPath(s: String): Unit = {
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
}

class TonContextScala private(var contextId: Int)(implicit val ec: ExecutionContext) {

  private val callbacksMap: ConcurrentHashMap[Promise[String], Request] =
    new ConcurrentHashMap[Promise[String], Request]()

  @native private[tonclient] def createContext(config: String): String
  @native private[tonclient] def destroyContext(context: Int): Unit
  @native private[tonclient] def asyncRequest(context: Int, functionName: String, params: String, promise: Promise[String]): Unit
  @native private[tonclient] def syncRequest(context: Int, functionName: String, params: String): String

  private[tonclient] def asyncHandler(code: Int, params: String, promise: Promise[String]): Unit =
    OperationCode.fromInt(code) match {
      case SuccessCode =>
        callbacksMap.remove(promise)
        promise.tryComplete(Success(Option(params).getOrElse("")))
      case ErrorCode =>
        callbacksMap.remove(promise)
        val cursor = parse(params).getOrElse(Json.Null).hcursor
        val code: Int = cursor.downField("code").as[Int].getOrElse(-1)
        val message = cursor.downField("message").as[String].getOrElse("")
        println(s"Code: $code; Message: $message")
        promise.tryFailure(new IllegalStateException(s"Code: $code; Message: $message"))
      case NopCode =>
        // TODO callbacksMap.remove(promise)
        println(s"NopCode: $params")
      case CustomCode =>
        Option(callbacksMap.get(promise))
          .foreach(callback => callback(parse(params).getOrElse(Json.fromString(s"Can't parse params in callback:\n$params"))))
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

  private[scala] def execAsync[In <: ApiNew : Encoder](functionName: String, arg: In): Future[Either[Throwable, arg.Out]] =
    callNativeAsync(functionName, arg.asJson.deepDropNullValues.noSpaces).map(r => parse(r).fold(
      t => Left(t),
      a => a.as[arg.Out](arg.decoder)
    ))

  private[scala] def execAsyncWithCallback[In <: ApiNew : Encoder](functionName: String, arg: In, callback: Request): Future[Either[Throwable, arg.Out]] =
    callNativeAsyncWithCallback(functionName, arg.asJson.deepDropNullValues.noSpaces, callback)
      .map(r => parse(r).fold(t => Left(t), a => a.as[arg.Out](arg.decoder)))

  private[scala] def execAsyncVoid[Out : Decoder](functionName: String): Future[Either[Throwable, Out]] =
    callNativeAsync(functionName, "").map(r => parse(r).fold(t => Left(t), a => a.as[Out]))

  private[scala] def execSync[In <: ApiNew : Encoder](functionName: String, arg: In): Either[Throwable, arg.Out] =
    Try { syncRequest(contextId, functionName, arg.asJson.deepDropNullValues.noSpaces) }
      .flatMap(r => parse(r).toTry)
      .map(a => a.as[arg.Out](arg.decoder))
      .fold(t => Left(t), r => r.fold(t => Left(t), r => Right(r)))

  // TODO implement execSyncVoid

  // TODO remove it later
  private[scala] def execAsyncFromSync[In <: ApiNew : Encoder](functionName: String, arg: In): Future[Either[Throwable, arg.Out]] =
    Future {
      implicit val d: Decoder[arg.Out] = arg.decoder
      val res = syncRequest(contextId, functionName, arg.asJson.deepDropNullValues.noSpaces)
      parse(res).toTry.toEither.map(_.as[OperationResponse[arg.Out]]).fold(
        t => Left(t),
        r => r.fold(
          t => Left(t),
          {
            case OperationResponse(Some(r), None) => Right(r)
            case OperationResponse(None, Some(error)) => Left(new Exception(error.asJson.deepDropNullValues.spaces2))
          }
        )
      )
    }

  // TODO remove it later
  private[scala] def execAsyncVoidFromSync[Out : Decoder](functionName: String): Future[Either[Throwable, Out]] = Future {
    val res = syncRequest(contextId, functionName, "{}")
    parse(res).toTry.toEither.map(_.as[OperationResponse[Out]]).fold(
      t => Left(t),
      r => r.fold(
        t => Left(t),
        {
          case OperationResponse(Some(r), None) => Right(r)
          case OperationResponse(None, Some(error)) => Left(new Exception(error.asJson.deepDropNullValues.spaces2))
        }
      )
    )
  }
}

