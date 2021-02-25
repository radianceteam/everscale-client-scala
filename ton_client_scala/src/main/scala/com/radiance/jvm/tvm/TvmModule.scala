package com.radiance.jvm.tvm

import com.radiance.jvm._
import com.radiance.jvm.abi._
import com.radiance.jvm.boc._

import scala.concurrent.Future

class TvmModule(private val ctx: Context) {

  /**
   * @param message
   *   Input message BOC. Must be encoded as base64.
   * @param account
   *   Account to run on executor
   * @param execution_options
   *   Execution options.
   * @param abi
   *   Contract ABI for decoding output messages
   * @param skip_transaction_check
   *   Skip transaction check flag
   * @param boc_cache
   *   The BOC intself returned if no cache type provided
   * @param return_updated_account
   *   Empty string is returned if the flag is `false`
   */
  def runExecutor(
    message: String,
    account: AccountForExecutorADT.AccountForExecutor,
    execution_options: Option[ExecutionOptions],
    abi: Option[AbiADT.Abi],
    skip_transaction_check: Option[Boolean],
    boc_cache: Option[BocCacheTypeADT.BocCacheType],
    return_updated_account: Option[Boolean]
  ): Future[Either[Throwable, ResultOfRunExecutor]] = {
    ctx.execAsync[ParamsOfRunExecutor, ResultOfRunExecutor](
      "tvm.run_executor",
      ParamsOfRunExecutor(
        message,
        account,
        execution_options,
        abi,
        skip_transaction_check,
        boc_cache,
        return_updated_account
      )
    )
  }

  /**
   * Executes getmethod and returns data from TVM stack
   * @param account
   *   Account BOC in `base64`
   * @param function_name
   *   Function name
   * @param input
   *   Input parameters
   * @param execution_options
   *   Execution options
   */
  def runGet(
    account: String,
    function_name: String,
    input: Option[Value],
    execution_options: Option[ExecutionOptions]
  ): Future[Either[Throwable, ResultOfRunGet]] = {
    ctx.execAsync[ParamsOfRunGet, ResultOfRunGet](
      "tvm.run_get",
      ParamsOfRunGet(account, function_name, input, execution_options)
    )
  }

  /**
   * @param message
   *   Input message BOC. Must be encoded as base64.
   * @param account
   *   Account BOC. Must be encoded as base64.
   * @param execution_options
   *   Execution options.
   * @param abi
   *   Contract ABI for dedcoding output messages
   * @param boc_cache
   *   The BOC intself returned if no cache type provided
   * @param return_updated_account
   *   Empty string is returned if the flag is `false`
   */
  def runTvm(
    message: String,
    account: String,
    execution_options: Option[ExecutionOptions],
    abi: Option[AbiADT.Abi],
    boc_cache: Option[BocCacheTypeADT.BocCacheType],
    return_updated_account: Option[Boolean]
  ): Future[Either[Throwable, ResultOfRunTvm]] = {
    ctx.execAsync[ParamsOfRunTvm, ResultOfRunTvm](
      "tvm.run_tvm",
      ParamsOfRunTvm(message, account, execution_options, abi, boc_cache, return_updated_account)
    )
  }

}
