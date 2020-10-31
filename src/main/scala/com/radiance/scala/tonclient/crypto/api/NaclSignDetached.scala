package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSignDetached(unsigned: String, secret: String) extends Args {
  override val functionName: String = "crypto.nacl_sign_detached"
  override val fieldName: Option[String] = Some("signature")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object NaclSignDetached {
  implicit val naclSignDetachedArgsEncoder: Encoder[NaclSignDetached] = deriveCodec[NaclSignDetached]
}