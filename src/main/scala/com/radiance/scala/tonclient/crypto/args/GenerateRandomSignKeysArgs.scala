package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class GenerateRandomSignKeysArgs() extends Args {
  override val functionName: String = "crypto.generate_random_sign_keys"
}

private[crypto] object GenerateRandomSignKeysArgs {
  implicit val EmptyArgsEncoder: Encoder[GenerateRandomSignKeysArgs] = deriveEncoder[GenerateRandomSignKeysArgs]
}


