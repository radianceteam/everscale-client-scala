package com.radiance.scala.tonclient.abi.api

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.out.ResultOfEncodeAccount
import io.circe._
import io.circe.derivation._

private[abi] case class EncodeAccount(
                                           state_init: String,
                                           balance: Long,
                                           last_trans_lt: Long,
                                           last_paid: Double
                                         ) extends Args {
  override val functionName: String = "abi.encode_account"
  override type Out = ResultOfEncodeAccount
  override val decoder: Decoder[ResultOfEncodeAccount] = implicitly[Decoder[ResultOfEncodeAccount]]
}

private [abi] object EncodeAccount {
  implicit val encodeAccountArgsEncoder: Encoder[EncodeAccount] = deriveCodec[EncodeAccount]
}
