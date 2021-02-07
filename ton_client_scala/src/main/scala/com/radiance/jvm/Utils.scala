package com.radiance.jvm

import io.circe
import io.circe.Encoder
import io.circe.Json._

import java.util.Base64

object Utils {

  def encode(arr: Array[Byte]): String = Base64.getEncoder.encodeToString(arr)

  def decode(str: String) = new String(Base64.getDecoder.decode(str))

  def hexEncode(arr: Array[Byte]): String = arr.map("%02X" format _).mkString
}
