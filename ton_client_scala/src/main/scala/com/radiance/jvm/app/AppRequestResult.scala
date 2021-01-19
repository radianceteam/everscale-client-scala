package com.radiance.jvm.app

import com.radiance.jvm.Utils.generateType
import com.radiance.jvm.Value
import io.circe.Encoder
import io.circe.derivation.deriveEncoder

sealed trait AppRequestResult

object AppRequestResult {
  import io.circe.syntax._
  case class Error(text: String) extends AppRequestResult
  case class Ok(result: Value) extends AppRequestResult

  implicit val encoder: Encoder[AppRequestResult] = {
    case a: Error => a.asJson.deepMerge(generateType(a))
    case a: Ok    => a.asJson.deepMerge(generateType(a))
  }

  object Error {
    implicit val encoder: Encoder[Error] = deriveEncoder[Error]
  }

  object Ok {
    implicit val encoder: Encoder[Ok] = deriveEncoder[Ok]
  }
}
