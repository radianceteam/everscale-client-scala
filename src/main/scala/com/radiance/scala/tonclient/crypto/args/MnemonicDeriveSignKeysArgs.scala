package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class MnemonicDeriveSignKeysArgs(phrase: String, path: String, dictionary: Long, word_count: Long) extends Args {
  override val functionName: String = "crypto.mnemonic_derive_sign_keys"
}

private[crypto] object MnemonicDeriveSignKeysArgs {
  implicit val mnemonicDeriveSignKeysArgsEncoder: Encoder[MnemonicDeriveSignKeysArgs] = deriveEncoder[MnemonicDeriveSignKeysArgs]
}
