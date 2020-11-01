package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[crypto] case class HdkeyDeriveFromXprvPath(xprv: String, path: String) extends Api {
  override val functionName: String = "crypto.hdkey_derive_from_xprv_path"
  override val fieldName: Option[String] = Some("xprv")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object HdkeyDeriveFromXprvPath {
  implicit val HdkeyDeriveFromXprvPathArgsEncoder: Encoder[HdkeyDeriveFromXprvPath] = deriveEncoder[HdkeyDeriveFromXprvPath]
}
