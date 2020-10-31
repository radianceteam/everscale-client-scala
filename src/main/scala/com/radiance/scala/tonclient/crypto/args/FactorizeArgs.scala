package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class FactorizeArgs(composite: String) extends Args {
  override val functionName: String = "crypto.factorize"
  override val fieldName: Option[String] = Some("factors")
}

private[crypto] object FactorizeArgs {
  implicit val factorizeArgsEncoder: Encoder[FactorizeArgs] = deriveEncoder[FactorizeArgs]
}