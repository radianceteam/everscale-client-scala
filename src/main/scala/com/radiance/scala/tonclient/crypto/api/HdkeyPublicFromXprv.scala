package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class HdkeyPublicFromXprv(xprv: String) extends Args {
  override val functionName: String = "crypto.hdkey_public_from_xprv"
  override val fieldName: Option[String] = Some("public")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object HdkeyPublicFromXprv {
  implicit val hdkeyPublicFromXprvArgsEncoder: Encoder[HdkeyPublicFromXprv] = deriveEncoder[HdkeyPublicFromXprv]
}
