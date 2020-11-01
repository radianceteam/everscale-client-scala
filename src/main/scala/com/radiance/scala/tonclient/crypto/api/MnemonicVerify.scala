package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[crypto] case class MnemonicVerify(
                                               phrase: String,
                                               dictionary: Long,
                                               word_count: Long
                                             ) extends Api {
  override val functionName: String = "crypto.mnemonic_verify"
  override val fieldName: Option[String] = Some("valid")
  override type Out = Boolean
  override val decoder: Decoder[Boolean] = implicitly[Decoder[Boolean]]
}

private[crypto] object MnemonicVerify {
  implicit val MnemonicVerifyArgsEncoder: Encoder[MnemonicVerify] = deriveCodec[MnemonicVerify]
}
