package com.radiance.scala.tonclient.abi.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[abi] case class AttachSignatureToMessageBodyArgs(
                                                          abi: String,
                                                          public_key: String,
                                                          message: String,
                                                          signature: String
                                                        ) extends Args {
  override val functionName: String = "abi.attach_signature_to_message_body"

  override val fieldName: Option[String] = Some("body")

}

private [abi] case object AttachSignatureToMessageBodyArgs {
  implicit val attachSignatureToMessageBodyArgsEncoder: Encoder[AttachSignatureToMessageBodyArgs] =
    deriveEncoder[AttachSignatureToMessageBodyArgs]
}