package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe.Encoder
import io.circe.derivation.deriveEncoder

private[crypto] case class HdkeyXprvFromMnemonicArgs(
                                                      phrase: String,
                                                      dictionary: Long,
                                                      word_count: Long
                                                    ) extends Args {
  override val functionName: String = "crypto.hdkey_xprv_from_mnemonic"
  override val fieldName: Option[String] = Some("xprv")
}

private[crypto] object HdkeyXprvFromMnemonicArgs {
  implicit val hdkeyXprvFromMnemonicArgsEncoder: Encoder[HdkeyXprvFromMnemonicArgs] = deriveEncoder[HdkeyXprvFromMnemonicArgs]
}
