package com.radiance.jvm.tvm

import com.radiance.jvm._
import com.radiance.jvm.abi._
import com.radiance.jvm.boc._

import scala.concurrent.Future

class TvmModule(private val ctx: Context) {

  /**
   * Emulates all the phases of contract execution locally Performs all the phases of contract execution on Transaction
   * Executor - the same component that is used on Validator Nodes.
   *
   * Can be used for contract debugging, to find out the reason why a message was not delivered successfully. Validators
   * throw away the failed external inbound messages (if they failed before `ACCEPT`) in the real network. This is why
   * these messages are impossible to debug in the real network. With the help of run_executor you can do that. In fact,
   * `process_message` function performs local check with `run_executor` if there was no transaction as a result of
   * processing and returns the error, if there is one.
   *
   * Another use case to use `run_executor` is to estimate fees for message execution. Set
   * `AccountForExecutor::Account.unlimited_balance` to `true` so that emulation will not depend on the actual balance.
   * This may be needed to calculate deploy fees for an account that does not exist yet. JSON with fees is in `fees`
   * field of the result.
   *
   * One more use case - you can produce the sequence of operations, thus emulating the sequential contract calls
   * locally. And so on.
   *
   * Transaction executor requires account BOC (bag of cells) as a parameter. To get the account BOC - use `net.query`
   * method to download it from GraphQL API (field `boc` of `account`) or generate it with `abi.encode_account` method.
   *
   * Also it requires message BOC. To get the message BOC - use `abi.encode_message` or `abi.encode_internal_message`.
   *
   * If you need this emulation to be as precise as possible (for instance - emulate transaction with particular lt in
   * particular block or use particular blockchain config, downloaded from a particular key block - then specify
   * `execution_options` parameter.
   *
   * If you need to see the aborted transaction as a result, not as an error, set `skip_transaction_check` to `true`.
   *
   * @param message
   *   Must be encoded as base64.
   * @param account
   *   account
   * @param execution_options
   *   execution_options
   * @param abi
   *   abi
   * @param skip_transaction_check
   *   skip_transaction_check
   * @param boc_cache
   *   The BOC itself returned if no cache type provided
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
   * Executes a get-method of FIFT contract that fulfills the smc-guidelines https://test.ton.org/smc-guidelines.txt and
   * returns the result data from TVM's stack
   * @param account
   *   account
   * @param function_name
   *   function_name
   * @param input
   *   input
   * @param execution_options
   *   execution_options
   * @param tuple_list_as_array
   *   Default is `false`. Input parameters may use any of lists representations If you receive this error on Web:
   *   "Runtime error. Unreachable code should not be executed...", set this flag to true. This may happen, for example,
   *   when elector contract contains too many participants
   */
  def runGet(
    account: String,
    function_name: String,
    input: Option[Value],
    execution_options: Option[ExecutionOptions],
    tuple_list_as_array: Option[Boolean]
  ): Future[Either[Throwable, ResultOfRunGet]] = {
    ctx.execAsync[ParamsOfRunGet, ResultOfRunGet](
      "tvm.run_get",
      ParamsOfRunGet(account, function_name, input, execution_options, tuple_list_as_array)
    )
  }

  /**
   * Executes get-methods of ABI-compatible contracts Performs only a part of compute phase of transaction execution
   * that is used to run get-methods of ABI-compatible contracts.
   *
   * If you try to run get-methods with `run_executor` you will get an error, because it checks ACCEPT and exits if
   * there is none, which is actually true for get-methods.
   *
   * To get the account BOC (bag of cells) - use `net.query` method to download it from GraphQL API (field `boc` of
   * `account`) or generate it with `abi.encode_account method`. To get the message BOC - use `abi.encode_message` or
   * prepare it any other way, for instance, with FIFT script.
   *
   * Attention! Updated account state is produces as well, but only `account_state.storage.state.data` part of the BOC
   * is updated.
   * @param message
   *   Must be encoded as base64.
   * @param account
   *   Must be encoded as base64.
   * @param execution_options
   *   execution_options
   * @param abi
   *   abi
   * @param boc_cache
   *   The BOC itself returned if no cache type provided
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
