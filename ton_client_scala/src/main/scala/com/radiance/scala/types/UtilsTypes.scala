package com.radiance.scala.types

import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import io.circe.Json._

object UtilsTypes {

  sealed trait AddressStringFormat

  case object AccountId extends AddressStringFormat

  case object Hex extends AddressStringFormat

  case class Base64(url: Boolean, test: Boolean, bounce: Boolean) extends AddressStringFormat

  case class ParamsOfConvertAddress(address: String, output_format: AddressStringFormat) extends Bind {
    override type Out = ResultOfConvertAddress
    override val decoder: Decoder[ResultOfConvertAddress] = implicitly[Decoder[ResultOfConvertAddress]]
  }

  case class ResultOfConvertAddress(address: String)

  object AddressStringFormat {
    implicit val AddressStringFormatEncoder: Encoder[AddressStringFormat] = {
      case AccountId => fromFields(Seq("type" -> fromString("AccountId")))
      case Hex => fromFields(Seq("type" -> fromString("Hex")))
      case a: Base64 => a.asJson.deepMerge(Utils.generateType(a))
    }
  }

  object Base64 {
    implicit val Base64Encoder: Encoder[Base64] = deriveEncoder[Base64]
  }

  object ParamsOfConvertAddress {
    implicit val ParamsOfConvertAddressEncoder: Encoder[ParamsOfConvertAddress] = deriveEncoder[ParamsOfConvertAddress]
  }

  object ResultOfConvertAddress {
    implicit val ResultOfConvertAddressDecoder: Decoder[ResultOfConvertAddress] = deriveDecoder[ResultOfConvertAddress]
  }

}
