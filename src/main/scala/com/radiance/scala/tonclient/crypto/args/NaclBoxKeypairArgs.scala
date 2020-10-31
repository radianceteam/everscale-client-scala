package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclBoxKeypairArgs() extends Args {
  override val functionName: String = "crypto.nacl_box_keypair"
}

private[crypto] object NaclBoxKeypairArgs {
  implicit val EmptyArgsEncoder: Encoder[NaclBoxKeypairArgs] = deriveCodec[NaclBoxKeypairArgs]
}
