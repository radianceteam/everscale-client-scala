package com.radiance.scala.tonclient.abi.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[abi] case class EncodeAccountArgs(
                                           state_init: String,
                                           balance: Long,
                                           last_trans_lt: Long,
                                           last_paid: Double
                                         ) extends Args {
  override val functionName: String = "abi.encode_account"
}

private [abi] object EncodeAccountArgs {
  implicit val encodeAccountArgsEncoder: Encoder[EncodeAccountArgs] = deriveCodec[EncodeAccountArgs]
}
