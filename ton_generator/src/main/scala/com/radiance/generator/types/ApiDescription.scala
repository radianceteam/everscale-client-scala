package com.radiance.generator.types

import io.circe.Decoder
import io.circe.derivation.deriveDecoder

object ApiDescription {

  case class Root(version: String, modules: List[ModuleDecl])

  object Root {
    implicit val rootDecoder: Decoder[Root] = deriveDecoder[Root]
  }

  case class ModuleDecl(
                         name: String,
                         summary: Option[String],
                         description: Option[String],
                         types: List[TypeDecl],
                         functions: List[FunctionDecl]
                       )

  object ModuleDecl {
    implicit val moduleDescriptionDecoder: Decoder[ModuleDecl] = deriveDecoder[ModuleDecl]
  }

  sealed trait TypeDeclEnum

  case object StructTypeDecl extends TypeDeclEnum

  case object EnumOfTypesDecl extends TypeDeclEnum

  case object EnumOfConstsDecl extends TypeDeclEnum

  case object NoneDecl extends TypeDeclEnum

  case object ValueClassDeclForRef extends TypeDeclEnum

  case object ValueClassDeclForNumber extends TypeDeclEnum

  case class ErrorDecl(x: String) extends TypeDeclEnum

  object TypeDeclEnum {

    implicit val typeEnumDecoder: Decoder[TypeDeclEnum] = Decoder[String].emap {
      case "Struct" => Right(StructTypeDecl)
      case "EnumOfTypes" => Right(EnumOfTypesDecl)
      case "EnumOfConsts" => Right(EnumOfConstsDecl)
      case "None" => Right(NoneDecl)
      case "Ref" => Right(ValueClassDeclForRef)
      case "Number" => Right(ValueClassDeclForNumber)
      case x => Right(ErrorDecl(x))
    }
  }

  sealed trait TypeEnum

  case object RefType extends TypeEnum

  case object NumberType extends TypeEnum

  case object OptionalType extends TypeEnum

  case object StringType extends TypeEnum

  case object BooleanType extends TypeEnum

  case object ArrayType extends TypeEnum

  case object GenericType extends TypeEnum

  case object BigIntType extends TypeEnum

  case object NoneType extends TypeEnum

  object TypeEnum {

    implicit val typeEnumDecoder: Decoder[TypeEnum] = Decoder[String].emap {
      case "Ref" => Right(RefType)
      case "Number" => Right(NumberType)
      case "Optional" => Right(OptionalType)
      case "String" => Right(StringType)
      case "Boolean" => Right(BooleanType)
      case "Array" => Right(ArrayType)
      case "Generic" => Right(GenericType)
      case "BigInt" => Right(BigIntType)
      case "None" => Right(NoneType)

      case x => Left(s"Can't read TypeEnum from $x")
    }
  }

  case class TypeDecl(
                       name: String,
                       summary: Option[String],
                       description: Option[String],
                       `type`: TypeDeclEnum,
                       struct_fields: Option[List[TypeData]],
                       enum_types: Option[List[TypeDecl]],
                       enum_consts: Option[List[TypeDecl]],
                       ref_name: Option[String],
                       number_type: Option[SubtypeOfNumber],
                       number_size: Option[Int]
                     )

  object TypeDecl {
    implicit val typeDeclarationDecoder: Decoder[TypeDecl] = deriveDecoder[TypeDecl]
  }

  case class TypeData(
                       name: Option[String],
                       summary: Option[String],
                       description: Option[String],
                       `type`: TypeEnum,
                       number_type: Option[SubtypeOfNumber],
                       number_size: Option[Int],
                       optional_inner: Option[TypeData],
                       array_item: Option[TypeData],
                       ref_name: Option[String],
                       generic_name: Option[String],
                       generic_args: Option[List[TypeData]]
                     )

  object TypeData {
    implicit val typeDescriptionDecoder: Decoder[TypeData] = deriveDecoder[TypeData]
  }

  sealed trait SubtypeOfNumber

  case object UIntSubtype extends SubtypeOfNumber

  case object IntSubtype extends SubtypeOfNumber

  case object FloatSubtype extends SubtypeOfNumber

  object SubtypeOfNumber {

    implicit val subtypeOfNumberDecoder: Decoder[SubtypeOfNumber] = Decoder[String].emap {
      case "UInt" => Right(UIntSubtype)
      case "Int" => Right(IntSubtype)
      case "Float" => Right(FloatSubtype)
      case x => Left(s"Can't read SubtypeOfNumber from $x")
    }
  }

  case class FunctionDecl(
                           name: String,
                           summary: Option[String],
                           description: Option[String],
                           params: List[TypeData],
                           result: TypeData,
                           errors: Option[String]
                         )

  object FunctionDecl {
    implicit val functionDescriptionDecoder: Decoder[FunctionDecl] = deriveDecoder[FunctionDecl]
  }


}
