package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class MnemonicWordsArgs(dictionary: Long) extends Args {
  override val functionName: String = "crypto.mnemonic_words"
  override val fieldName: Option[String] = Some("words")
}

private [crypto] object MnemonicWordsArgs {
  implicit val mnemonicWordsArgsEncoder: Encoder[MnemonicWordsArgs] = deriveCodec[MnemonicWordsArgs]
}
