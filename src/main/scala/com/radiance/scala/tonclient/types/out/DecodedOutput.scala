package com.radiance.scala.tonclient.types.out

import io.circe._
import io.circe.derivation._

case class DecodedOutput(
                          /**
                           * Decoded bodies of the out messages.<p> If the message can't be decoded then `None`
                           * will be stored in the appropriate position.
                           */
                          out_messages: DecodedMessageBody,

                          /**
                           * Decoded body of the function output message.
                           */
                          output: String
                        )

object DecodedOutput {
  implicit val decodedOutputDecoder: Decoder[DecodedOutput] = deriveDecoder[DecodedOutput]
}
