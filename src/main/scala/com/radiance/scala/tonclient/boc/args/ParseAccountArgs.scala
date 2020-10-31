package com.radiance.scala.tonclient.boc.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private [boc] case class ParseAccountArgs(boc: String) extends Args {
  override val functionName: String = "boc.parse_account"
  override val fieldName: Option[String] = Some("parsed")
}

private [boc] object ParseAccountArgs {
  implicit val parseAccountArgsEncoder: Encoder[ParseAccountArgs] = deriveEncoder[ParseAccountArgs]
}
