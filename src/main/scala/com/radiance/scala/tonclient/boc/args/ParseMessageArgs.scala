package com.radiance.scala.tonclient.boc.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._


private [boc] case class ParseMessageArgs(boc: String) extends Args {
  override val functionName: String = "boc.parse_message"
  override val fieldName: Option[String] = Some("parsed")
}

private [boc] object ParseMessageArgs {
  implicit val parseMessageEncoder: Encoder[ParseMessageArgs] = deriveEncoder[ParseMessageArgs]
}


