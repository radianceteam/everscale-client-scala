package com.radiance.scala.types

import com.radiance.scala.types.AbiTypes._
import com.radiance.scala.types.ProcessingTypes.DecodedOutput
import io.circe
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import io.circe.syntax._

object TvmTypes {
  type Value = circe.Json

  case class ExecutionOptions(blockchain_config: Option[String], block_time: Option[Long], block_lt: Option[BigInt], transaction_lt: Option[BigInt])

  sealed trait AccountForExecutor

  case object NoneAccount extends AccountForExecutor

  case object UninitAccount extends AccountForExecutor

  case class Account(boc: String, unlimited_balance: Option[Boolean]) extends AccountForExecutor

  case class TransactionFees(in_msg_fwd_fee: BigInt, storage_fee: BigInt, gas_fee: BigInt, out_msgs_fwd_fee: BigInt, total_account_fees: BigInt, total_output: BigInt)

  case class ParamsOfRunExecutor(message: String, account: AccountForExecutor, execution_options: Option[ExecutionOptions], abi: Option[Abi], skip_transaction_check: Option[Boolean]) extends ApiNew {
    override type Out = ResultOfRunExecutor
    override val decoder: Decoder[ResultOfRunExecutor] = implicitly[Decoder[ResultOfRunExecutor]]
  }

  case class ResultOfRunExecutor(transaction: Value, out_messages: List[String], decoded: Option[ProcessingTypes.DecodedOutput], account: String, fees: TransactionFees)

  case class ParamsOfRunTvm(message: String, account: String, execution_options: Option[ExecutionOptions], abi: Option[Abi]) extends ApiNew {
    override type Out = ResultOfRunTvm
    override val decoder: Decoder[ResultOfRunTvm] = implicitly[Decoder[ResultOfRunTvm]]
  }

  case class ResultOfRunTvm(out_messages: List[String], decoded: Option[DecodedOutput], account: String)

  case class ParamsOfRunGet(account: String, function_name: String, input: Option[Value], execution_options: Option[ExecutionOptions]) extends ApiNew {
    override type Out = ResultOfRunGet
    override val decoder: Decoder[ResultOfRunGet] = implicitly[Decoder[ResultOfRunGet]]
  }

  case class ResultOfRunGet(output: Value)


  object ExecutionOptions {
    implicit val ExecutionOptionsEncoder: Encoder[ExecutionOptions] = deriveEncoder[ExecutionOptions]
  }

  object AccountForExecutor {
    implicit val AccountForExecutorEncoder: Encoder[AccountForExecutor] = {
      case NoneAccount => circe.Json.fromFields(Seq("type" -> "None".asJson))
      case UninitAccount => circe.Json.fromFields(Seq("type" -> "Uninit".asJson))
      case a: Account => a.asJson.deepMerge(Utils.generateType(a))
    }
  }

  object Account {
    implicit val AccountEncoder: Encoder[Account] = deriveEncoder[Account]
  }

  object TransactionFees {
    implicit val TransactionFeesDecoder: Decoder[TransactionFees] = deriveDecoder[TransactionFees]
  }

  object ParamsOfRunExecutor {
    implicit val ParamsOfRunExecutorEncoder: Encoder[ParamsOfRunExecutor] = deriveEncoder[ParamsOfRunExecutor]
  }

  object ResultOfRunExecutor {
    implicit val ResultOfRunExecutorDecoder: Decoder[ResultOfRunExecutor] = deriveDecoder[ResultOfRunExecutor]
  }

  object ParamsOfRunTvm {
    implicit val ParamsOfRunTvmEncoder: Encoder[ParamsOfRunTvm] = deriveEncoder[ParamsOfRunTvm]
  }

  object ResultOfRunTvm {
    implicit val ResultOfRunTvmDecoder: Decoder[ResultOfRunTvm] = deriveDecoder[ResultOfRunTvm]
  }

  object ParamsOfRunGet {
    implicit val ParamsOfRunGetEncoder: Encoder[ParamsOfRunGet] = deriveEncoder[ParamsOfRunGet]
  }

  object ResultOfRunGet {
    implicit val ResultOfRunGetDecoder: Decoder[ResultOfRunGet] = deriveDecoder[ResultOfRunGet]
  }

}
