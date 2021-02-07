package com.radiance.jvm.app

import com.radiance.jvm.Value
import io.circe.Encoder
import io.circe.generic.extras

object AppRequestResultADT {

  sealed trait AppRequestResult

  case class Error(text: String) extends AppRequestResult

  case class Ok(result: Value) extends AppRequestResult

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[AppRequestResult] =
    extras.semiauto.deriveConfiguredEncoder[AppRequestResult]
}
