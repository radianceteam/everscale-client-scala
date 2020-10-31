package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.both.KeyPair
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSignKeypairFromSecretKey(secret: String) extends Args {
  override val functionName: String = "crypto.nacl_sign_keypair_from_secret_key"
  override type Out = KeyPair
  override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
}

private[crypto] object NaclSignKeypairFromSecretKey {
  implicit val naclSignKeypairFromSecretKeyArgsEncoder: Encoder[NaclSignKeypairFromSecretKey] =
    deriveCodec[NaclSignKeypairFromSecretKey]
}
