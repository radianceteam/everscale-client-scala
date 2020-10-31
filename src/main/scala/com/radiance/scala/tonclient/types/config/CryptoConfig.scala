package com.radiance.scala.tonclient.types.config

import io.circe._
import io.circe.derivation._

case class CryptoConfig(
                         mnemonic_dictionary: Long,
                         mnemonic_word_count: Long,
                         hdkey_derivation_path: String,
                         hdkey_compliant: Boolean
                       )

object CryptoConfig {
  implicit val cryptoConfigCodec: Codec[CryptoConfig] = deriveCodec[CryptoConfig]
}
