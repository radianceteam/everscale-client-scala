package com.radiance.scala.tonclient.abi.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.out.ResultOfEncodeMessage
import io.circe._
import io.circe.derivation._

private[abi] case class EncodeMessage(
                                           abi: String,
                                           address: String,
                                           deploy_set: String,
                                           call_set: String,
                                           signer: String,
                                           processing_try_index: Long
                                         ) extends Api {
  override val functionName: String = "abi.encode_message"
  override type Out = ResultOfEncodeMessage
  override val decoder: Decoder[ResultOfEncodeMessage] = implicitly[Decoder[ResultOfEncodeMessage]]
}

private [abi] object EncodeMessage {
  implicit val encodeMessageArgsEncoder: Encoder[EncodeMessage] = deriveCodec[EncodeMessage]
}