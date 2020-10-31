package com.radiance.scala.tonclient.types.unused

import io.circe._
import io.circe.derivation._

case class ClientError(
                        code: Long,
                        message: String,
                        data: String
                      )

object ClientError {
  implicit val clientErrorCodec: Codec[ClientError] = deriveCodec[ClientError]
}
