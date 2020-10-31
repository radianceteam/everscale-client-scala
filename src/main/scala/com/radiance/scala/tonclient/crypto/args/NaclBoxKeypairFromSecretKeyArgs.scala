package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe.Encoder
import io.circe.derivation.deriveCodec

private[crypto] case class NaclBoxKeypairFromSecretKeyArgs(secret: String) extends Args {
  override val functionName: String = "crypto.nacl_box_keypair_from_secret_key"
}

private[crypto] object NaclBoxKeypairFromSecretKeyArgs {
  implicit val NaclBoxKeypairFromSecretKeyArgsEncoder: Encoder[NaclBoxKeypairFromSecretKeyArgs] =
    deriveCodec[NaclBoxKeypairFromSecretKeyArgs]
}
