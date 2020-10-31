package com.radiance.scala.tonclient.abi.api

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.out.ResultOfEncodeMessageBody
import io.circe._
import io.circe.derivation._

private[abi] case class EncodeMessageBody(
                                               abi: String,
                                               call_set: String,
                                               is_internal: Boolean,
                                               signer: String,
                                               processing_try_index: Long
                                             ) extends Args {
  override val functionName: String = "abi.encode_message_body"
  override type Out = ResultOfEncodeMessageBody
  override val decoder: Decoder[ResultOfEncodeMessageBody] = implicitly[Decoder[ResultOfEncodeMessageBody]]
}

private [abi] object EncodeMessageBody {
  implicit val encodeMessageBodyArgsEncoder: Encoder[EncodeMessageBody] = deriveEncoder[EncodeMessageBody]
}
