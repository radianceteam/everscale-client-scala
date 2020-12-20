package com.radiance.jvm.utils

import com.radiance.jvm._
import com.radiance.jvm.Utils

import io.circe._
import io.circe.derivation._

sealed trait AddressStringFormat

object AddressStringFormat {

  import io.circe.Json._
  import io.circe.syntax._

  case object AccountId extends AddressStringFormat

  case object Hex extends AddressStringFormat

  case class Base64(url: Boolean, test: Boolean, bounce: Boolean)
      extends AddressStringFormat

  object Base64 {
    implicit val encoder: Encoder[Base64] = deriveEncoder[Base64]
  }

  implicit val encoder: Encoder[AddressStringFormat] = {
    case AccountId => fromFields(Seq("type" -> fromString("AccountId")))
    case Hex       => fromFields(Seq("type" -> fromString("Hex")))
    case a: Base64 => a.asJson.deepMerge(Utils.generateType(a))
  }
}

case class ParamsOfConvertAddress(
    address: String,
    output_format: AddressStringFormat
) extends Bind {
  override type Out = ResultOfConvertAddress
  override val decoder: Decoder[ResultOfConvertAddress] =
    implicitly[Decoder[ResultOfConvertAddress]]
}

object ParamsOfConvertAddress {
  implicit val encoder: Encoder[ParamsOfConvertAddress] =
    deriveEncoder[ParamsOfConvertAddress]
}

case class ResultOfConvertAddress(address: String)

object ResultOfConvertAddress {
  implicit val decoder: Decoder[ResultOfConvertAddress] =
    deriveDecoder[ResultOfConvertAddress]
}
