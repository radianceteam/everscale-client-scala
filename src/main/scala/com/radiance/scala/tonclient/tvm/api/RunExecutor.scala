package com.radiance.scala.tonclient.tvm.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.both.ExecutionOptions
import com.radiance.scala.tonclient.types.out.ResultOfRunExecutor
import io.circe.{Decoder, Encoder}
import io.circe.derivation.deriveCodec

private[tvm] case class RunExecutor(
                                         message: String,
                                         account: String,
                                         execution_options: ExecutionOptions,
                                         abi: String,
                                         skip_transaction_check: Boolean
                                       ) extends Api {
  override val functionName: String = "tvm.run_executor"
  override type Out = ResultOfRunExecutor
  override val decoder: Decoder[ResultOfRunExecutor] = implicitly[Decoder[ResultOfRunExecutor]]
}

private[tvm] object RunExecutor {
  implicit val runExecutorArgsEncoder: Encoder[RunExecutor] = deriveCodec[RunExecutor]
}
