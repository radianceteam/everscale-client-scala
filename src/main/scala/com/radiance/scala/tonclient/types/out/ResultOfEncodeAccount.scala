package com.radiance.scala.tonclient.types.out

import io.circe._
import io.circe.derivation._

case class ResultOfEncodeAccount(
                                  /**
                                   * Account BOC encoded in `base64`.
                                   */
                                  account: String,
                                  /**
                                   * Account ID  encoded in `hex`.
                                   */
                                  id: String
                                )

object ResultOfEncodeAccount {
  implicit val resultOfEncodeAccountDecoder: Decoder[ResultOfEncodeAccount] = deriveDecoder[ResultOfEncodeAccount]
}
