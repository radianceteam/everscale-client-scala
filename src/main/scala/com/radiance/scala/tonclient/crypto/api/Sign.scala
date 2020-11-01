package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.both.KeyPair
import com.radiance.scala.tonclient.types.out.ResultOfSign
import io.circe._
import io.circe.derivation._

private[crypto] case class Sign(unsigned: String, keys: KeyPair) extends Api {
  override val functionName: String = "crypto.sign"
  override type Out = ResultOfSign
  override val decoder: Decoder[ResultOfSign] = implicitly[Decoder[ResultOfSign]]
}

private[crypto] object Sign {
  implicit val signArgsEncoder: Encoder[Sign] = deriveEncoder[Sign]
}