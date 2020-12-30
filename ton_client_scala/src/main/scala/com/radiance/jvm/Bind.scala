package com.radiance.jvm

import io.circe.Decoder

trait Bind {
  type Out
  val decoder: Decoder[Out]
}
