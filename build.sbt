import scala.sys.process.Process

val circeVersion = "0.14.1"
val circeDerivationVersion = "0.13.0-M5"
val catsCoreVersion = "2.6.1"
val scalaTestVersion = "3.2.3"
val javaParserVersion = "3.23.0"
val treehuggerVersion = "0.4.4"
val logbackClassicVersion = "1.2.6"
val scalaLoggingVersion = "3.9.4"

val currentBranch = "master"
val pathToCmakeWin = """C:\Program Files\JetBrains\CLion 2021.1\bin\cmake\win\bin\cmake.exe"""
val pathToCmakeLinux = """/usr/bin/cmake"""
val pathToTonClientHeaderSdk = "ever-sdk/ton_client"
val pathToTonClientHeaderNative = "native/include"

lazy val pathToBridgeDll = SettingKey[File]("pathToBridgeDll")
lazy val pathToTestResources = SettingKey[File]("pathToTestResources")

lazy val buildDependentLib = taskKey[Unit]("Build dependent libraries.")
lazy val buildBridge = taskKey[Unit]("Build bridge library.")

val root = project in file(".")

lazy val `everscale-client-scala` = project
  .settings(
    scalaVersion := "2.13.4",
    version := "1.37.0",
    libraryDependencies ++= Seq(
      "io.circe"                   %% "circe-core"               % circeVersion,
      "io.circe"                   %% "circe-derivation"         % circeDerivationVersion,
      "io.circe"                   %% "circe-parser"             % circeVersion,
      "io.circe"                   %% "circe-generic-extras"     % circeVersion,
      "ch.qos.logback"              % "logback-classic"          % logbackClassicVersion,
      "com.typesafe.scala-logging" %% "scala-logging"            % scalaLoggingVersion,
      "org.scalatest"              %% "scalatest-flatspec"       % scalaTestVersion % Test,
      "org.scalatest"              %% "scalatest-shouldmatchers" % scalaTestVersion % Test,
      "org.typelevel"              %% "cats-core"                % catsCoreVersion  % Test
    ),
    pathToTestResources := (`ever-sdk` / baseDirectory).value.getAbsoluteFile / "ton_client" / "src" / "tests" / "contracts",
    pathToBridgeDll := (native / baseDirectory).value.getAbsoluteFile / "build",
    Test / unmanagedResourceDirectories ++= Seq(pathToBridgeDll.value, pathToTestResources.value),
    Compile / unmanagedResources / includeFilter := "*.dll" || "*.dll.a" || "*.dll.lib" || "*.so",
    Test / unmanagedResources / includeFilter := "*",
    assembly / test := {},
    scalacOptions := Seq(
      "-Xfatal-warnings",
      "-encoding",
      "utf-8",
      "-explaintypes",
      "-feature",
      "-language:existentials",
      "-language:experimental.macros",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-Ymacro-annotations",
      "-unchecked",
      "-Xcheckinit",
      "-Xlint:adapted-args",
      "-Xlint:constant",
      "-Xlint:delayedinit-select",
      "-Xlint:deprecation",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Wdead-code",
      "-Wextra-implicit",
      "-Wnumeric-widen",
      "-Wunused:implicits",
      "-Wunused:imports",
      "-Wunused:locals",
      "-Wunused:params",
      "-Wunused:patvars",
      "-Wunused:privates",
      "-Wvalue-discard"
    )
  )

lazy val native = project
  .settings(
    buildBridge := buildBridgeImpl.value
  )

lazy val `ever-sdk` = project
  .settings(
    buildDependentLib := buildDllImpl.value
  )

lazy val `everscale-codegen` = project
  .settings(
    libraryDependencies ++= Seq(
      "io.circe"             %% "circe-core"         % circeVersion,
      "io.circe"             %% "circe-derivation"   % circeDerivationVersion,
      "io.circe"             %% "circe-parser"       % circeVersion,
      "org.typelevel"        %% "cats-core"          % catsCoreVersion,
      "com.github.javaparser" % "javaparser-core"    % javaParserVersion,
      "com.eed3si9n"         %% "treehugger"         % treehuggerVersion,
      "org.scalatest"        %% "scalatest-flatspec" % scalaTestVersion % Test
    )
  )

lazy val buildDllImpl = Def.task {
  OperationSystem.define match {
    case Windows =>
      Process("cmd /C chcp 65001").!
    case _       => ()
  }

  Process(s"git submodule init", new File("ever-sdk")).!
  Process(s"git submodule update", new File("ever-sdk")).!
  Process(s"git checkout $currentBranch", new File("ever-sdk")).!
  Process(s"git pull", new File("ever-sdk")).!
  Process("node build", new File("ever-sdk/ton_client")).!

}

// TODO copy tonclient.h, add Cmake generator configuration
lazy val buildBridgeImpl = Def.task {
  val pathToParent = baseDirectory.value.getAbsoluteFile
  val pathToBuildDir = baseDirectory.value.getAbsoluteFile / "build"

  OperationSystem.define match {
    case Windows =>
      Process("cmd /C chcp 65001").!
      val deleteDir = "cmd /C if exist build rm /S /Q build"
      Process(deleteDir, new File("native")).!
      val createDir = "cmd /C mkdir build"
      Process(createDir, new File("native")).!
      val cmakeLoadCommand =
        s""""$pathToCmakeWin" -DCMAKE_BUILD_TYPE=Release -G "CodeBlocks - NMake Makefiles" $pathToParent"""
      Process(cmakeLoadCommand, new File("native/build")).!
      val cmakeCleanCommand = s""""$pathToCmakeWin" --build $pathToBuildDir --target clean"""
      Process(cmakeCleanCommand, new File("native")).!
      val cmakeBuildCommand = s""""$pathToCmakeWin" --build $pathToBuildDir --target all"""
      Process(cmakeBuildCommand, new File("native")).!
    case Linux   =>
      Process("mkdir -p build", pathToParent).!
      val cmakeLoadCommand = s"""$pathToCmakeLinux -DCMAKE_BUILD_TYPE=Release $pathToParent -B$pathToBuildDir"""
      Process(cmakeLoadCommand).!
      val cmakeCommand = s"""$pathToCmakeLinux --build $pathToBuildDir --target all -- -j 6"""
      Process(cmakeCommand, new File("native")).!

    case _ => ???
  }
}
