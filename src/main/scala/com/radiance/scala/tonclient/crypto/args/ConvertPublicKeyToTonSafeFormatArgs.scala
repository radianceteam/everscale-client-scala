package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class ConvertPublicKeyToTonSafeFormatArgs(public_key: String) extends Args {
  override val functionName: String = "crypto.convert_public_key_to_ton_safe_format"
  override val fieldName: Option[String] = Some("ton_public_key")
}

private[crypto] object ConvertPublicKeyToTonSafeFormatArgs {
  implicit val convertPublicKeyToTonSafeFormatArgsEncoder: Encoder[ConvertPublicKeyToTonSafeFormatArgs] =
    deriveEncoder[ConvertPublicKeyToTonSafeFormatArgs]
}
