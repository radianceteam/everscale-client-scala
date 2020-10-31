package com.radiance.scala.tonclient.tvm.args

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.both.ExecutionOptions
import io.circe.Encoder
import io.circe.derivation.deriveCodec

private[tvm] case class RunExecutorArgs(
                                         message: String,
                                         account: String,
                                         execution_options: ExecutionOptions,
                                         abi: String,
                                         skip_transaction_check: Boolean
                                       ) extends Args {
  override val functionName: String = "tvm.run_executor"
}

private[tvm] object RunExecutorArgs {
  implicit val runExecutorArgsEncoder: Encoder[RunExecutorArgs] = deriveCodec[RunExecutorArgs]
}
