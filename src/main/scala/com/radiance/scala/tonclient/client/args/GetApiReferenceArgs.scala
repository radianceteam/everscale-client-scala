package com.radiance.scala.tonclient.client.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[client] case class GetApiReferenceArgs() extends Args {
  override val functionName: String = "client.get_api_reference"
  override val fieldName: Option[String] = Some("api")
}

private[client] object GetApiReferenceArgs {
  implicit val getApiReferenceArgsEncoder: Encoder[GetApiReferenceArgs] = deriveEncoder[GetApiReferenceArgs]
}

