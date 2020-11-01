package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[crypto] case class Factorize(composite: String) extends Api {
  override val functionName: String = "crypto.factorize"
  override val fieldName: Option[String] = Some("factors")
  override type Out = List[String]
  override val decoder: Decoder[List[String]] = implicitly[Decoder[List[String]]]
}

private[crypto] object Factorize {
  implicit val factorizeArgsEncoder: Encoder[Factorize] = deriveEncoder[Factorize]
}