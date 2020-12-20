package com.radiance.jvm

import com.radiance.jvm.abi._
import io.circe.Decoder
import io.circe.derivation.deriveDecoder

object TestDecoders {

  implicit val decoder: Decoder[AbiContract] = deriveDecoder[AbiContract]

}
