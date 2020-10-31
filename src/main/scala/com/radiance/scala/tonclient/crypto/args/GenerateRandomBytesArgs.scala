package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class GenerateRandomBytesArgs(length: Long) extends Args {
  override val functionName: String = "crypto.generate_random_bytes"
  override val fieldName: Option[String] = Some("bytes")
}

private[crypto] object GenerateRandomBytesArgs {
  implicit val generateRandomBytesArgsEncoder: Encoder[GenerateRandomBytesArgs] = deriveEncoder[GenerateRandomBytesArgs]
}