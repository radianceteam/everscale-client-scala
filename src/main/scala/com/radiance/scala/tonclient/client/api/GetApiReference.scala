package com.radiance.scala.tonclient.client.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[client] case class GetApiReference() extends Api {
  override val functionName: String = "client.get_api_reference"
  override val fieldName: Option[String] = Some("api")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[client] object GetApiReference {
  implicit val getApiReferenceArgsEncoder: Encoder[GetApiReference] = deriveEncoder[GetApiReference]
}

