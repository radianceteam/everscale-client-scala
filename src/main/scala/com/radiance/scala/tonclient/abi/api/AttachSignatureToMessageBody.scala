package com.radiance.scala.tonclient.abi.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[abi] case class AttachSignatureToMessageBody(
                                                          abi: String,
                                                          public_key: String,
                                                          message: String,
                                                          signature: String
                                                        ) extends Api {
  override val functionName: String = "abi.attach_signature_to_message_body"

  override val fieldName: Option[String] = Some("body")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private [abi] case object AttachSignatureToMessageBody {
  implicit val attachSignatureToMessageBodyArgsEncoder: Encoder[AttachSignatureToMessageBody] =
    deriveEncoder[AttachSignatureToMessageBody]
}