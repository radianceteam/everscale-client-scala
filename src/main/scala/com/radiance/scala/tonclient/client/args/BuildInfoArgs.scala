package com.radiance.scala.tonclient.client.args

import com.radiance.scala.tonclient.Args
import io.circe.Encoder
import io.circe.derivation.deriveEncoder

private[client] case class BuildInfoArgs() extends Args {
  override val functionName: String = "client.build_info"
  override val fieldName: Option[String] = Some("build_info")
}

private[client] object BuildInfoArgs {
  implicit val getApiReferenceArgsEncoder: Encoder[BuildInfoArgs] = deriveEncoder[BuildInfoArgs]
}
