package com.radiance.scala.tonclient.client.api

import com.radiance.scala.tonclient.Args
import io.circe.{Decoder, Encoder}
import io.circe.derivation.deriveEncoder

private[client] case class BuildInfo() extends Args {
  override val functionName: String = "client.build_info"
  override val fieldName: Option[String] = Some("build_info")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[client] object BuildInfo {
  implicit val getApiReferenceArgsEncoder: Encoder[BuildInfo] = deriveEncoder[BuildInfo]
}
