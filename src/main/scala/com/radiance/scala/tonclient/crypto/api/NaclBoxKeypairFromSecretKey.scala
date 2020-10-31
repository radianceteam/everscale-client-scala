package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.both.KeyPair
import io.circe.{Decoder, Encoder}
import io.circe.derivation.deriveCodec

private[crypto] case class NaclBoxKeypairFromSecretKey(secret: String) extends Args {
  override val functionName: String = "crypto.nacl_box_keypair_from_secret_key"
  override type Out = KeyPair
  override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
}

private[crypto] object NaclBoxKeypairFromSecretKey {
  implicit val NaclBoxKeypairFromSecretKeyArgsEncoder: Encoder[NaclBoxKeypairFromSecretKey] =
    deriveCodec[NaclBoxKeypairFromSecretKey]
}
