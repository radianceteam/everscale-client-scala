package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class MnemonicFromEntropyArgs(
                                                    entropy: String,
                                                    dictionary: Long,
                                                    word_count: Long
                                                  ) extends Args {
  override val functionName: String = "crypto.mnemonic_from_entropy"
  override val fieldName: Option[String] = Some("phrase")
}

private[crypto] object MnemonicFromEntropyArgs {
  implicit val MnemonicFromEntropyArgsEncoder: Encoder[MnemonicFromEntropyArgs] = deriveEncoder[MnemonicFromEntropyArgs]
}

