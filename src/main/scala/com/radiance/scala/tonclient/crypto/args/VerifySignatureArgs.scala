package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class VerifySignatureArgs(signed: String, public: String) extends Args {
  override val functionName: String = "crypto.verify_signature"
  override val fieldName: Option[String] = Some("unsigned")
}

private[crypto] object VerifySignatureArgs {
  implicit val VerifySignatureArgsEncoder: Encoder[VerifySignatureArgs] = deriveCodec[VerifySignatureArgs]
}
