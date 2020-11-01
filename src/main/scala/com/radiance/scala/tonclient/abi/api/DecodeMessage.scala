package com.radiance.scala.tonclient.abi.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.out.DecodedMessageBody
import io.circe._
import io.circe.derivation._

private[abi] case class DecodeMessage(
                                           abi: String,
                                           message: String
                                         ) extends Api {
  override val functionName: String = "abi.decode_message"
  override type Out = DecodedMessageBody
  override val decoder: Decoder[DecodedMessageBody] = implicitly[Decoder[DecodedMessageBody]]
}

private[abi] object DecodeMessage {
  implicit val decodeMessageArgsEncoder: Encoder[DecodeMessage] = deriveCodec[DecodeMessage]
}
