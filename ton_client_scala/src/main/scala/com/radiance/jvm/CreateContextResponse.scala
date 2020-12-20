package com.radiance.jvm

import com.radiance.jvm.client.ClientError
import io.circe.Decoder
import io.circe.derivation.deriveDecoder

case class CreateContextResponse(error: Option[ClientError], result: Int)

object CreateContextResponse {
  implicit val decoder: Decoder[CreateContextResponse] = deriveDecoder[CreateContextResponse]
}