package com.radiance.jvm.app

import com.radiance.jvm.client._
import io.circe._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.Try

abstract class AppObject[T: Decoder, V: Encoder](implicit
  val ec: ExecutionContext
) {

  protected val f: T => V

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
        ParamsOfResolveAppRequest(appRequestId, AppRequestResultADT.Ok(body))
      case Left(t)     =>
        ParamsOfResolveAppRequest(appRequestId, AppRequestResultADT.Error(t.getMessage))
    }).asJson.noSpaces
  }
}
