package com.radiance.scala.tonclient.types

import io.circe._
import io.circe.derivation._


case class ClientConfig(
                         network: NetworkConfig,
                         crypto: CryptoConfig,
                         abi: AbiConfig
                       )

object ClientConfig {
  implicit val clientConfigCodec: Codec[ClientConfig] = deriveCodec[ClientConfig]
}
