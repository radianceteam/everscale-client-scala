package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSignArgs(unsigned: String, secret: String) extends Args {
  override val functionName: String = "crypto.nacl_sign"
  override val fieldName: Option[String] = Some("signed")
}

private[crypto] object NaclSignArgs {
  implicit val naclSignArgsEncoder: Encoder[NaclSignArgs] = deriveEncoder[NaclSignArgs]
}