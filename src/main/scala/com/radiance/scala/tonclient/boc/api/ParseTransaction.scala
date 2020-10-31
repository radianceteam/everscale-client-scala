package com.radiance.scala.tonclient.boc.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private [boc] case class ParseTransaction(boc: String) extends Args {
  override val functionName: String = "boc.parse_transaction"
  override val fieldName: Option[String] = Some("parsed")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private [boc] object ParseTransaction {
  implicit val parseTransactionArgsEncoder: Encoder[ParseTransaction] = deriveCodec[ParseTransaction]
}


