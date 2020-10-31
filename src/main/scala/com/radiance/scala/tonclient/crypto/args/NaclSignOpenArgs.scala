package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class NaclSignOpenArgs(signed: String, public: String) extends Args {
  override val functionName: String = "crypto.nacl_sign_open"
  override val fieldName: Option[String] = Some("unsigned")
}

private[crypto] object NaclSignOpenArgs {
  implicit val NaclSignOpenArgsEncoder: Encoder[NaclSignOpenArgs] = deriveCodec[NaclSignOpenArgs]
}
