package com.radiance.scala.tonclient.types.out

import com.radiance.scala.tonclient.types.both.FunctionHeader
import io.circe._
import io.circe.derivation._

case class DecodedMessageBody(
                               /**
                                * Type of the message body content.
                                */
                               body_type: MessageBodyType,

                               /**
                                * Function or event name.
                                */
                               name: String,

                               /**
                                * Parameters or result value.
                                */
                               value: String,

                               /**
                                * Function header.
                                */
                               header: FunctionHeader
                             )

object DecodedMessageBody {
  implicit val decodedMessageBodyDecoder: Decoder[DecodedMessageBody] = deriveDecoder[DecodedMessageBody]
}