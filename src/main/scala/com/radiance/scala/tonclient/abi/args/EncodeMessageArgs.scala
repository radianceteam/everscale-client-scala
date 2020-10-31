package com.radiance.scala.tonclient.abi.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[abi] case class EncodeMessageArgs(
                                           abi: String,
                                           address: String,
                                           deploy_set: String,
                                           call_set: String,
                                           signer: String,
                                           processing_try_index: Long
                                         ) extends Args {
  override val functionName: String = "abi.encode_message"
}

private [abi] object EncodeMessageArgs {
  implicit val encodeMessageArgsEncoder: Encoder[EncodeMessageArgs] = deriveCodec[EncodeMessageArgs]
}