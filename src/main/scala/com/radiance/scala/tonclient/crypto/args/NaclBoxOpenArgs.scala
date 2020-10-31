package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclBoxOpenArgs(
                                            encrypted: String,
                                            nonce: String,
                                            their_public: String,
                                            secret: String
                                          ) extends Args {
  override val functionName: String = "crypto.nacl_box_open"
  override val fieldName: Option[String] = Some("decrypted")
}

private[crypto] object NaclBoxOpenArgs {
  implicit val NaclBoxOpenArgsEncoder: Encoder[NaclBoxOpenArgs] = deriveEncoder[NaclBoxOpenArgs]
}
