package com.radiance.scala.tonclient.types

import io.circe._
import io.circe.derivation._

case class CryptoConfig(
                         mnemonic_dictionary: Number,
                         mnemonic_word_count: Number,
                         hdkey_derivation_path: String,
                         hdkey_compliant: Boolean
                       )

object CryptoConfig {
  implicit val cryptoConfigCodec: Codec[CryptoConfig] = deriveCodec[CryptoConfig]
}
