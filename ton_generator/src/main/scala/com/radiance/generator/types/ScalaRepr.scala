package com.radiance.generator.types

import com.radiance.generator.types.ApiDescription._
import cats.implicits._

object ScalaRepr {

  case class ScalaModuleDescription(
    name: String,
    summary: Option[String],
    description: Option[String],
    types: List[ScalaType],
    functions: List[ApiFunctionType]
  )

  case class FieldDescription(
    name: String,
    typ: ScalaType,
    summary: Option[String],
    description: Option[String]
  )

  sealed trait ScalaType

  case class ScalaRefType(name: String) extends ScalaType

  case object ScalaIntType extends ScalaType

  case object ScalaLongType extends ScalaType

  case object ScalaFloatType extends ScalaType

  case object ScalaDoubleType extends ScalaType

  case object ScalaBooleanType extends ScalaType

  case object ScalaStringType extends ScalaType

  case object ScalaBigIntType extends ScalaType

  case class ScalaOptionType(t: ScalaType) extends ScalaType

  case class ListScalaType(t: ScalaType) extends ScalaType

  case object UnitScalaType extends ScalaType

  case class GenericScalaType(name: String, args: List[ScalaType]) extends ScalaType

  sealed trait ScalaTypeDecl {
    val name: String
    val summary: Option[String]
    val description: Option[String]
  }

  case class ScalaCaseClassType(
    name: String,
    summary: Option[String],
    description: Option[String],
    fields: List[FieldDescription]
  ) extends ScalaTypeDecl

  case class ScalaValueClassType(
    name: String,
    summary: Option[String],
    description: Option[String],
    fields: List[FieldDescription]
  ) extends ScalaTypeDecl

  case class ScalaCaseObjectType(
    name: String,
    value: Option[String],
    summary: Option[String],
    description: Option[String]
  ) extends ScalaTypeDecl

  case class EnumScalaType(
    name: String,
    summary: Option[String],
    description: Option[String],
    list: List[ScalaCaseObjectType]
  ) extends ScalaTypeDecl

  case class AdtScalaType(name: String, summary: Option[String], description: Option[String], list: List[ScalaTypeDecl])
      extends ScalaTypeDecl

  case class ContentIsEmptyError(
                                  name: String,
                                  td: ApiType,
                                  summary: Option[String],
                                  description: Option[String]
  ) extends ScalaTypeDecl

  def toScalaType(td: ApiType): ScalaType = td match {
    case ApiValueClassDeclForRef(ref_name) =>
      fixUndefinedType(ref_name)

    case ApiNumberType(number_type) =>
      number_type match {
          case UIntSubtype  => ScalaLongType
          case IntSubtype   => ScalaIntType
          case FloatSubtype => ScalaFloatType
        }

    case ApiValueClassDeclForNumber(number_type, number_size) =>
      (number_type, number_size) match {
          case (IntSubtype, 64) => ScalaLongType
          case _                => ScalaBigIntType
        }

    case ApiOptional(optional_inner) =>
      ScalaOptionType(toScalaType(optional_inner))

    case ApiArray(array_item) =>
      ListScalaType(toScalaType(array_item))

    case ApiGeneric(generic_name, generic_args) =>
      GenericScalaType(generic_name, generic_args.collect {
        case ga: GenericRefDescription => ScalaRefType(ga.ref_name.get)
      })


    case ApiString => ScalaStringType

    case ApiBoolean => ScalaBooleanType

    case ApiNone(_) => UnitScalaType

  }

  private def sanitize(name: String) = if (name.contains(" ")) s"`$name`" else name

  private def toCamelCase(name: String): String = {
    val segments = name.split("_").filter(_.nonEmpty).toList
    (segments.head :: segments.drop(1).map(_.capitalize)).mkString("")
  }

  private def fixUndefinedType(n: String): ScalaRefType = n match {
    case "Value"   => ScalaRefType("Value")
    case "API"     => ScalaRefType("API")
    case "Request" => ScalaRefType("Request")
    case x         => ScalaRefType(x)
  }

  def toScalaTypeDecl(td: ApiTypeDescription): ScalaTypeDecl = {
    td.typ match {

      case ApiStruct(struct_fields) =>
        ScalaCaseClassType(
          sanitize(td.name),
          td.summary,
          td.description,
          struct_fields.map(t => FieldDescription(sanitize(t.name), toScalaType(t.typ), t.summary, t.description))
        )

      case ApiEmptyStruct(value) =>
        ScalaCaseObjectType(sanitize(td.name), value, td.summary, td.description)

      case ApiEnumOfTypes(enum_types) =>
        AdtScalaType(td.name, td.summary, td.description, enum_types.map(e => toScalaTypeDecl(e)))

      case ApiEnumOfConsts(enum_consts) =>
        EnumScalaType(
          td.name,
          td.summary,
          td.description,
          enum_consts.map(t => ScalaCaseObjectType(t.name, t.value, t.summary, t.description))
        )

      case ApiNone(value) =>
        ScalaCaseObjectType(td.name, value, td.summary, td.description)

      case ApiValueClassDeclForRef(ref_name) =>
        ScalaValueClassType(
          td.name,
          td.summary,
          td.description,
          List(FieldDescription("value", fixUndefinedType(ref_name), None, None))
        )

      case ApiValueClassDeclForNumber(number_type, number_size) =>
        (number_type, number_size) match {
          case (ntSubtype, 64) =>
            ScalaValueClassType(
              td.name,
              td.summary,
              td.description,
              List(FieldDescription("value", ScalaLongType, None, None))
            )
          case (IntSubtype, 32) =>
            ScalaValueClassType(
              td.name,
              td.summary,
              td.description,
              List(FieldDescription("value", ScalaIntType, None, None))
            )
          case _                            =>
            ScalaValueClassType(
              td.name,
              td.summary,
              td.description,
              List(FieldDescription("value", ScalaBigIntType, None, None))
            )
        }

    }
  }

  case class Param(
    name: String,
    summary: Option[String],
    description: Option[String],
    typ: ScalaType
  )

  case class ScalaFunctionDecl(
    name: String,
    camelName: String,
    summary: Option[String],
    description: Option[String],
    params: List[Param],
    result: ScalaType
  )

  def toScalaFuncDecl(f: ApiFunctionType): ScalaFunctionDecl = f match {
    case ApiFunctionType(name, summary, description, params, result) =>
      ScalaFunctionDecl(
        name,
        toCamelCase(name),
        summary,
        description,
        params.map(p => Param(p.name, p.summary, p.description, toScalaType(p.typ))),
        toScalaType(result)
      )
  }
}
