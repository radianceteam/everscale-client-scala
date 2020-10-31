package com.radiance.scala.tonclient.types

import io.circe.Decoder.Result
import io.circe._


sealed trait MessageBodyType

case object Input extends MessageBodyType
case object Output extends MessageBodyType
case object InternalOutput extends MessageBodyType
case object Event extends MessageBodyType

object MessageBodyType {
  implicit val messageBodyTypeDecoder: Decoder[MessageBodyType] = (c: HCursor) => c.focus.flatMap(_.asString).map {
    case "input" => Right(Input)
    case "output" => Right(Output)
    case "internal_output" => Right(InternalOutput)
    case "event" => Right(Event)
    case x => Left(DecodingFailure(s"Illegal argument $x", Nil))
  }.getOrElse(Left(DecodingFailure(s"Can't read as string", Nil)))
}



