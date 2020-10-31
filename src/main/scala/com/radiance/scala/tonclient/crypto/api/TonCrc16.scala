package com.radiance.scala.tonclient.crypto.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class TonCrc16(data: String) extends Args {
  override val functionName: String = "crypto.ton_crc16"
  override val fieldName: Option[String] = Some("crc")
  override type Out = Long
  override val decoder: Decoder[Long] = implicitly[Decoder[Long]]
}

private[crypto] object TonCrc16 {
  implicit val tonCrc16ArgsEncoder: Encoder[TonCrc16] = deriveEncoder[TonCrc16]
}


