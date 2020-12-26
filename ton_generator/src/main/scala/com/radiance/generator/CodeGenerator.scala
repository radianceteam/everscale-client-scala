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
  val jsonString : String = Source.fromResource("api-1.5.0.json").getLines.mkString("")

  val rootRes = parse(jsonString).map(_.as[ApiDescription.Root])
  val root = rootRes.fold(
    t => throw t,
    res => res.fold(
      t => throw t,
      r => r
    )
  )
  val declarationByName: Map[String, TypeDecl] = root.modules
    .flatMap(m => m.types.map(t => (s"${m.name}.${t.name}", t))).toMap
  root.modules.foreach { m =>
    generateType(m.types.map(t => ScalaRepr.toScalaTypeDecl(t)), m.name)
    generateFunctions(m.functions.map(f => ScalaRepr.toScalaFuncDecl(f)), declarationByName, m.name, "")
  }

  def mergeComments(summary: Option[String], description: Option[String]): Option[String] = (summary, description) match {
    case (Some(x), Some(y)) => if (y.contains(x)) y.some else s"$x\n$y".some
    case (Some(x), None) => x.some
    case (None, Some(y)) => y.some
    case _ => None
  }

  def generateFunctions(
                         fDecls: List[ScalaFunctionDecl],
                         declarationByName: Map[String, TypeDecl],
                         packageName: String,
                         objName: String
                       ): Unit = {
    val allDecls = fDecls.sortBy(_.camelName).foldRight(Nil: List[Tree]) { case (decl, acc) =>
      val commentOpt: Option[String] = mergeComments(decl.summary, decl.description)

      val filteredParams: List[Param] = decl.params.filter(p => p.name.get != "context" && p.name.get != "_context")

      val usedModules: mutable.Set[String] = mutable.Set()

      val extendedParams = filteredParams.flatMap { p =>
        p.typ match {
          case ScalaRefType(name) =>
            declarationByName.get(name).flatMap(t =>
              t.struct_fields.map(_.map(f => Param(
                f.name,
                f.summary,
                f.description,
                toScalaType(f)
              )))).getOrElse(List(p))
          case _ => List(p)
        }
      }

      val tree1 = (DEF(decl.camelName, toType(decl.result))
        .withParams(
          extendedParams.map(p => PARAM(p.name.get, toType(p.typ)): ValDef))
        ): Tree
      val fullInfo = commentOpt.fold("")(u => u.stripSuffix("\n") + "\n") + extendedParams
        .map(p => s"@param ${p.name.get} ${p.description.getOrElse("")}").mkString("\n")
      val tree = tree1.withDoc(fullInfo)
      tree :: acc
    }

    val tree = (PACKAGE(packageName) := BLOCK(
      allDecls
    ))
    println(treeToString(tree))
  }


  def generateType(decls: List[ScalaTypeDecl], packageName: String): Unit = {
    val allDecls = decls.sortBy(_.name).foldRight(Nil: List[Tree]) { case (decl, acc) =>
      val commentOpt: Option[String] = mergeComments(decl.summary, decl.description)

      decl match {
        case ScalaCaseClassType(name, _, _, fields) =>
          val params = fields.map { f => PARAM(f.name, toType(f.typ)): ValDef }
          val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).tree
          commentOpt.map(elm.withDoc(_)).getOrElse(elm) :: acc

        case SimpleAdtScalaType(traitName, _, _, list) =>
          val sealedTrait = TRAITDEF(traitName).withFlags(Flags.SEALED).tree
          val first = commentOpt.map(sealedTrait.withDoc(_)).getOrElse(sealedTrait)

          val objDecls = list.sortBy(_.name).map { case ScalaCaseObjectType(objectName, value, summary, description) =>
            val subCommentOpt = mergeComments(summary, description)
            val elm = (CASEOBJECTDEF(objectName) withParents traitName).tree

            subCommentOpt.map(elm.withDoc(_)).getOrElse(elm)
          }

          val scopeObject = OBJECTDEF(traitName).mkTree(objDecls)

          first :: scopeObject :: acc

        case Adt(traitName,_, _, list) =>
          val sealedTrait = TRAITDEF(traitName).withFlags(Flags.SEALED).tree
          val first = commentOpt.map(sealedTrait.withDoc(_)).getOrElse(sealedTrait)

          val childDecls = list.sortBy(_.name).map {
            case ScalaCaseObjectType(objectName, value, summary, description) =>
              val subCommentOpt = mergeComments(summary, description)
              val elm = (CASEOBJECTDEF(objectName) withParents traitName).tree
              subCommentOpt.map(elm.withDoc(_)).getOrElse(elm)

            case ScalaCaseClassType(name, _, _, fields) =>
              val params = fields.map { f => PARAM(f.name, toType(f.typ)): ValDef }
              val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).withParents(traitName).tree
              commentOpt.map(elm.withDoc(_)).getOrElse(elm)

            case x => throw new IllegalArgumentException(s"Unexpected value: $x in ADT definition")
          }
          val scopeObject = OBJECTDEF(traitName).mkTree(childDecls)
          first :: scopeObject :: acc

        case x => throw new IllegalArgumentException(s"Unexpected value: $x while types pattern matching")
      }
    }

    val tree = (PACKAGE(packageName) := BLOCK(
      allDecls
    ))
    println(treeToString(tree))
  }

  private def toType(fieldType1: ScalaType, fieldType2: ScalaType): Type = {
    functionType(List(toType(fieldType1)), toType(fieldType2))
  }

  private def toType(fieldType: ScalaType): Type = fieldType match {
      case ScalaRefType(name) => TYPE_REF(name)

      case ScalaIntType => IntClass

      case ScalaLongType => LongClass

      case ScalaFloatType => FloatClass

      case ScalaDoubleType => DoubleClass

      case ScalaBooleanType => BooleanClass

      case ScalaStringType => StringClass

      case ScalaBigIntType =>BigIntClass

      case ScalaOptionType(t: ScalaType) => OptionClass TYPE_OF toType(t)

      case ListScalaType(t: ScalaType) => ListClass TYPE_OF toType(t)

      case GenericScalaType(name: String, arg :: Nil) => toType(arg)

      case GenericScalaType(name: String, arg1 :: arg2 :: Nil) => {
        toType(arg1, arg2)
      }

      case UnitScalaType => UnitClass

      case x => throw new IllegalArgumentException(s"Match error on $x")

    }


}
