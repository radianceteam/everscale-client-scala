package com.radiance.generator.types

import io.circe.{Decoder, DecodingFailure, HCursor}
import io.circe.derivation.deriveDecoder
import cats.implicits._


import scala.util.Try

object ApiDescription {

  case class ApiRoot(version: String, modules: List[ApiModule])

  object ApiRoot {
    implicit val decoder: Decoder[ApiRoot] = deriveDecoder[ApiRoot]
  }

  case class ApiModule(
                        name: String,
                        summary: Option[String],
                        description: Option[String],
                        types: List[ApiTypeDescription],
                        functions: List[ApiFunctionType]
  )

  object ApiModule {
    implicit val decoder: Decoder[ApiModule] = deriveDecoder[ApiModule]
  }

  case class ApiTypeDescription(
                                 name: String,
                                 summary: Option[String],
                                 description: Option[String],
                                 typ: ApiType
                               )

  object ApiTypeDescription {
    implicit val decoder: Decoder[ApiTypeDescription] = (c: HCursor) => {
      for {
      name <- Try(c.downField("name").as[String]).getOrElse("emptyName".asRight[DecodingFailure])
      summary <- c.downField("summary").as[Option[String]]
      description <- c.downField("description").as[Option[String]]
      typ <- c.as[ApiType]
      } yield {
        ApiTypeDescription(name, summary, description, typ)
      }
    }
  }

  sealed trait ApiType

  case class ApiStruct(
    struct_fields: List[ApiTypeDescription]
  ) extends ApiType

  object ApiStruct {
    implicit val decoder: Decoder[ApiStruct] = deriveDecoder[ApiStruct]
  }

  case class ApiEmptyStruct(
    value: Option[String]
  ) extends ApiType

  object ApiEmptyStruct {
    implicit val decoder: Decoder[ApiEmptyStruct] = deriveDecoder[ApiEmptyStruct]
  }

  case class ApiEnumOfTypes(
    enum_types: List[ApiTypeDescription]
  ) extends ApiType

  object ApiEnumOfTypes {
    implicit val decoder: Decoder[ApiEnumOfTypes] = deriveDecoder[ApiEnumOfTypes]
  }

  case class ApiEnumOfConsts(
    enum_consts: List[EnumConstDescription]
  ) extends ApiType

  object ApiEnumOfConsts {
    implicit val decoder: Decoder[ApiEnumOfConsts] = deriveDecoder[ApiEnumOfConsts]
  }

  case class EnumConstDescription(
   name: String,
   summary: Option[String],
   description: Option[String],
   value: Option[String]
  )

  object EnumConstDescription {
    implicit val decoder: Decoder[EnumConstDescription] = deriveDecoder[EnumConstDescription]
  }

  case class ApiNone(
    value: Option[String]
  ) extends ApiType

  object ApiNone {
    implicit val decoder: Decoder[ApiNone] = deriveDecoder[ApiNone]
  }

  case class ApiValueClassDeclForRef(
    ref_name: String
  ) extends ApiType

  object ApiValueClassDeclForRef {
    implicit val decoder: Decoder[ApiValueClassDeclForRef] =
      deriveDecoder[ApiValueClassDeclForRef]
  }

  case class ApiValueClassDeclForNumber(
    number_type: ApiNumberSubtype,
    number_size: Int
  ) extends ApiType

  object ApiValueClassDeclForNumber {
    implicit val decoder: Decoder[ApiValueClassDeclForNumber] =
      deriveDecoder[ApiValueClassDeclForNumber]
  }

  case class ApiNumberType(number_type: ApiNumberSubtype) extends ApiType

  object ApiNumberType {
    implicit val decoder: Decoder[ApiNumberType] =
      deriveDecoder[ApiNumberType]
  }

  case class ApiOptional(optional_inner: ApiType) extends ApiType

  object ApiOptional {
    implicit val decoder: Decoder[ApiOptional] =
      deriveDecoder[ApiOptional]
  }

  case class ApiArray(array_item: ApiType) extends ApiType

  object ApiArray {
    implicit val decoder: Decoder[ApiArray] =
      deriveDecoder[ApiArray]
  }

  case class ApiGeneric(
    generic_name: String,
    generic_args: List[GenericArgDescription]
  ) extends ApiType

  sealed trait GenericArgDescription

  case class GenericRefDescription(
    ref_name: Option[String]
                                   ) extends  GenericArgDescription

  case object GenericNoneDescription extends GenericArgDescription

  object GenericRefDescription {
    implicit val decoder: Decoder[GenericRefDescription] =
      deriveDecoder[GenericRefDescription]
  }

  object GenericArgDescription {
    implicit val decoder: Decoder[GenericArgDescription] = (c: HCursor) => {
      c.downField("type").as[String].flatMap {
        case "Ref" => c.as[GenericRefDescription]
        case "None" => GenericNoneDescription.asRight[DecodingFailure]
      }
    }
  }

  object ApiGeneric {
    implicit val decoder: Decoder[ApiGeneric] =
      deriveDecoder[ApiGeneric]
  }

  case object ApiString extends ApiType

  case object ApiBoolean extends ApiType

  object ApiType {
    implicit val decoder: Decoder[ApiType] = (c: HCursor) =>
      c.downField("type").as[String].flatMap {
        case "Struct" =>
          if (c.downField("struct_fields").focus.flatMap(_.asArray).fold(false)(_.nonEmpty)) {
            c.as[ApiStruct]
          } else {
            c.as[ApiEmptyStruct]
          }

        case "EnumOfTypes"  => c.as[ApiEnumOfTypes]
        case "EnumOfConsts" => c.as[ApiEnumOfConsts]
        case "None"         => c.as[ApiNone]
        case "Ref"          => c.as[ApiValueClassDeclForRef]
        case "Number"       => c.as[ApiValueClassDeclForNumber]
        case "Optional"     => c.as[ApiOptional]
        case "Array"        => c.as[ApiArray]
        case "Generic"      => c.as[ApiGeneric]
        case "BigInt"       => c.as[ApiNumberType]
        case "String"       => ApiString.asRight
        case "Boolean"      => ApiBoolean.asRight
        case x              => DecodingFailure(s"Can't decode $x as ApiRefType", Nil).asLeft
      }
  }

//  sealed trait ApiStructFieldType {
//    val name: String
//    val summary: Option[String]
//    val description: Option[String]
//  }
//
//  case class RefFieldType(
//    name: String,
//    summary: Option[String],
//    description: Option[String],
//    ref_name: String
//  ) extends ApiStructFieldType
//
//  object RefFieldType {
//    implicit val decoder: Decoder[RefFieldType] = deriveDecoder[RefFieldType]
//  }
//
//  case class NumberFieldType(
//    name: String,
//    summary: Option[String],
//    description: Option[String],
//    number_type: ApiNumberSubtype,
//    number_size: Option[Int]
//  ) extends ApiStructFieldType
//
//  object NumberFieldType {
//    implicit val decoder: Decoder[NumberFieldType] = deriveDecoder[NumberFieldType]
//  }
//
//  case class BigIntFieldType(
//    name: String,
//    summary: Option[String],
//    description: Option[String],
//    number_type: ApiNumberSubtype,
//    number_size: Int
//  ) extends ApiStructFieldType
//
//  object BigIntFieldType {
//    implicit val decoder: Decoder[BigIntFieldType] = deriveDecoder[BigIntFieldType]
//  }
//
//  case class OptionalFieldType(
//    name: String,
//    summary: Option[String],
//    description: Option[String],
//    optional_inner: ApiStructFieldType
//  ) extends ApiStructFieldType
//
//  object OptionalFieldType {
//    implicit val decoder: Decoder[OptionalFieldType] = deriveDecoder[OptionalFieldType]
//  }
//
//  case class ArrayFieldType(
//    name: String,
//    summary: Option[String],
//    description: Option[String],
//    array_item: ApiStructFieldType
//  ) extends ApiStructFieldType
//
//  object ArrayFieldType {
//    implicit val decoder: Decoder[ArrayFieldType] = deriveDecoder[ArrayFieldType]
//  }
//
//  case class GenericFieldType(
//    name: String,
//    summary: Option[String],
//    description: Option[String],
//    generic_name: String,
//    generic_args: List[GenericArgDescription]
//  ) extends ApiStructFieldType
//
//  object GenericFieldType {
//    implicit val decoder: Decoder[GenericFieldType] = deriveDecoder[GenericFieldType]
//  }
//
//  case class GenericArgDescription(
//    `type`: String,
//    ref_name: String
//  )
//
//  object GenericArgDescription {
//    implicit val decoder: Decoder[GenericArgDescription] = deriveDecoder[GenericArgDescription ]
//  }
//
//  case class StringFieldType(
//    name: String,
//    summary: Option[String],
//    description: Option[String]
//  ) extends ApiStructFieldType
//
//  object StringFieldType {
//    implicit val decoder: Decoder[StringFieldType] = deriveDecoder[StringFieldType]
//  }
//
//  case class BooleanFieldType(
//    name: String,
//    summary: Option[String],
//    description: Option[String]
//  ) extends ApiStructFieldType
//
//  object BooleanFieldType {
//    implicit val decoder: Decoder[BooleanFieldType] = deriveDecoder[BooleanFieldType]
//  }
//
//  case class NoneFieldType(
//    name: String,
//    summary: Option[String],
//    description: Option[String]
//  ) extends ApiStructFieldType
//
//  object NoneFieldType {
//    implicit val decoder: Decoder[NoneFieldType] = deriveDecoder[NoneFieldType]
//  }
//
//  object ApiStructFieldType {
//
//    implicit val decoder: Decoder[ApiStructFieldType] = (c: HCursor) =>
//      c.downField("type").as[String].flatMap {
//        case "Ref"      => c.as[RefFieldType]
//        case "Number"   => c.as[NumberFieldType]
//        case "Optional" => c.as[OptionalFieldType]
//        case "Array"    => c.as[ArrayFieldType]
//        case "Generic"  => c.as[GenericFieldType]
//        case "BigInt"   => c.as[BigIntFieldType]
//        case "String"   => c.as[StringFieldType]
//        case "Boolean"  => c.as[BooleanFieldType]
//        case "None"     => c.as[NoneFieldType]
//        case x          => DecodingFailure(s"Can't read TypeEnum from $x", Nil).asLeft[ApiStructFieldType]
//      }
//  }

  sealed trait ApiNumberSubtype

  case object UIntSubtype extends ApiNumberSubtype

  case object IntSubtype extends ApiNumberSubtype

  case object FloatSubtype extends ApiNumberSubtype

  object ApiNumberSubtype {

    implicit val decoder: Decoder[ApiNumberSubtype] = Decoder[String].emap {
      case "UInt"  => UIntSubtype.asRight
      case "Int"   => IntSubtype.asRight
      case "Float" => FloatSubtype.asRight
      case x       => s"Can't read SubtypeOfNumber from $x".asLeft
    }
  }

  case class ApiFunctionType(
    name: String,
    summary: Option[String],
    description: Option[String],
    params: List[ApiTypeDescription],
    result: ApiType
  )

  object ApiFunctionType {
    implicit val decoder: Decoder[ApiFunctionType] = deriveDecoder[ApiFunctionType]
  }

}
