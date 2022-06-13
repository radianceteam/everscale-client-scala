package com.radiance.jvm.utils

import io.circe._
import io.circe.derivation._
import io.circe.generic.extras

object AccountAddressTypeEnum {
  sealed trait AccountAddressType

  case object AccountId extends AccountAddressType

  case object Base64 extends AccountAddressType

  case object Hex extends AccountAddressType

  implicit val decoder: Decoder[AccountAddressType] =
    extras.semiauto.deriveEnumerationDecoder[AccountAddressType]

}

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

case class ParamsOfCompressZstd(uncompressed: String, level: Option[Int])

object ParamsOfCompressZstd {
  implicit val encoder: Encoder[ParamsOfCompressZstd] =
    deriveEncoder[ParamsOfCompressZstd]
}

case class ParamsOfConvertAddress(
  address: String,
  output_format: AddressStringFormatADT.AddressStringFormat
)

object ParamsOfConvertAddress {
  implicit val encoder: Encoder[ParamsOfConvertAddress] =
    deriveEncoder[ParamsOfConvertAddress]
}

case class ParamsOfDecompressZstd(compressed: String)

object ParamsOfDecompressZstd {
  implicit val encoder: Encoder[ParamsOfDecompressZstd] =
    deriveEncoder[ParamsOfDecompressZstd]
}

case class ParamsOfGetAddressType(address: String)

object ParamsOfGetAddressType {
  implicit val encoder: Encoder[ParamsOfGetAddressType] =
    deriveEncoder[ParamsOfGetAddressType]
}

case class ResultOfCalcStorageFee(fee: String)

object ResultOfCalcStorageFee {
  implicit val decoder: Decoder[ResultOfCalcStorageFee] =
    deriveDecoder[ResultOfCalcStorageFee]
}

case class ResultOfCompressZstd(compressed: String)

object ResultOfCompressZstd {
  implicit val decoder: Decoder[ResultOfCompressZstd] =
    deriveDecoder[ResultOfCompressZstd]
}

case class ResultOfConvertAddress(address: String)

object ResultOfConvertAddress {
  implicit val decoder: Decoder[ResultOfConvertAddress] =
    deriveDecoder[ResultOfConvertAddress]
}

case class ResultOfDecompressZstd(decompressed: String)

object ResultOfDecompressZstd {
  implicit val decoder: Decoder[ResultOfDecompressZstd] =
    deriveDecoder[ResultOfDecompressZstd]
}

case class ResultOfGetAddressType(address_type: AccountAddressTypeEnum.AccountAddressType)

object ResultOfGetAddressType {
  implicit val decoder: Decoder[ResultOfGetAddressType] =
    deriveDecoder[ResultOfGetAddressType]
}
