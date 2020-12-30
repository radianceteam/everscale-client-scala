package com.radiance.jvm

import com.radiance.jvm.client.ClientError
import io.circe.{Decoder, DecodingFailure, HCursor}

case class OperationResponse[T : Decoder](value: Either[ClientError, T])

object OperationResponse {
  implicit def OperationResponseDecoder[T: Decoder]: Decoder[OperationResponse[T]] = (c: HCursor) => {
    c.downField("result").success.map(_.as[T].map(r => OperationResponse(Right(r))))
      .orElse(c.downField("error").success.map(u => u.as[ClientError].map(u => OperationResponse[T](Left(u)))))
      .getOrElse(Left(DecodingFailure("""Can't find nor "result" neither "error" fields""", Nil)))
  }
}
