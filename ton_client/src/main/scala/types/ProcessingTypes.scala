package types
import io.circe.{Decoder, Encoder}
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import types.AbiTypes._

object ProcessingTypes {
  type Value = String

  sealed trait ProcessingEvent

  case class WillFetchFirstBlock() extends ProcessingEvent

  case class FetchFirstBlockFailed(error: ClientTypes.ClientError) extends ProcessingEvent

  case class WillSend(shard_block_id: String, message_id: String, message: String) extends ProcessingEvent

  case class DidSend(shard_block_id: String, message_id: String, message: String) extends ProcessingEvent

  case class SendFailed(shard_block_id: String, message_id: String, message: String, error: ClientTypes.ClientError) extends ProcessingEvent

  case class WillFetchNextBlock(shard_block_id: String, message_id: String, message: String) extends ProcessingEvent

  case class FetchNextBlockFailed(shard_block_id: String, message_id: String, message: String, error: ClientTypes.ClientError) extends ProcessingEvent

  case class MessageExpired(message_id: String, message: String, error: ClientTypes.ClientError) extends ProcessingEvent

  case class ResultOfProcessMessage(transaction: Value, out_messages: List[String], decoded: Option[DecodedOutput], fees: TvmTypes.TransactionFees)

  case class DecodedOutput(out_messages: List[Option[DecodedMessageBody]], output: Option[Value])

  case class ParamsOfSendMessage(message: String, abi: Option[Abi], send_events: Boolean) extends ApiNew {
    override type Out = ResultOfSendMessage
    override val decoder: Decoder[ResultOfSendMessage] = implicitly[Decoder[ResultOfSendMessage]]
  }

  case class ResultOfSendMessage(shard_block_id: String)

  case class ParamsOfWaitForTransaction(abi: Option[Abi], message: String, shard_block_id: String, send_events: Boolean) extends ApiNew {
    override type Out = ResultOfProcessMessage
    override val decoder: Decoder[ResultOfProcessMessage] = implicitly[Decoder[ResultOfProcessMessage]]
  }

  case class ParamsOfProcessMessage(message_encode_params: ParamsOfEncodeMessage, send_events: Boolean) extends ApiNew {
    override type Out = ResultOfProcessMessage
    override val decoder: Decoder[ResultOfProcessMessage] = implicitly[Decoder[ResultOfProcessMessage]]
  }


  object ProcessingEvent {

  }

  object WillFetchFirstBlock {

  }

  object FetchFirstBlockFailed {

  }

  object WillSend {

  }

  object DidSend {

  }

  object SendFailed {

  }

  object WillFetchNextBlock {

  }

  object FetchNextBlockFailed {

  }

  object MessageExpired {

  }

  object ResultOfProcessMessage {
    implicit val ResultOfProcessMessageDecoder: Decoder[ResultOfProcessMessage] =
      deriveDecoder[ResultOfProcessMessage]
  }

  object DecodedOutput {
    implicit val DecodedOutputDecoder: Decoder[DecodedOutput] = deriveDecoder[DecodedOutput]
  }

  object ParamsOfSendMessage {
    implicit val ParamsOfSendMessageEncoder: Encoder[ParamsOfSendMessage] = deriveEncoder[ParamsOfSendMessage]
  }

  object ResultOfSendMessage {
    implicit val ResultOfSendMessageDecoder: Decoder[ResultOfSendMessage] = deriveDecoder[ResultOfSendMessage]
  }

  object ParamsOfWaitForTransaction {
    implicit val ParamsOfWaitForTransactionEncoder: Encoder[ParamsOfWaitForTransaction] = deriveEncoder[ParamsOfWaitForTransaction]
  }

  object ParamsOfProcessMessage {
    implicit val ParamsOfProcessMessageEncoder: Encoder[ParamsOfProcessMessage] = deriveEncoder[ParamsOfProcessMessage]
  }

}
