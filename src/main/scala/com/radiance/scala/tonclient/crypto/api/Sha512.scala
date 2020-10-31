package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class Sha512(data: String) extends Args {
  override val functionName: String = "crypto.sha512"
  override val fieldName: Option[String] = Some("hash")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object Sha512 {
  implicit val DataArgsEncoder: Encoder[Sha512] = deriveEncoder[Sha512]
}
