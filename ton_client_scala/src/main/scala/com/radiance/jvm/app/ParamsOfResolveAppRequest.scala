package com.radiance.jvm.app

import com.radiance.jvm.Bind
import io.circe.derivation.deriveEncoder
import io.circe.{Decoder, Encoder}

case class ParamsOfResolveAppRequest(
  app_request_id: Long,
  result: AppRequestResult
) extends Bind {
  override type Out = Unit
  override val decoder: Decoder[Unit] = implicitly[Decoder[Unit]]
}

object ParamsOfResolveAppRequest {
  implicit val encoder: Encoder[ParamsOfResolveAppRequest] =
    deriveEncoder[ParamsOfResolveAppRequest]
}
