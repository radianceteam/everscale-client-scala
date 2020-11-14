package com.radiance.scala.types

import io.circe
import io.circe.Encoder

object Utils {
  def generateType[T : Encoder](a: T): circe.Json = circe.Json.fromFields(Seq(
    "type" -> circe.Json.fromString(a.getClass.getSimpleName)
  ))
}
