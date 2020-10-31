package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSecretBoxOpen(encrypted: String, nonce: String, key: String) extends Args {
  override val functionName: String = "crypto.nacl_secret_box_open"
  override val fieldName: Option[String] = Some("decrypted")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object NaclSecretBoxOpen {
  implicit val NaclSecretBoxOpenArgsEncoder: Encoder[NaclSecretBoxOpen] = deriveCodec[NaclSecretBoxOpen]
}
