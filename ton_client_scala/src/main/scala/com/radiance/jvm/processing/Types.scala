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
  send_events: Boolean
)

object ParamsOfWaitForTransaction {
  implicit val encoder: Encoder[ParamsOfWaitForTransaction] =
    deriveEncoder[ParamsOfWaitForTransaction]
}

sealed trait ProcessingErrorCode {
  val code: String
}
object ProcessingErrorCode {

  case object MessageAlreadyExpired extends ProcessingErrorCode {
    override val code: String = "501"
  }

  case object MessageHasNotDestinationAddress extends ProcessingErrorCode {
    override val code: String = "502"
  }

  case object CanNotBuildMessageCell extends ProcessingErrorCode {
    override val code: String = "503"
  }

  case object FetchBlockFailed extends ProcessingErrorCode {
    override val code: String = "504"
  }

  case object SendMessageFailed extends ProcessingErrorCode {
    override val code: String = "505"
  }

  case object InvalidMessageBoc extends ProcessingErrorCode {
    override val code: String = "506"
  }

  case object MessageExpired extends ProcessingErrorCode {
    override val code: String = "507"
  }

  case object TransactionWaitTimeout extends ProcessingErrorCode {
    override val code: String = "508"
  }

  case object InvalidBlockReceived extends ProcessingErrorCode {
    override val code: String = "509"
  }

  case object CanNotCheckBlockShard extends ProcessingErrorCode {
    override val code: String = "510"
  }

  case object BlockNotFound extends ProcessingErrorCode {
    override val code: String = "511"
  }

  case object InvalidData extends ProcessingErrorCode {
    override val code: String = "512"
  }

  case object ExternalSignerMustNotBeUsed extends ProcessingErrorCode {
    override val code: String = "513"
  }

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
