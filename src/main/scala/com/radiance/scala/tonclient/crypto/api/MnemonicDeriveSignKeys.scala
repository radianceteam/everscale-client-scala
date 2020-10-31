package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.both.KeyPair
import io.circe.{Decoder, _}
import io.circe.derivation._

private[crypto] case class MnemonicDeriveSignKeys(phrase: String, path: String, dictionary: Long, word_count: Long) extends Args {
  override val functionName: String = "crypto.mnemonic_derive_sign_keys"
  override type Out = KeyPair
  override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
}

private[crypto] object MnemonicDeriveSignKeys {
  implicit val mnemonicDeriveSignKeysArgsEncoder: Encoder[MnemonicDeriveSignKeys] = deriveEncoder[MnemonicDeriveSignKeys]
}
