package com.radiance.scala.tonclient.tvm

import com.radiance.scala.tonclient.TONContext
import com.radiance.scala.tonclient.tvm.args.{RunExecutorArgs, RunGetArgs, RunTvmArgs}
import com.radiance.scala.tonclient.types.both.ExecutionOptions
import com.radiance.scala.tonclient.types.out.{ResultOfRunExecutor, ResultOfRunTvm}

import scala.concurrent.{ExecutionContext, Future}

/**
 *
 */
class Tvm(val ctx: TONContext)(implicit val ec: ExecutionContext) {
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
                 ): Future[Either[Throwable, ResultOfRunExecutor]] = ctx.requestValue[RunExecutorArgs, ResultOfRunExecutor](
      RunExecutorArgs(message, account, executionOptions, abi, skipTransactionCheck)
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
            ): Future[Either[Throwable, String]] = ctx.requestField[RunGetArgs, String](
    RunGetArgs(account, functionName, input, executionOptions)
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
    .requestValue[RunTvmArgs, ResultOfRunTvm](RunTvmArgs(message, account, executionOptions, abi))
}