package com.radiance.jvm.processing

import com.radiance.jvm._
import com.radiance.jvm.abi._
import com.radiance.jvm.client.ClientError

import io.circe._
import io.circe.derivation._
import io.circe.generic.extras

case class DecodedOutput(
  out_messages: List[Option[DecodedMessageBody]],
  output: Option[Value]
)

object DecodedOutput {
  implicit val decoder: Decoder[DecodedOutput] = deriveDecoder[DecodedOutput]
}

case class MessageMonitoringParams(
  message: MonitoredMessageADT.MonitoredMessage,
  wait_until: Long,
  user_data: Option[Value]
)

object MessageMonitoringParams {
  implicit val codec: Codec[MessageMonitoringParams] =
    deriveCodec[MessageMonitoringParams]
}

case class MessageMonitoringResult(
  hash: String,
  status: MessageMonitoringStatusEnum.MessageMonitoringStatus,
  transaction: Option[MessageMonitoringTransaction],
  error: Option[String],
  user_data: Option[Value]
)

object MessageMonitoringResult {
  implicit val codec: Codec[MessageMonitoringResult] =
    deriveCodec[MessageMonitoringResult]
}

object MessageMonitoringStatusEnum {
  sealed trait MessageMonitoringStatus

  /**
   * Returned when the messages was processed and included into finalized block before `wait_until` block time.
   */
  case object Finalized extends MessageMonitoringStatus

  /**
   * Reserved for future statuses. Is never returned. Application should wait for one of the `Finalized` or `Timeout`
   * statuses. All other statuses are intermediate.
   */
  case object Reserved extends MessageMonitoringStatus

  /**
   * Returned when the message was not processed until `wait_until` block time.
   */
  case object Timeout extends MessageMonitoringStatus

  implicit val codec: Codec[MessageMonitoringStatus] =
    extras.semiauto.deriveEnumerationCodec[MessageMonitoringStatus]
}

case class MessageMonitoringTransaction(
  hash: Option[String],
  aborted: Boolean,
  compute: Option[MessageMonitoringTransactionCompute]
)

object MessageMonitoringTransaction {
  implicit val codec: Codec[MessageMonitoringTransaction] =
    deriveCodec[MessageMonitoringTransaction]
}

case class MessageMonitoringTransactionCompute(exit_code: Int)

object MessageMonitoringTransactionCompute {
  implicit val codec: Codec[MessageMonitoringTransactionCompute] =
    deriveCodec[MessageMonitoringTransactionCompute]
}

case class MessageSendingParams(boc: String, wait_until: Long, user_data: Option[Value])

object MessageSendingParams {
  implicit val encoder: Encoder[MessageSendingParams] =
    deriveEncoder[MessageSendingParams]
}

object MonitorFetchWaitModeEnum {
  sealed trait MonitorFetchWaitMode

  /**
   * Monitor waits until all unresolved messages will be resolved. If there are no unresolved messages then monitor will
   * wait.
   */
  case object All extends MonitorFetchWaitMode

  /**
   * If there are no resolved results yet, then monitor awaits for the next resolved result.
   */
  case object AtLeastOne extends MonitorFetchWaitMode

  case object NoWait extends MonitorFetchWaitMode

  implicit val encoder: Encoder[MonitorFetchWaitMode] =
    extras.semiauto.deriveEnumerationEncoder[MonitorFetchWaitMode]
}

object MonitoredMessageADT {
  sealed trait MonitoredMessage

  case class Boc(boc: String) extends MonitoredMessage

  case class HashAddress(hash: String, address: String) extends MonitoredMessage

  import com.radiance.jvm.DiscriminatorConfig._

  implicit val codec: Codec[MonitoredMessage] =
    extras.semiauto.deriveConfiguredCodec[MonitoredMessage]
}

case class MonitoringQueueInfo(unresolved: Long, resolved: Long)

object MonitoringQueueInfo {
  implicit val codec: Codec[MonitoringQueueInfo] =
    deriveCodec[MonitoringQueueInfo]
}

case class ParamsOfCancelMonitor(queue: String)

object ParamsOfCancelMonitor {
  implicit val encoder: Encoder[ParamsOfCancelMonitor] =
    deriveEncoder[ParamsOfCancelMonitor]
}

case class ParamsOfFetchNextMonitorResults(
  queue: String,
  wait_mode: Option[MonitorFetchWaitModeEnum.MonitorFetchWaitMode]
)

object ParamsOfFetchNextMonitorResults {
  implicit val encoder: Encoder[ParamsOfFetchNextMonitorResults] =
    deriveEncoder[ParamsOfFetchNextMonitorResults]
}

case class ParamsOfGetMonitorInfo(queue: String)

object ParamsOfGetMonitorInfo {
  implicit val encoder: Encoder[ParamsOfGetMonitorInfo] =
    deriveEncoder[ParamsOfGetMonitorInfo]
}

case class ParamsOfMonitorMessages(queue: String, messages: List[MessageMonitoringParams])

object ParamsOfMonitorMessages {
  implicit val encoder: Encoder[ParamsOfMonitorMessages] =
    deriveEncoder[ParamsOfMonitorMessages]
}

case class ParamsOfProcessMessage(
  message_encode_params: ParamsOfEncodeMessage,
  send_events: Boolean
)

object ParamsOfProcessMessage {
  implicit val encoder: Encoder[ParamsOfProcessMessage] =
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

case class ParamsOfSendMessages(messages: List[MessageSendingParams], monitor_queue: Option[String])

object ParamsOfSendMessages {
  implicit val encoder: Encoder[ParamsOfSendMessages] =
    deriveEncoder[ParamsOfSendMessages]
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

  case class DidSend(shard_block_id: String, message_id: String, message_dst: String, message: String)
      extends ProcessingEvent

  case class FetchFirstBlockFailed(error: ClientError, message_id: String, message_dst: String) extends ProcessingEvent

  case class FetchNextBlockFailed(
    shard_block_id: String,
    message_id: String,
    message_dst: String,
    message: String,
    error: ClientError
  ) extends ProcessingEvent

  case class MessageExpired(message_id: String, message_dst: String, message: String, error: ClientError)
      extends ProcessingEvent

  case class RempError(message_id: String, message_dst: String, error: ClientError) extends ProcessingEvent

  case class RempIncludedIntoAcceptedBlock(message_id: String, message_dst: String, timestamp: BigInt, json: Value)
      extends ProcessingEvent

  case class RempIncludedIntoBlock(message_id: String, message_dst: String, timestamp: BigInt, json: Value)
      extends ProcessingEvent

  case class RempOther(message_id: String, message_dst: String, timestamp: BigInt, json: Value) extends ProcessingEvent

  case class RempSentToValidators(message_id: String, message_dst: String, timestamp: BigInt, json: Value)
      extends ProcessingEvent

  case class SendFailed(
    shard_block_id: String,
    message_id: String,
    message_dst: String,
    message: String,
    error: ClientError
  ) extends ProcessingEvent

  case class WillFetchFirstBlock(message_id: String, message_dst: String) extends ProcessingEvent

  case class WillFetchNextBlock(shard_block_id: String, message_id: String, message_dst: String, message: String)
      extends ProcessingEvent

  case class WillSend(shard_block_id: String, message_id: String, message_dst: String, message: String)
      extends ProcessingEvent
}

case class ResultOfFetchNextMonitorResults(results: List[MessageMonitoringResult])

object ResultOfFetchNextMonitorResults {
  implicit val decoder: Decoder[ResultOfFetchNextMonitorResults] =
    deriveDecoder[ResultOfFetchNextMonitorResults]
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

case class ResultOfSendMessages(messages: List[MessageMonitoringParams])

object ResultOfSendMessages {
  implicit val decoder: Decoder[ResultOfSendMessages] =
    deriveDecoder[ResultOfSendMessages]
}
