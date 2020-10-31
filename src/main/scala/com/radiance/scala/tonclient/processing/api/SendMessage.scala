package com.radiance.scala.tonclient.processing.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[processing] case class SendMessage(
                                                message: String,
                                                abi: String,
                                                send_events: Boolean
                                              ) extends Args {
  override val functionName: String = "processing.send_message"
  override val fieldName: Option[String] = Some("shard_block_id")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[processing] object SendMessage {
  implicit val SendMessageArgsEncoders: Encoder[SendMessage] = deriveEncoder[SendMessage]
}
