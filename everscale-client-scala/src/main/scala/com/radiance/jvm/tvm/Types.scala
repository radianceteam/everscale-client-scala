package com.radiance.jvm.tvm

import com.radiance.jvm._
import com.radiance.jvm.abi._
import com.radiance.jvm.boc._
import com.radiance.jvm.processing._
import io.circe._
import io.circe.derivation._
import io.circe.generic.extras

object AccountForExecutorADT {

  sealed trait AccountForExecutor

  case class Account(boc: String, unlimited_balance: Option[Boolean]) extends AccountForExecutor

  case object None extends AccountForExecutor

  case object Uninit extends AccountForExecutor

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[AccountForExecutor] =
    extras.semiauto.deriveConfiguredEncoder[AccountForExecutor]

}

case class ExecutionOptions(
  blockchain_config: Option[String],
  block_time: Option[Long],
  block_lt: Option[BigInt],
  transaction_lt: Option[BigInt],
  chksig_always_succeed: Option[Boolean],
  signature_id: Option[Int]
)

object ExecutionOptions {
  implicit val encoder: Encoder[ExecutionOptions] =
    deriveEncoder[ExecutionOptions]
}

case class ParamsOfRunExecutor(
  message: String,
  account: AccountForExecutorADT.AccountForExecutor,
  execution_options: Option[ExecutionOptions],
  abi: Option[AbiADT.Abi],
  skip_transaction_check: Option[Boolean],
  boc_cache: Option[BocCacheTypeADT.BocCacheType],
  return_updated_account: Option[Boolean]
)

object ParamsOfRunExecutor {
  implicit val encoder: Encoder[ParamsOfRunExecutor] =
    deriveEncoder[ParamsOfRunExecutor]
}

case class ParamsOfRunGet(
  account: String,
  function_name: String,
  input: Option[Value],
  execution_options: Option[ExecutionOptions],
  tuple_list_as_array: Option[Boolean]
)

object ParamsOfRunGet {
  implicit val encoder: Encoder[ParamsOfRunGet] =
    deriveEncoder[ParamsOfRunGet]
}

case class ParamsOfRunTvm(
  message: String,
  account: String,
  execution_options: Option[ExecutionOptions],
  abi: Option[AbiADT.Abi],
  boc_cache: Option[BocCacheTypeADT.BocCacheType],
  return_updated_account: Option[Boolean]
)

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
  total_output: BigInt,
  ext_in_msg_fee: BigInt,
  total_fwd_fees: BigInt,
  account_fees: BigInt
)

object TransactionFees {
  implicit val decoder: Decoder[TransactionFees] =
    deriveDecoder[TransactionFees]
}

object TvmErrorCodeEnum {

  sealed trait TvmErrorCode {
    val code: String
  }

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
