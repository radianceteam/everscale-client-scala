package com.radiance.scala.tonclient.processing.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[processing] case class SendMessageArgs(
                                                message: String,
                                                abi: String,
                                                send_events: Boolean
                                              ) extends Args {
  override val functionName: String = "processing.send_message"
  override val fieldName: Option[String] = Some("shard_block_id")
}

private[processing] object SendMessageArgs {
  implicit val SendMessageArgsEncoders: Encoder[SendMessageArgs] = deriveEncoder[SendMessageArgs]
}
