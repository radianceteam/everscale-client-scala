package com.radiance.scala.methods

import com.radiance.scala.types.AbiTypes.{AbiContract, Serialized}
import io.circe.Decoder
import io.circe.derivation.deriveDecoder

object TestDecoders {

  implicit val AbiContractDecoder: Decoder[AbiContract] = deriveDecoder[AbiContract]

}
