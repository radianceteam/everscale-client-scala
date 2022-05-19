package com.radiance.jvm.processing

import com.radiance.jvm._
import com.radiance.jvm.abi._
import com.radiance.jvm.client.ClientError

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
)

object ParamsOfProcessMessage {
  implicit val ParamsOfProcessMessageEncoder: Encoder[ParamsOfProcessMessage] =
    deriveEncoder[ParamsOfProcessMessage]
}

case class ParamsOfSendMessage(
  message: String,
  abi: Option[AbiADT.Abi],
  send_events: Boolean
)

object ParamsOfSendMessage {
  implicit val encoder: Encoder[ParamsOfSendMessage] =
    deriveEncoder[ParamsOfSendMessage]
}

case class ParamsOfWaitForTransaction(
  abi: Option[AbiADT.Abi],
  message: String,
  shard_block_id: String,
  send_events: Boolean,
  sending_endpoints: Option[List[String]]
)

object ParamsOfWaitForTransaction {
  implicit val encoder: Encoder[ParamsOfWaitForTransaction] =
    deriveEncoder[ParamsOfWaitForTransaction]
}
object ProcessingErrorCodeEnum {
  sealed trait ProcessingErrorCode {
    val code: String
  }
  case object BlockNotFound extends ProcessingErrorCode {
    override val code: String = "511"
  }
  case object CanNotBuildMessageCell extends ProcessingErrorCode {
    override val code: String = "503"
  }
  case object CanNotCheckBlockShard extends ProcessingErrorCode {
    override val code: String = "510"
  }
  case object ExternalSignerMustNotBeUsed extends ProcessingErrorCode {
    override val code: String = "513"
  }
  case object FetchBlockFailed extends ProcessingErrorCode {
    override val code: String = "504"
  }
  case object InvalidBlockReceived extends ProcessingErrorCode {
    override val code: String = "509"
  }
  case object InvalidData extends ProcessingErrorCode {
    override val code: String = "512"
  }
  case object InvalidMessageBoc extends ProcessingErrorCode {
    override val code: String = "506"
  }
  case object InvalidRempStatus extends ProcessingErrorCode {
    override val code: String = "515"
  }
  case object MessageAlreadyExpired extends ProcessingErrorCode {
    override val code: String = "501"
  }
  case object MessageExpired extends ProcessingErrorCode {
    override val code: String = "507"
  }
  case object MessageHasNotDestinationAddress extends ProcessingErrorCode {
    override val code: String = "502"
  }
  case object MessageRejected extends ProcessingErrorCode {
    override val code: String = "514"
  }
  case object NextRempStatusTimeout extends ProcessingErrorCode {
    override val code: String = "516"
  }
  case object SendMessageFailed extends ProcessingErrorCode {
    override val code: String = "505"
  }
  case object TransactionWaitTimeout extends ProcessingErrorCode {
    override val code: String = "508"
  }
}

object ProcessingEventADT {
  sealed trait ProcessingEvent
  case class DidSend(shard_block_id: String, message_id: String, message: String) extends ProcessingEvent
  case class FetchFirstBlockFailed(error: ClientError) extends ProcessingEvent
  case class FetchNextBlockFailed(shard_block_id: String, message_id: String, message: String, error: ClientError)
      extends ProcessingEvent
  case class MessageExpired(message_id: String, message: String, error: ClientError) extends ProcessingEvent
  case class RempError(error: ClientError) extends ProcessingEvent
  case class RempIncludedIntoAcceptedBlock(message_id: String, timestamp: BigInt, json: Value) extends ProcessingEvent
  case class RempIncludedIntoBlock(message_id: String, timestamp: BigInt, json: Value) extends ProcessingEvent
  case class RempOther(message_id: String, timestamp: BigInt, json: Value) extends ProcessingEvent
  case class RempSentToValidators(message_id: String, timestamp: BigInt, json: Value) extends ProcessingEvent
  case class SendFailed(shard_block_id: String, message_id: String, message: String, error: ClientError)
      extends ProcessingEvent

  /**
   * Notifies the application that the account's current shard block will be fetched from the network. This step is
   * performed before the message sending so that sdk knows starting from which block it will search for the
   * transaction. Fetched block will be used later in waiting phase.
   */
  case object WillFetchFirstBlock extends ProcessingEvent
  case class WillFetchNextBlock(shard_block_id: String, message_id: String, message: String) extends ProcessingEvent
  case class WillSend(shard_block_id: String, message_id: String, message: String) extends ProcessingEvent
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

case class ResultOfSendMessage(shard_block_id: String, sending_endpoints: List[String])

object ResultOfSendMessage {
  implicit val decoder: Decoder[ResultOfSendMessage] =
    deriveDecoder[ResultOfSendMessage]
}
