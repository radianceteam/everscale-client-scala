package com.radiance.scala.tonclient.types

import io.circe._
import io.circe.derivation._

case class ResultOfRunTvm(
                           out_messages: String,
                           decoded: DecodedOutput,
                           account: String
                         )

object ResultOfRunTvm {
  implicit val ResultOfRunTvmCodec: Codec[ResultOfRunTvm] = deriveCodec[ResultOfRunTvm]
}
