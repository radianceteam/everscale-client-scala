package com.radiance.scala.tonclient.utils.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private [utils] case class ConvertAddress(address: String, output_format: String) extends Args {
  override val functionName: String = "utils.convert_address"
  override val fieldName: Option[String] = Some("address")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private [utils] object ConvertAddress {
  implicit val convertAddressArgsEncoder: Encoder[ConvertAddress] = deriveCodec[ConvertAddress]
}