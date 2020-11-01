package com.radiance.scala.tonclient.boc.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private [boc] case class ParseTransaction(boc: String) extends Api {
  override val functionName: String = "boc.parse_transaction"
  override val fieldName: Option[String] = Some("parsed")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private [boc] object ParseTransaction {
  implicit val parseTransactionArgsEncoder: Encoder[ParseTransaction] = deriveCodec[ParseTransaction]
}


