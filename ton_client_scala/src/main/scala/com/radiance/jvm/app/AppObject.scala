package com.radiance.jvm.app

import io.circe._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.Try

class AppObject[T: Decoder, V: Encoder](f: T => V)(implicit
  val ec: ExecutionContext
) {

  val functionName: String = "client.resolve_app_request"

  def resolveRequest(params: String): String = {
    val ParamsOfAppRequest(appRequestId, innerJson) = (for {
      json <- parse(params)
      paramsOfRequest <- json.as[ParamsOfAppRequest]
    } yield paramsOfRequest).fold(
      t => throw new RuntimeException(t),
      identity
    )
    ((for {
      arg <- innerJson.as[T]
      res <- Try(f(arg).asJson).toEither
    } yield res) match {
      case Right(body) =>
        ParamsOfResolveAppRequest(appRequestId, AppRequestResult.Ok(body))
      case Left(t)     =>
        ParamsOfResolveAppRequest(appRequestId, AppRequestResult.Error(t.getMessage))
    }).asJson.noSpaces
  }
}
