package com.radiance.scala.tonclient.abi.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.out.ResultOfAttachSignature
import io.circe._
import io.circe.derivation._

private [abi] case class AttachSignature(
                                             abi: String,
                                             public_key: String,
                                             message: String,
                                             signature: String
                                           ) extends Api {
  val functionName: String = "abi.attach_signature"
  override type Out = ResultOfAttachSignature
  override val decoder: Decoder[ResultOfAttachSignature] = implicitly[Decoder[ResultOfAttachSignature]]
}

private [abi] object AttachSignature {
  implicit val attachSignatureArgsEncoder: Encoder[AttachSignature] = deriveCodec[AttachSignature]
}


