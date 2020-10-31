package com.radiance.scala.tonclient.client.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[client] case class VersionArgs() extends Args {
  override val functionName: String = "client.version"
  override val fieldName: Option[String] = Some("version")
}

private[client] object VersionArgs {
  implicit val versionArgsEncoder: Encoder[VersionArgs] = deriveEncoder[VersionArgs]
}
