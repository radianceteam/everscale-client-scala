package com.radiance.jvm.processing

import com.radiance.jvm._
import com.radiance.jvm.abi._
import io.circe._
import io.circe.derivation._


case class DecodedOutput(
                          out_messages: List[Option[DecodedMessageBody]],
                          output: Option[Value]
                        )

object DecodedOutput {
  implicit val decoder: Decoder[DecodedOutput] = deriveDecoder[DecodedOutput]
}

case class ParamsOfProcessMessage(
                                   message_encode_params: ParamsOfEncodeMessage,
                                   send_events: Boolean
                                 ) extends Bind {
  override type Out = ResultOfProcessMessage
  override val decoder: Decoder[ResultOfProcessMessage] =
    implicitly[Decoder[ResultOfProcessMessage]]
}

object ParamsOfProcessMessage {
  implicit val ParamsOfProcessMessageEncoder
  : Encoder[ParamsOfProcessMessage] =
    deriveEncoder[ParamsOfProcessMessage]
}

case class ParamsOfSendMessage(
                                message: String,
                                abi: Option[Abi],
                                send_events: Boolean
                              ) extends Bind {
  override type Out = ResultOfSendMessage
  override val decoder: Decoder[ResultOfSendMessage] =
    implicitly[Decoder[ResultOfSendMessage]]
}

object ParamsOfSendMessage {
  implicit val encoder: Encoder[ParamsOfSendMessage] =
    deriveEncoder[ParamsOfSendMessage]
}

case class ParamsOfWaitForTransaction(
                                       abi: Option[Abi],
                                       message: String,
                                       shard_block_id: String,
                                       send_events: Boolean
                                     ) extends Bind {
  override type Out = ResultOfProcessMessage
  override val decoder: Decoder[ResultOfProcessMessage] =
    implicitly[Decoder[ResultOfProcessMessage]]
}

object ParamsOfWaitForTransaction {
  implicit val encoder: Encoder[ParamsOfWaitForTransaction] =
    deriveEncoder[ParamsOfWaitForTransaction]
}

case class ResultOfProcessMessage(
                                   transaction: Value,
                                   out_messages: List[String],
                                   decoded: Option[DecodedOutput],
                                   fees: com.radiance.jvm.tvm.TransactionFees
                                 )

object ResultOfProcessMessage {
  implicit val decoder: Decoder[ResultOfProcessMessage] =
    deriveDecoder[ResultOfProcessMessage]
}

case class ResultOfSendMessage(shard_block_id: String)

object ResultOfSendMessage {
  implicit val decoder: Decoder[ResultOfSendMessage] =
    deriveDecoder[ResultOfSendMessage]
}

