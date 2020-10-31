package com.radiance.scala.tonclient.abi.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[abi] case class DecodeMessageArgs(
                                           abi: String,
                                           message: String
                                         ) extends Args {
  override val functionName: String = "abi.decode_message"
}

private[abi] object DecodeMessageArgs {
  implicit val decodeMessageArgsEncoder: Encoder[DecodeMessageArgs] = deriveCodec[DecodeMessageArgs]
}
