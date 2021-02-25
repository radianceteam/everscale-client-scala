package com.radiance.generator

import com.radiance.generator.types.ApiDescription.TypeDecl
import com.radiance.generator.types.ScalaRepr._
import com.radiance.generator.types.{ApiDescription, ScalaRepr}
import io.circe.parser._
import treehugger.forest._
import definitions._
import treehuggerDSL._
import cats.implicits._

import scala.collection.mutable

object CodeGenerator extends App {

  import scala.io.Source
  val jsonString: String = Source.fromResource("api-1.8.0.json").getLines.mkString("")

  val rootRes = parse(jsonString).map(_.as[ApiDescription.Root])
  val root = rootRes.fold(
    t => throw t,
    res =>
      res.fold(
        t => throw t,
        r => r
      )
  )
  val declarationByName: Map[String, TypeDecl] = root.modules
    .flatMap(m => m.types.map(t => (s"${m.name}.${t.name}", t)))
    .toMap
  root.modules.foreach {
    m =>
      generateType(m.types.map(t => ScalaRepr.toScalaTypeDecl(t)), m.name)
      generateFunctions(m.functions.map(f => ScalaRepr.toScalaFuncDecl(f)), declarationByName, m.name, "")
  }

  def mergeComments(summary: Option[String], description: Option[String]): Option[String] =
    (summary, description) match {
      case (Some(x), Some(y)) => if (y.contains(x)) y.some else s"$x\n$y".some
      case (Some(x), None)    => x.some
      case (None, Some(y))    => y.some
      case _                  => None
    }

  def generateFunctions(
    fDecls: List[ScalaFunctionDecl],
    declarationByName: Map[String, TypeDecl],
    packageName: String,
    objName: String
  ): Unit = {
    val usedModules: mutable.Set[String] = mutable.Set()

    val allDecls = fDecls.sortBy(_.camelName).foldRight(Nil: List[Tree]) {
      case (decl, acc) =>
        val commentOpt: Option[String] = mergeComments(decl.summary, decl.description)

        val filteredParams: List[Param] = decl.params.filter(p => p.name.get != "context" && p.name.get != "_context")

        val extendedParams = filteredParams.flatMap {
          p =>
            p.typ match {
              case ScalaRefType(name) =>
                declarationByName
                  .get(name)
                  .flatMap(
                    t =>
                      t.struct_fields.map(
                        _.map(
                          f =>
                            Param(
                              f.name,
                              f.summary,
                              f.description,
                              toScalaType(f)
                            )
                        )
                      )
                  )
                  .getOrElse(List(p))
              case _                  => List(p)
            }
        }
        val returnType = TYPE_REF("Future") TYPE_OF (
          TYPE_EITHER(
            TYPE_REF("Throwable"),
            toType(decl.result, usedModules)
          )
        )

        val tree1: Tree = (DEF(decl.camelName, returnType)
          .withParams(extendedParams.map(p => PARAM(p.name.get, toType(p.typ, usedModules)): ValDef)))
        val fullInfo = commentOpt.fold("")(u => u.stripSuffix("\n") + "\n") + extendedParams
          .map(p => s"@param ${p.name.get} ${p.description.getOrElse(p.name.get)}")
          .mkString("\n")
        val tree = tree1.withDoc(fullInfo)
        tree :: acc
    }

    val tree = (PACKAGE(packageName) := BLOCK(
      allDecls
    ))
    println(treeToString(tree))
  }

  def generateType(decls: List[ScalaTypeDecl], packageName: String): Unit = {
    val usedModules: mutable.Set[String] = mutable.Set()

    val allDecls = decls.sortBy(_.name).foldRight(Nil: List[Tree]) {
      case (decl, acc) =>
        val commentOpt: Option[String] = mergeComments(decl.summary, decl.description)

        decl match {
          case ScalaCaseClassType(name, _, _, fields) =>
            val params = fields.map { f => PARAM(f.name, toType(f.typ, usedModules)): ValDef }
            val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).tree
            commentOpt.map(elm.withDoc(_)).getOrElse(elm) :: acc

          case ScalaValueClassType(name, _, _, fields) =>
            val params = fields.map { f => PARAM(f.name, toType(f.typ, usedModules)): ValDef }
            val elm = CASECLASSDEF(RootClass.newClass(name)).withParents("AnyVal").withParams(params).tree
            commentOpt.map(elm.withDoc(_)).getOrElse(elm) :: acc

          case EnumScalaType(traitName, _, _, list) =>
            val valueCheck: mutable.Set[Option[String]] = mutable.Set()

            val objDecls = list.sortBy(_.name).map {
              case ScalaCaseObjectType(objectName, value, summary, description) =>
                valueCheck.add(value)
                val subCommentOpt = mergeComments(summary, description)
                val elm = if (value.isEmpty) {
                  (CASEOBJECTDEF(objectName) withParents traitName).tree
                } else {
                  (CASEOBJECTDEF(objectName) withParents traitName).mkTree(
                    VAL("code", StringClass) withFlags (Flags.OVERRIDE) := Literal(Constant(value.get))
                  )
                }
                subCommentOpt.map(elm.withDoc(_)).getOrElse(elm)
            }
            val valueSet = valueCheck.flatten.toSet
            val sealedTrait: Tree = if (valueSet.isEmpty) {
              TRAITDEF(traitName).withFlags(Flags.SEALED).tree
            } else if (valueSet.size == list.size) {
              TRAITDEF(traitName)
                .withFlags(Flags.SEALED)
                .mkTree(
                  VAL("code", StringClass): Tree
                )
            } else {
              throw new IllegalArgumentException("Something wrong with value field")
            }

            val first = commentOpt.fold(sealedTrait)(sealedTrait.withDoc(_))
            val encloseObj = OBJECTDEF(traitName + "Enum").mkTree(first :: objDecls)
            encloseObj :: acc

          case AdtScalaType(traitName, _, _, list) =>
            val sealedTrait = TRAITDEF(traitName).withFlags(Flags.SEALED).tree
            val first = commentOpt.map(sealedTrait.withDoc(_)).getOrElse(sealedTrait)

            val childDecls = list.sortBy(_.name).map {
              case ScalaCaseObjectType(objectName, value, summary, description) =>
                val subCommentOpt = mergeComments(summary, description)
                val elm = (CASEOBJECTDEF(objectName) withParents traitName).tree
                subCommentOpt.map(elm.withDoc(_)).getOrElse(elm)

              case ScalaCaseClassType(name, _, _, fields) =>
                val params = fields.map { f => PARAM(f.name, toType(f.typ, usedModules)): ValDef }
                val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).withParents(traitName).tree
                commentOpt.map(elm.withDoc(_)).getOrElse(elm)

              case ScalaValueClassType(name, _, _, fields) =>
                val params = fields.map { f => PARAM(f.name, toType(f.typ, usedModules)): ValDef }
                val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).withParents(traitName).tree
                commentOpt.map(elm.withDoc(_)).getOrElse(elm)

              case x => throw new IllegalArgumentException(s"Unexpected value: $x in ADT definition")
            }
            val encloseObj = OBJECTDEF(traitName + "ADT").mkTree(first :: childDecls)
            encloseObj :: acc

          case x => throw new IllegalArgumentException(s"Unexpected value: $x while types pattern matching")
        }
    }

    val tree = (PACKAGE(packageName) := BLOCK(
      allDecls
    ))
    println(treeToString(tree))
  }

  private def toType(fieldType1: ScalaType, fieldType2: ScalaType, importSet: mutable.Set[String]): Type = {
    functionType(List(toType(fieldType1, importSet)), toType(fieldType2, importSet))
  }

  private def toType(fieldType: ScalaType, importSet: mutable.Set[String]): Type = fieldType match {
    case ScalaRefType(name) =>
      val refName = name.split("\\.").toList match {
        case List(m, r) =>
          importSet.add(m)
          r
        case List(r)    => r

        case x => throw new IllegalArgumentException(s"Unexpected argument in refName: $x")
      }
      TYPE_REF(refName)

    case ScalaIntType => IntClass

    case ScalaLongType => LongClass

    case ScalaFloatType => FloatClass

    case ScalaDoubleType => DoubleClass

    case ScalaBooleanType => BooleanClass

    case ScalaStringType => StringClass

    case ScalaBigIntType => BigIntClass

    case ScalaOptionType(t: ScalaType) => OptionClass TYPE_OF toType(t, importSet)

    case ListScalaType(t: ScalaType) => ListClass TYPE_OF toType(t, importSet)

    case GenericScalaType(_, arg :: Nil) => toType(arg, importSet)

    case GenericScalaType(_, arg1 :: arg2 :: Nil) => {
      toType(arg1, arg2, importSet)
    }

    case UnitScalaType => UnitClass

    case x => throw new IllegalArgumentException(s"Match error on $x")

  }

}
