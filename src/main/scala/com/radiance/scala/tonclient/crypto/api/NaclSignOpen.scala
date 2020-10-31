package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSignOpen(signed: String, public: String) extends Args {
  override val functionName: String = "crypto.nacl_sign_open"
  override val fieldName: Option[String] = Some("unsigned")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object NaclSignOpen {
  implicit val NaclSignOpenArgsEncoder: Encoder[NaclSignOpen] = deriveCodec[NaclSignOpen]
}
