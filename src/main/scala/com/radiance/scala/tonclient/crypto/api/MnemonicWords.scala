package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class MnemonicWords(dictionary: Long) extends Args {
  override val functionName: String = "crypto.mnemonic_words"
  override val fieldName: Option[String] = Some("words")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private [crypto] object MnemonicWords {
  implicit val mnemonicWordsArgsEncoder: Encoder[MnemonicWords] = deriveCodec[MnemonicWords]
}
