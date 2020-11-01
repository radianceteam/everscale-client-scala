package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import io.circe.{Decoder, Encoder}
import io.circe.derivation.deriveCodec

private[crypto] case class NaclBox(
                                        decrypted: String,
                                        nonce: String,
                                        their_public: String,
                                        secret: String
                                      ) extends Api {
  override val functionName: String = "crypto.nacl_box"
  override val fieldName: Option[String] = Some("encrypted")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object NaclBox {
  implicit val NaclBoxArgsEncoder: Encoder[NaclBox] = deriveCodec[NaclBox]
}
