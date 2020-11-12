package com.radiance.scala.methods

import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.types.AbiTypes._
import com.radiance.scala.types.TvmTypes._

import scala.concurrent.Future

class TvmModule(private val ctx: TonContextScala) {
  type Value = String
  /**
   *  Executes getmethod and returns data from TVM stack@param account  Account BOC in `base64`
   * @param function_name  Function name
   * @param input  Input parameters
   * @param execution_options
   */
  def run_get(account: String, function_name: String, input: Option[Value], execution_options: Option[ExecutionOptions]): Future[Either[Throwable, ResultOfRunGet]] = {
    val arg = ParamsOfRunGet(account, function_name, input, execution_options)
    ctx.execAsync("tvm.run_get", arg)
  }

  /**
   * @param message  Input message BOC. Must be encoded as base64.
   * @param account  Account BOC. Must be encoded as base64.
   * @param execution_options  Execution options.
   * @param abi  Contract ABI for dedcoding output messages
   */
  def run_tvm(message: String, account: String, execution_options: Option[ExecutionOptions], abi: Option[Abi]): Future[Either[Throwable, ResultOfRunTvm]] = {
    val arg = ParamsOfRunTvm(message, account, execution_options, abi)
    ctx.execAsync("tvm.run_tvm", arg)
  }
  /**
   * @param message  Input message BOC. Must be encoded as base64.
   * @param account  Account to run on executor
   * @param execution_options  Execution options.
   * @param abi  Contract ABI for decoding output messages
   * @param skip_transaction_check  Skip transaction check flag
   */
  def run_executor(message: String, account: AccountForExecutor, execution_options: Option[ExecutionOptions], abi: Option[Abi], skip_transaction_check: Option[Boolean]): Future[Either[Throwable, ResultOfRunExecutor]] = {
    val arg = ParamsOfRunExecutor(message, account, execution_options, abi, skip_transaction_check)
    ctx.execAsync("tvm.run_executor", arg)
  }
}
