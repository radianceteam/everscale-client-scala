package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.both.KeyPair
import io.circe._
import io.circe.derivation._

private[crypto] case class SignArgs(unsigned: String, keys: KeyPair) extends Args {
  override val functionName: String = "crypto.sign"
}

private[crypto] object SignArgs {
  implicit val signArgsEncoder: Encoder[SignArgs] = deriveEncoder[SignArgs]
}