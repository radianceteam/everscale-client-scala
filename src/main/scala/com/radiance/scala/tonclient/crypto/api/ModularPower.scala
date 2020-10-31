package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class ModularPower(
                                             base: String,
                                             exponent: String,
                                             modulus: String
                                           ) extends Args {
  override val functionName: String = "crypto.modular_power"
  override val fieldName: Option[String] = Some("modular_power")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object ModularPower {
  implicit val modularPowerArgsEncoder: Encoder[ModularPower] = deriveEncoder[ModularPower]
}
