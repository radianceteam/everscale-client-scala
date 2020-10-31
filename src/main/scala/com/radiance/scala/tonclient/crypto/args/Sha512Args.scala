package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class Sha512Args(data: String) extends Args {
  override val functionName: String = "crypto.sha512"
  override val fieldName: Option[String] = Some("hash")
}

private[crypto] object Sha512Args {
  implicit val DataArgsEncoder: Encoder[Sha512Args] = deriveEncoder[Sha512Args]
}
