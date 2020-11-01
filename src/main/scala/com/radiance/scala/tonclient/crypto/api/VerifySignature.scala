package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[crypto] case class VerifySignature(signed: String, public: String) extends Api {
  override val functionName: String = "crypto.verify_signature"
  override val fieldName: Option[String] = Some("unsigned")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object VerifySignature {
  implicit val VerifySignatureArgsEncoder: Encoder[VerifySignature] = deriveCodec[VerifySignature]
}
