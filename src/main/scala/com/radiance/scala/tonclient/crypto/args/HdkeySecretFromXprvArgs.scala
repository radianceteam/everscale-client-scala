package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class HdkeySecretFromXprvArgs(xprv: String) extends Args {
  override val functionName: String = "crypto.hdkey_secret_from_xprv"
  override val fieldName: Option[String] = Some("secret")
}

private[crypto] object HdkeySecretFromXprvArgs {
  implicit val hdkeySecretFromXprvArgsEncoder: Encoder[HdkeySecretFromXprvArgs] = deriveEncoder[HdkeySecretFromXprvArgs]
}