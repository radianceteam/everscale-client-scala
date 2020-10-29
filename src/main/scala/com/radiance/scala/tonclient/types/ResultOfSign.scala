package com.radiance.scala.tonclient.types

import io.circe._
import io.circe.derivation._

case class ResultOfSign(
                         /**
                          * Signed data combined with signature encoded in `base64`.
                          */
                         signed: String,

                         /**
                          * Signature encoded in `hex`.
                          */
                         signature: String
                       )

object ResultOfSign {
  implicit val ResultOfSignCodec: Codec[ResultOfSign] = deriveCodec[ResultOfSign]
}
