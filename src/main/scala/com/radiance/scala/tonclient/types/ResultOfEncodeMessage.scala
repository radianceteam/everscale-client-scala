package com.radiance.scala.tonclient.types

import io.circe._
import io.circe.derivation._

case class ResultOfEncodeMessage(
                                  /**
                                   * Message BOC encoded with `base64`.
                                   */
                                  message: String,

                                  /**
                                   * Optional data to be signed encoded in `base64`.<p> Returned in case of `Signer::External`. Can be used for external message signing. Is this case you need to use this data to create signature and then produce signed message using `abi.attach_signature`.
                                   */
                                  data_to_sign: String,
                                  /**
                                   * Destination address.
                                   */
                                  address: String,

                                  /**
                                   * Message id.
                                   */
                                  message_id: String
                                )

object ResultOfEncodeMessage {
  implicit val resultOfEncodeMessageCodec: Codec[ResultOfEncodeMessage] = deriveCodec[ResultOfEncodeMessage]
}
