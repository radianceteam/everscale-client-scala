package com.radiance.scala.tonclient.boc.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._


private [boc] case class ParseMessage(boc: String) extends Args {
  override val functionName: String = "boc.parse_message"
  override val fieldName: Option[String] = Some("parsed")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private [boc] object ParseMessage {
  implicit val parseMessageEncoder: Encoder[ParseMessage] = deriveEncoder[ParseMessage]
}


