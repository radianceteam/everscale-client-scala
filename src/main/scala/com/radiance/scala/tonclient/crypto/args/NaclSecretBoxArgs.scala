package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSecretBoxArgs(
                                              decrypted: String,
                                              nonce: String,
                                              key: String
                                            ) extends Args {
  override val functionName: String = "crypto.nacl_secret_box"
  override val fieldName: Option[String] = Some("encrypted")
}

private[crypto] object NaclSecretBoxArgs {
  implicit val naclSecretBoxArgsEncoder: Encoder[NaclSecretBoxArgs] = deriveCodec[NaclSecretBoxArgs]
}
