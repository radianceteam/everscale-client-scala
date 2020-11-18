package com.radiance.scala.types

import io.circe
import io.circe.Encoder
import io.circe.Json._

object Utils {
  def generateType[T : Encoder](a: T): circe.Json = fromFields(Seq(
    "type" -> fromString(a.getClass.getSimpleName)
  ))
}

