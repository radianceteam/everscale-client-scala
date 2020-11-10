package com.radiance.scala.tonclient

import io.circe.Decoder

private[tonclient] trait Api {
  type Out
  val decoder: Decoder[Out]
  val functionName: String
  val fieldName: Option[String] = None
}
