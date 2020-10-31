package com.radiance.scala.tonclient.tvm.api

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.both.ExecutionOptions
import com.radiance.scala.tonclient.types.out.ResultOfRunTvm
import io.circe._
import io.circe.derivation._

private[tvm] case class RunTvm(
                                    message: String,
                                    account: String,
                                    execution_options: ExecutionOptions,
                                    abi: String
                                  ) extends Args {
  override val functionName: String = "tvm.run_tvm"
  override type Out = ResultOfRunTvm
  override val decoder: Decoder[ResultOfRunTvm] = implicitly[Decoder[ResultOfRunTvm]]
}

private[tvm] object RunTvm {
  implicit val RunTvmArgsEncoder: Encoder[RunTvm] = deriveCodec[RunTvm]
}
