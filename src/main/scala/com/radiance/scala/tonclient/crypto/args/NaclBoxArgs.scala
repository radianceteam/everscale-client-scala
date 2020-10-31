package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe.Encoder
import io.circe.derivation.deriveCodec

private[crypto] case class NaclBoxArgs(
                                        decrypted: String,
                                        nonce: String,
                                        their_public: String,
                                        secret: String
                                      ) extends Args {
  override val functionName: String = "crypto.nacl_box"
  override val fieldName: Option[String] = Some("encrypted")
}

private[crypto] object NaclBoxArgs {
  implicit val NaclBoxArgsEncoder: Encoder[NaclBoxArgs] = deriveCodec[NaclBoxArgs]
}
