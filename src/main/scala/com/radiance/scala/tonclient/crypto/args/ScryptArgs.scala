package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class ScryptArgs(
                                       password: String,
                                       salt: String,
                                       log_n: Long,
                                       r: Double,
                                       p: Long,
                                       dk_len: Long
                                     ) extends Args {
  override val functionName: String = "crypto.scrypt"
  override val fieldName: Option[String] = Some("key")
}

private[crypto] object ScryptArgs {
  implicit val scryptArgsEncoder: Encoder[ScryptArgs] = deriveCodec[ScryptArgs]
}
