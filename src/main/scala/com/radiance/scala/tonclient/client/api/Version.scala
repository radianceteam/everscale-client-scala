package com.radiance.scala.tonclient.client.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[client] case class Version() extends Api {
  override val functionName: String = "client.version"
  override val fieldName: Option[String] = Some("version")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[client] object Version {
  implicit val versionArgsEncoder: Encoder[Version] = deriveEncoder[Version]
}
