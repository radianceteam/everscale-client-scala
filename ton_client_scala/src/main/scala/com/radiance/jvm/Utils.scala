package com.radiance.jvm

import io.circe
import io.circe.Encoder
import io.circe.Json.{fromFields, fromString}

object Utils {
  def generateType[T: Encoder](a: T): circe.Json = {
    fromFields(Seq("type" -> fromString(a.getClass.getName.split("\\$").last)))
  }
}
