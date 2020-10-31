package com.radiance.scala.tonclient.boc.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._


private [boc] case class ParseBlockArgs(boc: String) extends Args {
  override val functionName: String = "boc.parse_block"
  override val fieldName: Option[String] = Some("parsed")
}

private [boc] object ParseBlockArgs {
  implicit val parseBlockArgsEncoder: Encoder[ParseBlockArgs] = deriveEncoder[ParseBlockArgs]
}