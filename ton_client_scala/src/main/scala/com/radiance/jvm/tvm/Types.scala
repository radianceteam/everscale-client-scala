package com.radiance.jvm.tvm

import com.radiance.jvm._
import com.radiance.jvm.abi._
import com.radiance.jvm.processing._
import io.circe._
import io.circe.derivation._

sealed trait AccountForExecutor

object AccountForExecutor {

  import io.circe.Json._
  import io.circe.syntax._

  case class Account(boc: String, unlimited_balance: Option[Boolean]) extends AccountForExecutor

  object Account {
    implicit val encoder: Encoder[Account] = deriveEncoder[Account]
  }

  case object None extends AccountForExecutor

  case object Uninit extends AccountForExecutor

  implicit val encoder: Encoder[AccountForExecutor] = {
    case None       => fromFields(Seq("type" -> fromString("None")))
    case Uninit     => fromFields(Seq("type" -> fromString("Uninit")))
    case a: Account => a.asJson.deepMerge(Utils.generateType(a))
  }

}

case class ExecutionOptions(
  blockchain_config: Option[String],
  block_time: Option[Long],
  block_lt: Option[BigInt],
  transaction_lt: Option[BigInt]
)

object ExecutionOptions {
  implicit val encoder: Encoder[ExecutionOptions] =
    deriveEncoder[ExecutionOptions]
}

case class ParamsOfRunExecutor(
  message: String,
  account: AccountForExecutor,
  execution_options: Option[ExecutionOptions],
  abi: Option[Abi],
  skip_transaction_check: Option[Boolean]
) extends Bind {
  override type Out = ResultOfRunExecutor
  override val decoder: Decoder[ResultOfRunExecutor] =
    implicitly[Decoder[ResultOfRunExecutor]]
}

object ParamsOfRunExecutor {
  implicit val encoder: Encoder[ParamsOfRunExecutor] =
    deriveEncoder[ParamsOfRunExecutor]
}

case class ParamsOfRunGet(
  account: String,
  function_name: String,
  input: Option[Value],
  execution_options: Option[ExecutionOptions]
) extends Bind {
  override type Out = ResultOfRunGet
  override val decoder: Decoder[ResultOfRunGet] =
    implicitly[Decoder[ResultOfRunGet]]
}

object ParamsOfRunGet {
  implicit val encoder: Encoder[ParamsOfRunGet] =
    deriveEncoder[ParamsOfRunGet]
}

case class ParamsOfRunTvm(
  message: String,
  account: String,
  execution_options: Option[ExecutionOptions],
  abi: Option[Abi]
) extends Bind {
  override type Out = ResultOfRunTvm
  override val decoder: Decoder[ResultOfRunTvm] =
    implicitly[Decoder[ResultOfRunTvm]]
}

object ParamsOfRunTvm {
  implicit val encoder: Encoder[ParamsOfRunTvm] =
    deriveEncoder[ParamsOfRunTvm]
}

case class ResultOfRunExecutor(
  transaction: Value,
  out_messages: List[String],
  decoded: Option[com.radiance.jvm.processing.DecodedOutput],
  account: String,
  fees: TransactionFees
)

object ResultOfRunExecutor {
  implicit val decoder: Decoder[ResultOfRunExecutor] =
    deriveDecoder[ResultOfRunExecutor]
}

case class ResultOfRunGet(output: Value)

object ResultOfRunGet {
  implicit val decoder: Decoder[ResultOfRunGet] =
    deriveDecoder[ResultOfRunGet]
}

case class ResultOfRunTvm(
  out_messages: List[String],
  decoded: Option[DecodedOutput],
  account: String
)

object ResultOfRunTvm {
  implicit val decoder: Decoder[ResultOfRunTvm] =
    deriveDecoder[ResultOfRunTvm]
}

case class TransactionFees(
  in_msg_fwd_fee: BigInt,
  storage_fee: BigInt,
  gas_fee: BigInt,
  out_msgs_fwd_fee: BigInt,
  total_account_fees: BigInt,
  total_output: BigInt
)

object TransactionFees {
  implicit val decoder: Decoder[TransactionFees] =
    deriveDecoder[TransactionFees]
}

sealed trait TvmErrorCode {
  val code: String
}

object TvmErrorCode {

  case object CanNotReadTransaction extends TvmErrorCode {
    override val code: String = "401"
  }

  case object CanNotReadBlockchainConfig extends TvmErrorCode {
    override val code: String = "402"
  }

  case object TransactionAborted extends TvmErrorCode {
    override val code: String = "403"
  }

  case object InternalError extends TvmErrorCode {
    override val code: String = "404"
  }

  case object ActionPhaseFailed extends TvmErrorCode {
    override val code: String = "405"
  }

  case object AccountCodeMissing extends TvmErrorCode {
    override val code: String = "406"
  }

  case object LowBalance extends TvmErrorCode {
    override val code: String = "407"
  }

  case object AccountFrozenOrDeleted extends TvmErrorCode {
    override val code: String = "408"
  }

  case object AccountMissing extends TvmErrorCode {
    override val code: String = "409"
  }

  case object UnknownExecutionError extends TvmErrorCode {
    override val code: String = "410"
  }

  case object InvalidInputStack extends TvmErrorCode {
    override val code: String = "411"
  }

  case object InvalidAccountBoc extends TvmErrorCode {
    override val code: String = "412"
  }

  case object InvalidMessageType extends TvmErrorCode {
    override val code: String = "413"
  }

  case object ContractExecutionError extends TvmErrorCode {
    override val code: String = "414"
  }

}
