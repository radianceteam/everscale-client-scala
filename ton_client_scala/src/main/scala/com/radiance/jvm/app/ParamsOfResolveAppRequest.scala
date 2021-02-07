package com.radiance.jvm.app

import io.circe.derivation.deriveEncoder
import io.circe.Encoder

case class ParamsOfResolveAppRequest(
  app_request_id: Long,
  result: AppRequestResultADT.AppRequestResult
)

object ParamsOfResolveAppRequest {
  implicit val encoder: Encoder[ParamsOfResolveAppRequest] =
    deriveEncoder[ParamsOfResolveAppRequest]
}
