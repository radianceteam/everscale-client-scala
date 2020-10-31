package com.radiance.scala.tonclient.types.out

import io.circe._
import io.circe.derivation._

case class ResultOfAttachSignature(
                                    /**
                                     * Signed message BOC
                                     */
                                    message: String,

                                    /**
                                     * Message ID
                                     */
                                    message_id: String
                                  )

object ResultOfAttachSignature {
  implicit val resultOfAttachSignatureCodec: Decoder[ResultOfAttachSignature] = deriveDecoder[ResultOfAttachSignature]
}
