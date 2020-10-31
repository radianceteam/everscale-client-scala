package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSignDetachedArgs(unsigned: String, secret: String) extends Args {
  override val functionName: String = "crypto.nacl_sign_detached"
  override val fieldName: Option[String] = Some("signature")
}

private[crypto] object NaclSignDetachedArgs {
  implicit val naclSignDetachedArgsEncoder: Encoder[NaclSignDetachedArgs] = deriveCodec[NaclSignDetachedArgs]
}