package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class ModularPowerArgs(
                                             base: String,
                                             exponent: String,
                                             modulus: String
                                           ) extends Args {
  override val functionName: String = "crypto.modular_power"
  override val fieldName: Option[String] = Some("modular_power")
}

private[crypto] object ModularPowerArgs {
  implicit val modularPowerArgsEncoder: Encoder[ModularPowerArgs] = deriveEncoder[ModularPowerArgs]
}
