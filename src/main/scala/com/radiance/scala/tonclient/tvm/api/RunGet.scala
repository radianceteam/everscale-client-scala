package com.radiance.scala.tonclient.tvm.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.both.ExecutionOptions
import io.circe._
import io.circe.derivation._

private[tvm] case class RunGet(
                                    account: String,
                                    function_name: String,
                                    input: String,
                                    execution_options: ExecutionOptions
                                  ) extends Api {
  override val functionName: String = "tvm.run_get"
  override val fieldName: Option[String] = Some("output")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[tvm] object RunGet {
  implicit val runGetArgsEncoder: Encoder[RunGet] = deriveCodec[RunGet]
}
