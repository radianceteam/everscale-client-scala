package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.both.KeyPair
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclBoxKeypair() extends Api {
  override val functionName: String = "crypto.nacl_box_keypair"
  override type Out = KeyPair
  override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
}

private[crypto] object NaclBoxKeypair {
  implicit val EmptyArgsEncoder: Encoder[NaclBoxKeypair] = deriveCodec[NaclBoxKeypair]
}
