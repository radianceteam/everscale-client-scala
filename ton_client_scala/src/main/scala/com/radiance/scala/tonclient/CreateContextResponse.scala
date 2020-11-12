package com.radiance.scala.tonclient

import com.radiance.scala.types.ClientTypes.ClientError
import io.circe.Decoder
import io.circe.derivation.deriveDecoder

case class CreateContextResponse(error: Option[ClientError], result: Int)

object CreateContextResponse {
  implicit val CreateContextResponseDecoder: Decoder[CreateContextResponse] = deriveDecoder[CreateContextResponse]
}