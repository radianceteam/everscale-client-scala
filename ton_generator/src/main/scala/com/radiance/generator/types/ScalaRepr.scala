package com.radiance.generator.types

import com.radiance.generator.types.ApiDescription._

object ScalaRepr {

  case class ScalaModuleDescription(
                                     name: String,
                                     summary: Option[String],
                                     description: Option[String],
                                     types: List[ScalaType],
                                     functions: List[FunctionDecl]
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

  case class Error(td: TypeData) extends ScalaType

  sealed trait ScalaTypeDecl {
    val name: String
    val summary: Option[String]
    val description: Option[String]
  }

  case class ScalaCaseClassType(name: String, summary: Option[String], description: Option[String], fields: List[FieldDescription]) extends ScalaTypeDecl

  case class ScalaCaseObjectType(name: String, value: Option[String], summary: Option[String], description: Option[String]) extends ScalaTypeDecl

  case class SimpleAdtScalaType(name: String, summary: Option[String], description: Option[String], list: List[ScalaCaseObjectType]) extends ScalaTypeDecl

  case class Adt(name: String, summary: Option[String], description: Option[String], list: List[ScalaTypeDecl]) extends ScalaTypeDecl

  case class ContentIsEmptyError(name: String, td: TypeDecl, summary: Option[String], description: Option[String]) extends ScalaTypeDecl

  case class WrongDeclError(name: String, d: ErrorDecl, summary: Option[String], description: Option[String]) extends ScalaTypeDecl

  def toScalaType(td: TypeData): ScalaType = td.`type` match {
    case RefType => td.ref_name.map(fixUndefinedType).getOrElse(Error(td))

    case NumberType => td.number_type.map {
      case UIntSubtype => ScalaLongType
      case IntSubtype => ScalaIntType
      case FloatSubtype => ScalaFloatType
    }.getOrElse(Error(td))

    case BigIntType => td.number_type.flatMap(t => td.number_size.map(i => (t, i)))
      .map {
        case (IntSubtype, 64) => ScalaLongType
        case _ => ScalaBigIntType
      }.getOrElse(Error(td))

    case StringType => ScalaStringType

    case BooleanType => ScalaBooleanType

    case OptionalType => td.optional_inner
      .map(i => ScalaOptionType(toScalaType(i))).getOrElse(Error(td))

    case ArrayType => td.array_item
      .map(i => ListScalaType(toScalaType(i))).getOrElse(Error(td))

    case GenericType => td.generic_name
      .flatMap(n => td.generic_args.map(list => (n, list.map(toScalaType))))
      .map { case (n, list) => GenericScalaType(n, list) }
      .getOrElse(Error(td))

    case NoneType => UnitScalaType

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
    case x => ScalaRefType(x)
  }

  def toCaseObjectDecl(td: TypeDecl): ScalaCaseObjectType =
    ScalaCaseObjectType(td.name, td.value, td.summary, td.description)

  def toScalaTypeDecl(td: TypeDecl): ScalaTypeDecl = td.`type` match {
    case StructTypeDecl =>
      td.struct_fields.map(list => (td.name, list.map(t =>
        FieldDescription(sanitize(t.name.get), toScalaType(t), t.summary, t.description)
      )))
        .map {
          case (n, Nil) => ScalaCaseObjectType(sanitize(n), td.value, td.summary, td.description)
          case (n, list) => ScalaCaseClassType(sanitize(n), td.summary, td.description, list)
        }
        .getOrElse(ContentIsEmptyError("Error in struct declaration", td, None, None))

    case EnumOfTypesDecl => td.enum_types.map(t =>
      Adt(td.name, td.summary, td.description, t.map(e => toScalaTypeDecl(e)))
    ).getOrElse(ContentIsEmptyError("Error in EnumOfTypes decl", td, None, None))

    case EnumOfConstsDecl => td.enum_consts.map(list =>
      SimpleAdtScalaType(td.name, td.summary, td.description, list.map(toCaseObjectDecl))
    ).getOrElse(ContentIsEmptyError("Error in EnumOfConsts", td, None, None))

    case NoneDecl => ScalaCaseObjectType(td.name, td.value, td.summary, td.description)

    case ValueClassDeclForRef => td.ref_name.map { n =>
      ScalaCaseClassType(
        td.name,
        td.summary,
        td.description,
        List(FieldDescription("value", fixUndefinedType(n), None, None))
      )
    }.getOrElse(ContentIsEmptyError("Error in ValueClassDeclForRef", td, None, None))

    case ValueClassDeclForNumber => td.number_type.flatMap(nt => td.number_size.map(ns => (nt, ns)))
      .map {
        case (IntSubtype, 64) =>  ScalaCaseClassType(
          td.name,
          td.summary,
          td.description,
          List(FieldDescription("value", ScalaLongType, None, None))
        )
        case _ => ScalaCaseClassType(
          td.name,
          td.summary,
          td.description,
          List(FieldDescription("value", ScalaBigIntType, None, None))
        )
      }.getOrElse(ContentIsEmptyError("Error in ValueClassDeclForNumber", td, None, None))

    case p@ErrorDecl(_) => WrongDeclError("WrongDeclError", p, Some(td.name), Some(td.toString))
  }

  case class Param(
                    name: Option[String],
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

  def toScalaFuncDecl(f: FunctionDecl): ScalaFunctionDecl = f match {
    case FunctionDecl(name, summary, description, params, result, _) =>
     ScalaFunctionDecl(
       name,
       toCamelCase(name),
       summary,
       description,
       params.map(p => Param(p.name, p.summary, p.description, toScalaType(p))),
       toScalaType(result)
     )

  }

}
