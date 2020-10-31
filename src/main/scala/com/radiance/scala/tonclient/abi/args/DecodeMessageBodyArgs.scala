package com.radiance.scala.tonclient.abi.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[abi] case class DecodeMessageBodyArgs(
                                               abi: String,
                                               body: String,
                                               is_internal: Boolean
                                             ) extends Args {
  override val functionName: String = "abi.decode_message_body"
}

private[abi] object DecodeMessageBodyArgs {
  implicit val decodeMessageBodyArgsEncoder: Encoder[DecodeMessageBodyArgs] = deriveEncoder[DecodeMessageBodyArgs]
}