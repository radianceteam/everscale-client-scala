package com.radiance.scala.tonclient.abi.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[abi] case class EncodeMessageBodyArgs(
                                               abi: String,
                                               call_set: String,
                                               is_internal: Boolean,
                                               signer: String,
                                               processing_try_index: Long
                                             ) extends Args {
  override val functionName: String = "abi.encode_message_body"
}

private [abi] object EncodeMessageBodyArgs {
  implicit val encodeMessageBodyArgsEncoder: Encoder[EncodeMessageBodyArgs] = deriveEncoder[EncodeMessageBodyArgs]
}
