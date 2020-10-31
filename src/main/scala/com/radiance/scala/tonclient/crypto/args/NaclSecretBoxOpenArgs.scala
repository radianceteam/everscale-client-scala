package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSecretBoxOpenArgs(encrypted: String, nonce: String, key: String) extends Args {
  override val functionName: String = "crypto.nacl_secret_box_open"
  override val fieldName: Option[String] = Some("decrypted")
}

private[crypto] object NaclSecretBoxOpenArgs {
  implicit val NaclSecretBoxOpenArgsEncoder: Encoder[NaclSecretBoxOpenArgs] = deriveCodec[NaclSecretBoxOpenArgs]
}
