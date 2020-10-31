package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe.{Decoder, Encoder}
import io.circe.derivation.deriveEncoder

private[crypto] case class HdkeyXprvFromMnemonic(
                                                      phrase: String,
                                                      dictionary: Long,
                                                      word_count: Long
                                                    ) extends Args {
  override val functionName: String = "crypto.hdkey_xprv_from_mnemonic"
  override val fieldName: Option[String] = Some("xprv")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object HdkeyXprvFromMnemonic {
  implicit val hdkeyXprvFromMnemonicArgsEncoder: Encoder[HdkeyXprvFromMnemonic] = deriveEncoder[HdkeyXprvFromMnemonic]
}
