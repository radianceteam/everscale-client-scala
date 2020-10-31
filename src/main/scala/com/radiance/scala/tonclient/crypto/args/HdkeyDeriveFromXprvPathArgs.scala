package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class HdkeyDeriveFromXprvPathArgs(xprv: String, path: String) extends Args {
  override val functionName: String = "crypto.hdkey_derive_from_xprv_path"
  override val fieldName: Option[String] = Some("xprv")
}

private[crypto] object HdkeyDeriveFromXprvPathArgs {
  implicit val HdkeyDeriveFromXprvPathArgsEncoder: Encoder[HdkeyDeriveFromXprvPathArgs] = deriveEncoder[HdkeyDeriveFromXprvPathArgs]
}
