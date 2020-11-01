package com.radiance.scala.tonclient.abi.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.out.DecodedMessageBody
import io.circe._
import io.circe.derivation._

private[abi] case class DecodeMessageBody(
                                               abi: String,
                                               body: String,
                                               is_internal: Boolean
                                             ) extends Api {
  override val functionName: String = "abi.decode_message_body"
  override type Out = DecodedMessageBody
  override val decoder: Decoder[DecodedMessageBody] = implicitly[Decoder[DecodedMessageBody]]
}

private[abi] object DecodeMessageBody {
  implicit val decodeMessageBodyArgsEncoder: Encoder[DecodeMessageBody] = deriveEncoder[DecodeMessageBody]
}