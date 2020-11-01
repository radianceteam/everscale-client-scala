package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[crypto] case class Sha256(data: String) extends Api {
  override val functionName: String = "crypto.sha256"
  override val fieldName: Option[String] = Some("hash")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object Sha256 {
  implicit val DataArgsEncoder: Encoder[Sha256] = deriveEncoder[Sha256]
}
