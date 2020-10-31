package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class Sha256Args(data: String) extends Args {
  override val functionName: String = "crypto.sha256"
  override val fieldName: Option[String] = Some("hash")
}

private[crypto] object Sha256Args {
  implicit val DataArgsEncoder: Encoder[Sha256Args] = deriveEncoder[Sha256Args]
}
