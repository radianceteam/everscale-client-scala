package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class MnemonicVerifyArgs(
                                               phrase: String,
                                               dictionary: Long,
                                               word_count: Long
                                             ) extends Args {
  override val functionName: String = "crypto.mnemonic_verify"
  override val fieldName: Option[String] = Some("valid")
}

private[crypto] object MnemonicVerifyArgs {
  implicit val MnemonicVerifyArgsEncoder: Encoder[MnemonicVerifyArgs] = deriveCodec[MnemonicVerifyArgs]
}
