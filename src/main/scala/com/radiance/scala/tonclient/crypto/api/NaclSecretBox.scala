package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSecretBox(
                                              decrypted: String,
                                              nonce: String,
                                              key: String
                                            ) extends Args {
  override val functionName: String = "crypto.nacl_secret_box"
  override val fieldName: Option[String] = Some("encrypted")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object NaclSecretBox {
  implicit val naclSecretBoxArgsEncoder: Encoder[NaclSecretBox] = deriveCodec[NaclSecretBox]
}
