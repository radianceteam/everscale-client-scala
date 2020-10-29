package com.radiance.scala.tonclient

import com.radiance.scala.tonclient.types.{ExecutionOptions, ResultOfRunExecutor, ResultOfRunTvm}

import scala.concurrent.{ExecutionContext, Future}


/**
 *
 */
class Tvm(val context: TONContext)(implicit val ec: ExecutionContext) {
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
                 ): Future[ResultOfRunExecutor] =
    context.requestValue[ResultOfRunExecutor](
      "tvm.run_executor",
      s"""{
         |"message":"$message",
         |"account":"$account",
         |"execution_options":${executionOptions.toString},
         |"abi":"$abi",
         |"skip_transaction_check":$skipTransactionCheck
         |}""".stripMargin
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
            ): Future[ResultOfRunTvm] =
    context.requestValue[ResultOfRunTvm](
      "tvm.run_tvm",
      s"""{
         |"message":"$message",
         |"account":"$account",
         |"execution_options":${executionOptions},
         |"abi":"$abi"
         |}""".stripMargin
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
            ): Future[String] =
    context.requestField[String](
      "tvm.run_get",
      s"""{
         |"account":"$account",
         |"function_name":"$functionName",
         |"input":"$input",
         |"execution_options":${executionOptions.toString}
         |}""".stripMargin
    )("output")

}