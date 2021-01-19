package com.radiance.jvm.app

import com.radiance.jvm.Value
import io.circe._
import io.circe.derivation._

case class ParamsOfAppRequest(app_request_id: Long, request_data: Value)

object ParamsOfAppRequest {
  implicit val decoder: Decoder[ParamsOfAppRequest] = deriveDecoder[ParamsOfAppRequest]
}
