package com.radiance.scala.tonclient

private[tonclient] trait Args {
  val functionName: String
  val fieldName: Option[String] = None
}
