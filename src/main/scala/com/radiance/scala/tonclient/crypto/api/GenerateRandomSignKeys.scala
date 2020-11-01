package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.both.KeyPair
import io.circe._
import io.circe.derivation._

private[crypto] case class GenerateRandomSignKeys() extends Api {
  override val functionName: String = "crypto.generate_random_sign_keys"
  override type Out = KeyPair
  override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
}

private[crypto] object GenerateRandomSignKeys {
  implicit val EmptyArgsEncoder: Encoder[GenerateRandomSignKeys] = deriveEncoder[GenerateRandomSignKeys]
}


