package com.radiance.scala.tonclient.types

import com.radiance.tonclient.{FunctionHeader, MessageBodyType}
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
  implicit val decodedMessageBodyCodec: Codec[DecodedMessageBody] = deriveCodec[DecodedMessageBody]
}