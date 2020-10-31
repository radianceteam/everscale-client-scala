package com.radiance.scala.tonclient.boc.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private [boc] case class ParseTransactionArgs(boc: String) extends Args {
  override val functionName: String = "boc.parse_transaction"
  override val fieldName: Option[String] = Some("parsed")
}

private [boc] object ParseTransactionArgs {
  implicit val parseTransactionArgsEncoder: Encoder[ParseTransactionArgs] = deriveCodec[ParseTransactionArgs]
}


