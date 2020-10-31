package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class HdkeyPublicFromXprvArgs(xprv: String) extends Args {
  override val functionName: String = "crypto.hdkey_public_from_xprv"
  override val fieldName: Option[String] = Some("public")
}

private[crypto] object HdkeyPublicFromXprvArgs {
  implicit val hdkeyPublicFromXprvArgsEncoder: Encoder[HdkeyPublicFromXprvArgs] = deriveEncoder[HdkeyPublicFromXprvArgs]
}
