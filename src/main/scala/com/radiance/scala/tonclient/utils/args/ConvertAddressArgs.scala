package com.radiance.scala.tonclient.utils.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private [utils] case class ConvertAddressArgs(address: String, output_format: String) extends Args {
  override val functionName: String = "utils.convert_address"
  override val fieldName: Option[String] = Some("address")
}

private [utils] object ConvertAddressArgs {
  implicit val convertAddressArgsEncoder: Encoder[ConvertAddressArgs] = deriveCodec[ConvertAddressArgs]
}