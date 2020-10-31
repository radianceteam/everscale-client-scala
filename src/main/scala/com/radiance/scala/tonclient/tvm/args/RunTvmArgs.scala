package com.radiance.scala.tonclient.tvm.args

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.both.ExecutionOptions
import io.circe._
import io.circe.derivation._

private[tvm] case class RunTvmArgs(
                                    message: String,
                                    account: String,
                                    execution_options: ExecutionOptions,
                                    abi: String
                                  ) extends Args {
  override val functionName: String = "tvm.run_tvm"
}

private[tvm] object RunTvmArgs {
  implicit val RunTvmArgsEncoder: Encoder[RunTvmArgs] = deriveCodec[RunTvmArgs]
}
