import scala.sys.process.Process

val currentBranch = "master"
val pathToCmakeWin = """C:\Program Files\JetBrains\CLion 2020.2.4\bin\cmake\win\bin\cmake.exe"""
val pathToCmakeLinux = """/usr/bin/cmake"""
val pathToTonClientHeaderSdk = "TON-SDK/ton_client"
val pathToTonClientHeaderNative = "native/include"

lazy val pathToBridgeDll = SettingKey[File]("pathToBridgeDll")
lazy val pathToTestResources = SettingKey[File]("pathToTestResources")

lazy val buildDependentLib = taskKey[Unit]("Build dependent libraries.")
lazy val buildBridge = taskKey[Unit]("Build bridge library.")

val root = project in file(".")

lazy val ton_client_scala = project
  .settings(
    scalaVersion := "2.13.4",
    version := "1.6.0",
    libraryDependencies ++= Seq(
      "io.circe"                   %% "circe-core"               % "0.14.0-M1",
      "io.circe"                   %% "circe-derivation"         % "0.13.0-M4",
      "io.circe"                   %% "circe-parser"             % "0.14.0-M1",
      "io.circe"                   %% "circe-generic-extras"     % "0.13.0",
      "ch.qos.logback"              % "logback-classic"          % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging"            % "3.9.2",
      "org.scalatest"              %% "scalatest-flatspec"       % "3.2.3"    % Test,
      "org.scalatest"              %% "scalatest-shouldmatchers" % "3.2.3"    % Test,
      "org.typelevel"              %% "cats-core"                % "2.3.0-M2" % Test
    ),
    // pathToExternalDll := baseDirectory.in(`TON-SDK`).value.getAbsoluteFile / "ton_client" / "build",
    pathToTestResources := baseDirectory.in(`TON-SDK`).value.getAbsoluteFile / "ton_client" / "src" / "tests" / "contracts",
    pathToBridgeDll := baseDirectory.in(native).value.getAbsoluteFile / "build",
    Test / unmanagedResourceDirectories ++= Seq(pathToBridgeDll.value, pathToTestResources.value),
    includeFilter in unmanagedResources in Compile := "*.dll" || "*.dll.a" || "*.dll.lib" || "*.so",
    includeFilter in unmanagedResources in Test := "*",
    test in assembly := {},
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

lazy val `TON-SDK` = project
  .settings(
    buildDependentLib := buildDllImpl.value
  )

lazy val ton_generator = project
  .settings(
    libraryDependencies ++= Seq(
      "io.circe"             %% "circe-core"         % "0.14.0-M1",
      "io.circe"             %% "circe-derivation"   % "0.13.0-M4",
      "io.circe"             %% "circe-parser"       % "0.14.0-M1",
      "org.typelevel"        %% "cats-core"          % "2.3.0-M2",
      "com.github.javaparser" % "javaparser-core"    % "3.18.0",
      "com.eed3si9n"         %% "treehugger"         % "0.4.4",
      "org.scalatest"        %% "scalatest-flatspec" % "3.2.3" % Test
    )
  )

lazy val buildDllImpl = Def.task {
  OperationSystem.define match {
    case Windows =>
      Process("cmd /C chcp 65001").!
    case _       => ()
  }

  Process(s"git submodule init", new File("TON-SDK")).!
  Process(s"git checkout $currentBranch", new File("TON-SDK")).!
  Process(s"git pull", new File("TON-SDK")).!
  Process("node build", new File("TON-SDK/ton_client")).!

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
