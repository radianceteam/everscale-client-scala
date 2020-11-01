package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[crypto] case class MnemonicFromRandom(dictionary: Long, word_count: Long) extends Api {
  override val functionName: String = "crypto.mnemonic_from_random"
  override val fieldName: Option[String] = Some("phrase")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object MnemonicFromRandom {
  implicit val mnemonicFromRandomEncoder: Encoder[MnemonicFromRandom] = deriveEncoder[MnemonicFromRandom]
}