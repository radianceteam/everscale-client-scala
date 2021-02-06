package com.radiance.jvm

import cats.implicits._
import com.radiance.jvm.app.AppObject
import com.radiance.jvm.client.ClientConfig
import io.circe._
import io.circe.parser._
import io.circe.syntax._

import java.nio.file.{Files, Path, Paths}
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Success, Try}

object Context {

  sealed trait OperationSystem
  case object Windows extends OperationSystem
  case object Linux extends OperationSystem
  case object MacOs extends OperationSystem
  case object UndefinedOs extends OperationSystem

  def define: OperationSystem =
    System.getProperty("os.name").toLowerCase match {
      case x if x.contains("win")                                           => Windows
      case x if x.contains("nix") || x.contains("nux") || x.contains("aix") => Linux
      case x if x.contains("mac")                                           => MacOs
    }

  private def trimExtension(name: String): String = name.split("\\.").head

  private def init(): Unit = {
    val libName: String = (define match {
      case Windows     =>
        List(
          "libton_client_scala_bridge.dll", //mingw64
          "cygton_client_scala_bridge.dll", //cygwin
          "ton_client_scala_bridge.dll" // win
        )
      case Linux       =>
        List(
          "libton_client_scala_bridge.so" // linux
        )
      case MacOs       => List()
      case UndefinedOs =>
        throw new IllegalStateException("Can't define current operation system")
    })
      .find(n => getClass.getResource(s"/$n") != null)
      .getOrElse(
        throw new IllegalStateException("Can't find compiled bridge library")
      )

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
    val byteArr =
      Files.readAllBytes(Paths.get(getClass.getResource(s"/$fileName").toURI))
    Files.write(tempDir.resolve(fileName), byteArr)
  }

  def apply(config: ClientConfig)(implicit
    ec: ExecutionContext
  ): Context = {
    val ctx = new Context(-1)
    val create = ctx.createContext(config.asJson.deepDropNullValues.noSpaces)
    parse(create).toTry.fold(
      t => throw t,
      (u: Json) =>
        u.as[CreateContextResponse]
          .fold(
            t => throw t,
            r =>
              if (r.error.isEmpty) ctx.setCtxId(r.result)
              else
                throw new RuntimeException(
                  r.error.asJson.deepDropNullValues.spaces2
                )
          )
    )
  }
  init()
}

class Context private (var contextId: Int)(implicit
  val ec: ExecutionContext
) {

  private val maxId = new AtomicInteger(0)

  private val callbackMap: ConcurrentHashMap[Promise[String], Request] =
    new ConcurrentHashMap[Promise[String], Request]()

  private val appPromiseMap: ConcurrentHashMap[Int, Promise[String]] =
    new ConcurrentHashMap[Int, Promise[String]]()

  private val appCallbackMap: ConcurrentHashMap[Int, AppObject[Any, Any]] =
    new ConcurrentHashMap[Int, AppObject[Any, Any]]()

  @native private[jvm] def createContext(config: String): String

  @native private[jvm] def destroyContext(context: Int): Unit

  @native private[jvm] def asyncRequest(
    context: Int,
    functionName: String,
    params: String,
    promise: Promise[String]
  ): Unit

  @native private[jvm] def asyncRequestWithAppId(
    context: Int,
    functionName: String,
    params: String,
    appId: Int
  ): Unit

  @native private[jvm] def unregisterAppId(
    context: Int,
    functionName: String,
    params: String,
    appId: Int
  ): Unit

  @native private[jvm] def syncRequest(
    context: Int,
    functionName: String,
    params: String
  ): String

  private[jvm] def asyncHandler(
    code: Int,
    params: String,
    promise: Promise[String],
    finished: Boolean
  ): Unit = {
    OperationCode.fromInt(code) match {
      case SuccessCode =>
        promise.tryComplete(Success(Option(params).getOrElse("")))

      case ErrorCode =>
        callbackMap.remove(promise)
        val cursor = parse(params).getOrElse(Json.Null).hcursor
        val exception = (for {
          code <- cursor.get[Int]("code")
          message <- cursor.get[String]("message")
        } yield new IllegalStateException(s"Code: $code; Message: $message"))
          .getOrElse(throw new RuntimeException(s"Unexpected message format: $params"))
        promise.tryFailure(exception)

      case NopCode => // nop

      case CustomCode    =>
        Option(callbackMap.get(promise))
          .foreach(
            callback =>
              callback(
                parse(params).getOrElse(
                  Json.fromString(s"Can't parse params in callback:\n$params")
                )
              )
          )
      case AppNotifyCode =>
        throw new IllegalStateException("Unexpected response AppNotifyCode")

      case AppRequestCode =>
        throw new IllegalStateException("Unexpected response AppRequestCode")
    }
    if (finished) {
      callbackMap.remove(promise)
    }
  }

  private[jvm] def asyncHandlerWithAppId(
    code: Int,
    params: String,
    appId: Int,
    finished: Boolean
  ): Unit = {
    OperationCode.fromInt(code) match {
      case SuccessCode =>
        Option(appPromiseMap.remove(appId)).foreach(p => p.success(params))

      case ErrorCode =>
        Option(appPromiseMap.remove(appId)).foreach(p => p.failure(new IllegalStateException(params)))

      case NopCode => // nop

      case CustomCode => // nop

      case AppNotifyCode => // TODO nop

      case AppRequestCode =>
        val app = appCallbackMap.get(appId)
        val str = app.resolveRequest(params)
        asyncRequestWithAppId(contextId, app.functionName, str, appId)

    }
  }

  private[Context] def setCtxId(i: Int): Context = {
    contextId = i
    this
  }

  def destroy(): Unit = {
    if (contextId >= 0) {
      destroyContext(contextId)
      contextId = -1
    }
  }

  private def callNativeAsync(
    functionName: String,
    params: String
  ): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    asyncRequest(contextId, functionName, params, promise)
    promise.future
  }

  private def callNativeAsyncWithCallback(
    functionName: String,
    params: String,
    callback: Request
  ): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    callbackMap.put(promise, callback)
    asyncRequest(contextId, functionName, params, promise)
    promise.future
  }

  private[jvm] def execAsync[In <: Bind: Encoder](
    functionName: String,
    arg: In
  ): Future[Either[Throwable, arg.Out]] =
    callNativeAsync(functionName, arg.asJson.deepDropNullValues.noSpaces)
      .map(r => parse(r).flatMap(_.as[arg.Out](arg.decoder)))

  private[jvm] def execAsyncNew[In: Encoder, Out: Decoder](
    functionName: String,
    arg: In
  ): Future[Either[Throwable, Out]] =
    callNativeAsync(functionName, arg.asJson.deepDropNullValues.noSpaces)
      .map(r => parse(r).flatMap(_.as[Out]))

  private[jvm] def registerAppObject[Out: Decoder, T, V](
    functionName: String,
    params: String,
    app_object: AppObject[T, V]
  ): Future[Either[Throwable, Out]] = {
    val promise: Promise[String] = Promise[String]()
    val appId = maxId.incrementAndGet()
    appPromiseMap.put(appId, promise)
    appCallbackMap.put(appId, app_object.asInstanceOf[AppObject[Any, Any]])
    asyncRequestWithAppId(contextId, functionName, params, appId)
    promise.future
      .map(r => parse(r).flatMap(_.as[Out]))
  }

  private[jvm] def executeWithAppObject[T: Encoder](
    functionName: String,
    arg: T,
    appId: Int
  ): Future[Either[Throwable, Unit]] = {
    val promise: Promise[String] = Promise[String]()
    appPromiseMap.put(appId, promise)
    asyncRequestWithAppId(contextId, functionName, arg.asJson.noSpaces, appId)
    promise.future
      .map(_ => ().asRight)
  }

  private[jvm] def unregisterAppObject(
    handleValue: Int,
    functionName: String
  ): Future[Either[Throwable, Unit]] = {
    val promise: Promise[String] = Promise[String]()
    val appId = handleValue
    appPromiseMap.put(appId, promise)
    appCallbackMap.remove(appId)
    unregisterAppId(contextId, functionName, "", appId)
    promise.future.map(_ => Right(()))
  }

  private[jvm] def execAsyncVoid[In: Encoder](
    functionName: String,
    arg: In
  ): Future[Either[Throwable, Unit]] =
    callNativeAsync(functionName, arg.asJson.deepDropNullValues.noSpaces)
      .map(_ => Right(()))
      .recover(Left(_))

  private[jvm] def execAsyncParameterless[Out: Decoder](
    functionName: String
  ): Future[Either[Throwable, Out]] =
    callNativeAsync(functionName, "").map(r => parse(r).flatMap(_.as[Out]))

  private[jvm] def execAsyncWithCallback[In <: Bind: Encoder](
    functionName: String,
    arg: In,
    callback: Request
  ): Future[Either[Throwable, arg.Out]] =
    callNativeAsyncWithCallback(
      functionName,
      arg.asJson.deepDropNullValues.noSpaces,
      callback
    ).map(r => parse(r).flatMap(_.as[arg.Out](arg.decoder)))

  private[jvm] def execSync[In <: Bind: Encoder](
    functionName: String,
    arg: In
  ): Either[Throwable, arg.Out] = {
    implicit val d: Decoder[arg.Out] = arg.decoder
    Try {
      syncRequest(
        contextId,
        functionName,
        arg.asJson.deepDropNullValues.noSpaces
      )
    }.toEither
      .flatMap(parse)
      .flatMap(_.as[OperationResponse[arg.Out]])
      .flatMap {
        case OperationResponse(Right(r)) => Right(r)
        case OperationResponse(Left(t))  => Left(new Exception(t.asJson.spaces2))
      }
  }

  private[jvm] def execSyncParameterless[Out: Decoder](
    functionName: String
  ): Either[Throwable, Out] =
    Try { syncRequest(contextId, functionName, "") }.toEither
      .flatMap(parse)
      .flatMap(_.as[OperationResponse[Out]])
      .flatMap {
        case OperationResponse(Right(r)) => Right(r)
        case OperationResponse(Left(t))  => Left(new Exception(t.asJson.spaces2))
      }
}
