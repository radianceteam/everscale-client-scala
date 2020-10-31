package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSignKeypairFromSecretKeyArgs(secret: String) extends Args {
  override val functionName: String = "crypto.nacl_sign_keypair_from_secret_key"
}

private[crypto] object NaclSignKeypairFromSecretKeyArgs {
  implicit val naclSignKeypairFromSecretKeyArgsEncoder: Encoder[NaclSignKeypairFromSecretKeyArgs] =
    deriveCodec[NaclSignKeypairFromSecretKeyArgs]
}
