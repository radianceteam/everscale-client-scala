package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class Scrypt(
                                       password: String,
                                       salt: String,
                                       log_n: Long,
                                       r: Double,
                                       p: Long,
                                       dk_len: Long
                                     ) extends Args {
  override val functionName: String = "crypto.scrypt"
  override val fieldName: Option[String] = Some("key")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object Scrypt {
  implicit val scryptArgsEncoder: Encoder[Scrypt] = deriveCodec[Scrypt]
}
