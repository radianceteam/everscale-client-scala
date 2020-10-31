package com.radiance.scala.tonclient.types.config

import io.circe._
import io.circe.derivation._

case class AbiConfig(
                      workchain: Long,
                      message_expiration_timeout: Long,
                      message_expiration_timeout_grow_factor: Long
                    )

object AbiConfig {
  implicit val abiConfigCodec: Codec[AbiConfig] = deriveCodec[AbiConfig]
}
