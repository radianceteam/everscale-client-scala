package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class TonCrc16Args(data: String) extends Args {
  override val functionName: String = "crypto.ton_crc16"
  override val fieldName: Option[String] = Some("crc")
}

private[crypto] object TonCrc16Args {
  implicit val tonCrc16ArgsEncoder: Encoder[TonCrc16Args] = deriveEncoder[TonCrc16Args]
}


