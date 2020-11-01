package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private[crypto] case class ConvertPublicKeyToTonSafeFormat(public_key: String) extends Api {
  override val functionName: String = "crypto.convert_public_key_to_ton_safe_format"
  override val fieldName: Option[String] = Some("ton_public_key")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[crypto] object ConvertPublicKeyToTonSafeFormat {
  implicit val convertPublicKeyToTonSafeFormatArgsEncoder: Encoder[ConvertPublicKeyToTonSafeFormat] =
    deriveEncoder[ConvertPublicKeyToTonSafeFormat]
}
