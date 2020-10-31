package com.radiance.scala.tonclient.types.unused

import io.circe._
import io.circe.derivation._

case class StateInitParams(
                            abi: String,
                            value: String
                          )

object StateInitParams {
  implicit val stateInitParamsCodec: Codec[StateInitParams] = deriveCodec[StateInitParams]
}