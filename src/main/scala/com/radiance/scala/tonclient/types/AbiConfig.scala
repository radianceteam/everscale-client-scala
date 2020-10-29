package com.radiance.scala.tonclient.types

import io.circe._
import io.circe.derivation._

case class AbiConfig(
                      workchain: Number,
                      message_expiration_timeout: Number,
                      message_expiration_timeout_grow_factor: Number
                    )

object AbiConfig {
  implicit val abiConfigCodec: Codec[AbiConfig] = deriveCodec[AbiConfig]
}
