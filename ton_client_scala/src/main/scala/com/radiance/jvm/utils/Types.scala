package com.radiance.jvm.utils

import io.circe._
import io.circe.derivation._
import io.circe.generic.extras

object AddressStringFormatADT {

  sealed trait AddressStringFormat

  case object AccountId extends AddressStringFormat

  case object Hex extends AddressStringFormat

  case class Base64(url: Boolean, test: Boolean, bounce: Boolean) extends AddressStringFormat

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[AddressStringFormat] =
    extras.semiauto.deriveConfiguredEncoder[AddressStringFormat]
}

case class ParamsOfCalcStorageFee(account: String, period: Long)

object ParamsOfCalcStorageFee {
  implicit val encoder: Encoder[ParamsOfCalcStorageFee] =
    deriveEncoder[ParamsOfCalcStorageFee]
}

case class ParamsOfConvertAddress(
  address: String,
  output_format: AddressStringFormatADT.AddressStringFormat
)

object ParamsOfConvertAddress {
  implicit val encoder: Encoder[ParamsOfConvertAddress] =
    deriveEncoder[ParamsOfConvertAddress]
}

case class ResultOfCalcStorageFee(fee: String)

object ResultOfCalcStorageFee {
  implicit val decoder: Decoder[ResultOfCalcStorageFee] =
    deriveDecoder[ResultOfCalcStorageFee]
}

case class ResultOfConvertAddress(address: String)

object ResultOfConvertAddress {
  implicit val decoder: Decoder[ResultOfConvertAddress] =
    deriveDecoder[ResultOfConvertAddress]
}
