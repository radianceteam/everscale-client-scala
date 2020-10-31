package com.radiance.scala.tonclient.tvm.args

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.both.ExecutionOptions
import io.circe._
import io.circe.derivation._

private[tvm] case class RunGetArgs(
                                    account: String,
                                    function_name: String,
                                    input: String,
                                    execution_options: ExecutionOptions
                                  ) extends Args {
  override val functionName: String = "tvm.run_get"
  override val fieldName: Option[String] = Some("output")
}

private[tvm] object RunGetArgs {
  implicit val runGetArgsEncoder: Encoder[RunGetArgs] = deriveCodec[RunGetArgs]
}
