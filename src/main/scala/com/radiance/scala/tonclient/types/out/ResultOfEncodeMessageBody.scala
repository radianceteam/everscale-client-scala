package com.radiance.scala.tonclient.types.out

import io.circe._
import io.circe.derivation._

case class ResultOfEncodeMessageBody(
                                      /**
                                       * Message body BOC encoded with `base64`.
                                       */
                                      body: String,
                                      /**
                                       * Optional data to sign. Encoded with `base64`.<p> Presents when `message` is unsigned. Can be used for external message signing. Is this case you need to sing this data and produce signed message using `abi.attach_signature`.
                                       */
                                      data_to_sign: String
                                    )

object ResultOfEncodeMessageBody {
  implicit val resultOfEncodeMessageBodyCodec: Decoder[ResultOfEncodeMessageBody] = deriveDecoder[ResultOfEncodeMessageBody]
}
