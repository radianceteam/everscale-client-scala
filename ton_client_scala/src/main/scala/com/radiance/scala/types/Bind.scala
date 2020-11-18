package com.radiance.scala.types

import io.circe.Decoder

trait Bind {
  type Out
  val decoder: Decoder[Out]
}
