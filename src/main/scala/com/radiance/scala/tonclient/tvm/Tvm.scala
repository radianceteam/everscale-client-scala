package com.radiance.scala.tonclient.tvm

import com.radiance.scala.tonclient.TonContext
import com.radiance.scala.tonclient.tvm.api.{RunExecutor, RunGet, RunTvm}
import com.radiance.scala.tonclient.types.both.ExecutionOptions
import com.radiance.scala.tonclient.types.out.{ResultOfRunExecutor, ResultOfRunTvm}

import scala.concurrent.{ExecutionContext, Future}

/**
 *
 */
class Tvm(val ctx: TonContext)(implicit val ec: ExecutionContext) {
  /**
   *
   *
   * @param message              Input message BOC. Must be encoded as base64.
   * @param account              Account to run on executor
   * @param executionOptions     Execution options.
   * @param abi                  Contract ABI for decoding output messages
   * @param skipTransactionCheck Skip transaction check flag
   */
  def runExecutor(
                   message: String,
                   account: String,
                   executionOptions: ExecutionOptions,
                   abi: String,
                   skipTransactionCheck: Boolean
                 ): Future[Either[Throwable, ResultOfRunExecutor]] = ctx.exec[RunExecutor](
      RunExecutor(message, account, executionOptions, abi, skipTransactionCheck)
    )

  /**
   * Executes getmethod and returns data from TVM stack
   *
   * @param account      Account BOC in `base64`
   * @param functionName Function name
   * @param input        Input parameters
   */
  def runGet(
              account: String,
              functionName: String,
              input: String,
              executionOptions: ExecutionOptions
            ): Future[Either[Throwable, String]] = ctx.exec(
    RunGet(account, functionName, input, executionOptions)
  )

  /**
   *
   *
   * @param message          Input message BOC. Must be encoded as base64.
   * @param account          Account BOC. Must be encoded as base64.
   * @param executionOptions Execution options.
   * @param abi              Contract ABI for dedcoding output messages
   */
  def runTvm(
              message: String,
              account: String,
              executionOptions: ExecutionOptions,
              abi: String
            ): Future[Either[Throwable, ResultOfRunTvm]] = ctx
    .exec[RunTvm](RunTvm(message, account, executionOptions, abi))
}