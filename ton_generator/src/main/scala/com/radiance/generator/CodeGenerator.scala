package com.radiance.generator

import com.radiance.generator.types.ApiDescription.TypeDecl
import com.radiance.generator.types.ScalaRepr._
import com.radiance.generator.types.{ApiDescription, ScalaRepr}
import io.circe._
import io.circe.parser._
import io.circe.syntax._

object CodeGenerator extends App {

  import scala.io.Source
  val jsonString : String = Source.fromResource("api.json").getLines.mkString("")

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
  //println(declarationByName)
  root.modules.foreach { m =>
    //m.functions.foldLeft((Map.empty, Map.empty))(f => f.params.map(p => p.))
    // val declaredTypes = m.types.map(t => (s"${m.name}.${t.name}", t)).toMap
    println(s"${m.name} ****************************************")
    //generateType(m.types.map(t => ScalaRepr.toScalaTypeDecl(t)), m.name)
    generateFunctions(m.functions.map(f => ScalaRepr.toScalaFuncDecl(f)), declarationByName, m.name, "")
  }
  import treehugger.forest._
  import definitions._
  import treehuggerDSL._

  def mergeComments(summary: Option[String], description: Option[String]): Option[String] = summary
    .flatMap(s => description.map(d => if(d.contains(s)) d else s"$s\n$d"))
    .orElse(description)

  object sym {
    val A = RootClass.newAliasType("A")
    val B = RootClass.newAliasType("B")
  }


  def generateFunctions(
                         fDecls: List[ScalaFunctionDecl],
                         declarationByName: Map[String, TypeDecl],
                         packageName: String,
                         objName: String
                       ): Unit = {
    val allDecls = fDecls.foldRight(Nil: List[Tree]) { case (decl, acc) =>
      val commentOpt: Option[String] = mergeComments(decl.summary, decl.description)

      val filteredParams = decl.params.filter(p => p.name.get != "context" && p.name.get != "_context")

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


      val tree1 = (DEF(decl.name, toType(decl.result))
        //.withTypeParams(decl.params.map(p => TYPEVAR(toType(p.typ).typeSymbol.newTypeParameter(p.name.get)))))
        .withParams(
          extendedParams.map(p => PARAM(p.name.get, toType(p.typ))): Iterable[ValDef])
        ) : Tree
      val fullInfo = commentOpt.getOrElse("") + extendedParams
        .map(p => s"@param ${p.name.get} ${p.description.getOrElse("")}").mkString("\n")
      val tree = tree1.withDoc(fullInfo)
        println(treeToString(tree))
//      decl.params.foldRight(DEF(decl.name, toType(decl.result))) { case (param, acc) =>
//        acc.wit
//      }
      null
    }
  }

  def generateType(decls: List[ScalaTypeDecl], packageName: String): Unit = {
    val allDecls = decls.foldRight(Nil: List[Tree]) { case (decl, acc) =>
      val commentOpt: Option[String] = mergeComments(decl.summary, decl.description)

      decl match {
        case ScalaCaseClass(name, _, _, fields) =>
          val params = fields.map { f => PARAM(f.name, toType(f.typ)): ValDef }
          val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).tree
          commentOpt.map(elm.withDoc(_)).getOrElse(elm) :: acc

        case CaseObjectScalaType(name, _, _) =>
          val elm = OBJECTDEF(name).tree
          commentOpt.map(elm.withDoc(_)).getOrElse(elm) :: acc

        case SimpleAdtScalaType(traitName, _, _, list) =>
          val sealedTrait = TRAITDEF(traitName).withFlags(Flags.SEALED).tree
          val first = commentOpt.map(sealedTrait.withDoc(_)).getOrElse(sealedTrait)
          val objDecls = list.map { case CaseObjectScalaType(objectName, summary, description) =>
            val subCommentOpt = mergeComments(summary, description)
            val elm = (OBJECTDEF(objectName) withParents traitName).tree
            subCommentOpt.map(elm.withDoc(_)).getOrElse(elm)
          }
          first :: objDecls ::: acc

        case Adt(traitName,_, _, list) =>
          val sealedTrait = TRAITDEF(traitName).withFlags(Flags.SEALED).tree
          val first = commentOpt.map(sealedTrait.withDoc(_)).getOrElse(sealedTrait)

          val childDecls = list.map {
            case CaseObjectScalaType(objectName, summary, description) =>
              val subCommentOpt = mergeComments(summary, description)
              val elm = (OBJECTDEF(objectName) withParents traitName).tree
              subCommentOpt.map(elm.withDoc(_)).getOrElse(elm)

            case ScalaCaseClass(name, _, _, fields) =>
              val params = fields.map { f => PARAM(f.name, toType(f.typ)): ValDef }
              val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).withParents(traitName).tree
              commentOpt.map(elm.withDoc(_)).getOrElse(elm)

            case x =>
              println("***************************************************")
              println(x)
              Block()

          }
          first :: childDecls ::: acc

        case _ => acc

      }
    }

    // generate class definition
    val tree = (PACKAGE(packageName) := BLOCK(
      allDecls
    ))
//    val tree = PACKAGE(packageName) ::: allDecls
//      //.inPackage(packageName)

    // pretty print the tree
    println(treeToString(tree))
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

      case UnitScalaType => UnitClass

    }


}
