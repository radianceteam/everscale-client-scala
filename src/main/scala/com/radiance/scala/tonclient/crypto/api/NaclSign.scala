package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSign(unsigned: String, secret: String) extends Args {
  override val functionName: String = "crypto.nacl_sign"
  override val fieldName: Option[String] = Some("signed")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object NaclSign {
  implicit val naclSignArgsEncoder: Encoder[NaclSign] = deriveEncoder[NaclSign]
}