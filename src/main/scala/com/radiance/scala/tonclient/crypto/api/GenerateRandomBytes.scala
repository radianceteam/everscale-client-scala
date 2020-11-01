package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[crypto] case class GenerateRandomBytes(length: Long) extends Api {
  override val functionName: String = "crypto.generate_random_bytes"
  override val fieldName: Option[String] = Some("bytes")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object GenerateRandomBytes {
  implicit val generateRandomBytesArgsEncoder: Encoder[GenerateRandomBytes] = deriveEncoder[GenerateRandomBytes]
}