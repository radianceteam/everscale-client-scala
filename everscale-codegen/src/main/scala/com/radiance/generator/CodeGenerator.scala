package com.radiance.generator

import cats.implicits._
import com.radiance.generator.types.ApiDescription.{ApiStruct, ApiType, ApiTypeDescription}
import com.radiance.generator.types.ScalaRepr._
import com.radiance.generator.types.{ApiDescription, ScalaRepr}
import io.circe.parser._
import treehugger.forest._
import definitions._
import treehuggerDSL._

import java.io.File
import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable

object CodeGenerator extends App {

  import scala.io.Source
  val jsonString: String = Source.fromResource("api.json").getLines.mkString("")

  val basePackageList: List[String] = List("com", "radiance", "jvm2")

  val genDirPath: Path = Paths.get("/home/admin/IdeaProjects/ton-client-scala/ton_client_scala/src/main/scala")

  val rootDirPath = Paths.get(genDirPath.toString, basePackageList.toArray: _*)

  Files.createDirectories(rootDirPath)

  val rootRes = parse(jsonString).map(_.as[ApiDescription.ApiRoot])

  val root = rootRes.fold(
    t => throw t,
    res =>
      res.fold(
        t => throw t,
        r => r
      )
  )

  println(root)

  val declarationByName: Map[String, ApiTypeDescription] = root.modules
    .flatMap(m => m.types.map(t => (s"${m.name}.${t.name}", t)))
    .toMap
  val fileSeq: Seq[File] = root.modules.flatMap {
    m =>
      generateTypesFile(
        rootDirPath.toFile,
        m.types.map(t => ScalaRepr.toScalaTypeDecl(t)),
        basePackageList.mkString("."),
        m.name
      ) ::
        generateModuleFile(
          rootDirPath.toFile,
          m.functions.map(f => ScalaRepr.toScalaFuncDecl(f)),
          declarationByName,
          basePackageList.mkString("."),
          m.name,
          m.summary,
          m.description
        ) :: Nil
  }

  def mergeComments(summary: Option[String], description: Option[String]): Option[String] =
    (summary, description) match {
      case (Some(x), Some(y)) => if (y.contains(x)) y.some else s"$x\n$y".some
      case (Some(x), None)    => x.some
      case (None, Some(y))    => y.some
      case _                  => None
    }

  def generateModuleFile(
                          baseDir: File,
                          fDecls: List[ScalaFunctionDecl],
                          declarationByName: Map[String, ApiTypeDescription],
                          basePackageName: String,
                          subPackageName: String,
                          maybeSummary: Option[String],
                          maybeDescription: Option[String]
  ): File = {
    val usedModules: mutable.Set[String] = mutable.Set()

    val allDecls = fDecls.sortBy(_.camelName).foldRight(Nil: List[Tree]) {
      case (decl, acc) =>
        val commentOpt: Option[String] = mergeComments(decl.summary, decl.description)

        val filteredParams: List[Param] = decl.params.filter(p => p.name != "context" && p.name != "_context")

        val extendedParams = filteredParams.flatMap {
          p =>
            p.typ match {
              case ScalaRefType(name) =>
                (for {
                  d <- declarationByName.get(name)
                  t <- d.some.filter(_.typ.isInstanceOf[ApiStruct])
                         .map(_.typ.asInstanceOf[ApiStruct])
                  h <- t.struct_fields.map {
                         f =>
                           Param(
                             f.name,
                             f.summary,
                             f.description,
                             toScalaType(f.typ)
                           )
                       }.some
                } yield {
                  h
                }).getOrElse(List(p))
              case _                  => List(p)
            }
        }
        val returnType = TYPE_REF("Future") TYPE_OF (
          TYPE_EITHER(
            TYPE_REF("Throwable"),
            toType(decl.result, usedModules)
          )
        )

        val tree1: Tree = DEF(decl.camelName, returnType)
          .withParams(extendedParams.map(p => PARAM(p.name, toType(p.typ, usedModules)): ValDef))
        val fullInfo = commentOpt.fold("")(u => u.stripSuffix("\n") + "\n") + extendedParams
          .map(p => s"@param ${p.name} ${p.description.getOrElse(p.name)}")
          .mkString("\n")
        val tree = tree1.withDoc(fullInfo)
        tree :: acc
    }

    val tree: Tree = PACKAGEHEADER(s"$basePackageName.$subPackageName").mkTree(
      List(
        IMPORT("com.radiance.jvm.Context"),
        IMPORT("com.radiance.jvm.Value"),
        IMPORT("scala.concurrent.Future"),
        CLASSDEF(s"${subPackageName.capitalize}Module")
          .withParams(PARAM("ctx", "Context"))
          .mkTree(allDecls)
          .withDoc(mergeComments(maybeSummary, maybeDescription))
      )
    )

    val subDir: Path = baseDir.toPath.resolve(subPackageName)
    val file: Path = subDir.resolve(s"${subPackageName.capitalize}Module.scala")
    Files.createFile(file)
    Files.writeString(file, treeToString(tree))
    file.toFile
  }

  def generateTypesFile(
    baseDir: File,
    decls: List[ScalaTypeDecl],
    basePackageName: String,
    subPackageName: String
  ): File = {
    val usedModules: mutable.Set[String] = mutable.Set()

    val allDecls: List[Tree] = decls.sortBy(_.name).foldRight(Nil: List[Tree]) {
      case (decl, acc) =>
        val maybeComment: Option[String] = mergeComments(decl.summary, decl.description)

        decl match {
          case ScalaCaseClassType(name, _, _, fields) =>
            val params = fields.map { f => PARAM(f.name, toType(f.typ, usedModules)): ValDef }
            val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).tree
            maybeComment.map(elm.withDoc(_)).getOrElse(elm) :: acc

          case ScalaValueClassType(name, _, _, fields) =>
            val params = fields.map { f => PARAM(f.name, toType(f.typ, usedModules)): ValDef }
            val elm = CASECLASSDEF(RootClass.newClass(name)).withParents("AnyVal").withParams(params).tree
            maybeComment.map(elm.withDoc(_)).getOrElse(elm) :: acc

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

            val first = maybeComment.fold(sealedTrait)(sealedTrait.withDoc(_))
            val encloseObj = OBJECTDEF(traitName + "Enum").mkTree(first :: objDecls)
            encloseObj :: acc

          case AdtScalaType(traitName, _, _, list) =>
            val sealedTrait = TRAITDEF(traitName).withFlags(Flags.SEALED).tree
            val first = maybeComment.map(sealedTrait.withDoc(_)).getOrElse(sealedTrait)

            val childDecls = list.sortBy(_.name).map {
              case ScalaCaseObjectType(objectName, value, summary, description) =>
                val subCommentOpt = mergeComments(summary, description)
                val elm = (CASEOBJECTDEF(objectName) withParents traitName).tree
                subCommentOpt.map(elm.withDoc(_)).getOrElse(elm)

              case ScalaCaseClassType(name, _, _, fields) =>
                val params = fields.map { f => PARAM(f.name, toType(f.typ, usedModules)): ValDef }
                val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).withParents(traitName).tree
                maybeComment.map(elm.withDoc(_)).getOrElse(elm)

              case ScalaValueClassType(name, _, _, fields) =>
                val params = fields.map { f => PARAM(f.name, toType(f.typ, usedModules)): ValDef }
                val elm = CASECLASSDEF(RootClass.newClass(name)).withParams(params).withParents(traitName).tree
                maybeComment.map(elm.withDoc(_)).getOrElse(elm)

              case x => throw new IllegalArgumentException(s"Unexpected value: $x in ADT definition")
            }
            val encloseObj = OBJECTDEF(traitName + "ADT").mkTree(first :: childDecls)
            encloseObj :: acc

          case x => throw new IllegalArgumentException(s"Unexpected value: $x while types pattern matching")
        }
    }

    val tree: Tree = PACKAGEHEADER(s"$basePackageName.$subPackageName").mkTree(
      IMPORT("com.radiance.jvm.Value") :: allDecls
    )

    val subDir: Path = baseDir.toPath.resolve(subPackageName)
    Files.createDirectory(subDir)
    val file: Path = subDir.resolve("Types.scala")
    Files.createFile(file)
    Files.writeString(file, treeToString(tree))
    file.toFile
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

    case GenericScalaType(_, _) => UnitClass

    case UnitScalaType => UnitClass

    case x => throw new IllegalArgumentException(s"Match error on $x")

  }

}
