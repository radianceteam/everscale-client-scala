package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class HdkeySecretFromXprv(xprv: String) extends Args {
  override val functionName: String = "crypto.hdkey_secret_from_xprv"
  override val fieldName: Option[String] = Some("secret")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object HdkeySecretFromXprv {
  implicit val hdkeySecretFromXprvArgsEncoder: Encoder[HdkeySecretFromXprv] = deriveEncoder[HdkeySecretFromXprv]
}