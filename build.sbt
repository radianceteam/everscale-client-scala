import scala.sys.process.{Process, ProcessLogger}



lazy val root = (project in file("."))

lazy val ton_client = project
  .settings(
    scalaVersion := "2.13.3",
    version := "0.1",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.14.0-M1",
      "io.circe" %% "circe-derivation" % "0.13.0-M4",
      "io.circe" %% "circe-parser" % "0.14.0-M1"
    ),

    unmanagedResourceDirectories in Compile += baseDirectory.in(`TON-SDK`).value.getAbsoluteFile / "ton_client" / "client" / "build"
  )
  //.dependsOn(native % Runtime)

lazy val native = project
//  .settings(
//    nativePlatform := "win32-x86_64"
//  ).enablePlugins(JniNative)

lazy val `TON-SDK` = project
  .settings(
    buildDll := buildDllImpl.value
  )

lazy val ton_generator = project
  .settings(
    scalaVersion := "2.13.3",
    version := "0.1",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.14.0-M1",
      "io.circe" %% "circe-derivation" % "0.13.0-M4",
      "io.circe" %% "circe-parser" % "0.14.0-M1",
      "com.eed3si9n" %% "treehugger" % "0.4.4"
    )
  )


lazy val buildDll = taskKey[Unit]("Build libraries.")

lazy val buildDllImpl = Def.task {
  val sbtLog = streams.value.log
  val logger: ProcessLogger = new ProcessLogger {
    override def out(s: => String): Unit = sbtLog.info(s)
    override def err(s: => String): Unit = sbtLog.info(s)
    override def buffer[T](f: => T) = sbtLog.buffer(f)
  }
  val currentDir = Process("cmd /C chcp 65001")
  currentDir.run(logger)
  val initSubmodule =  Process("git submodule init", new File("TON-SDK"))
  initSubmodule.run(logger)
  val runScript = Process("node build", new File("TON-SDK/ton_client/client/"))
  runScript.run(logger)
}

lazy val buildBridge = taskKey[Unit]("Build libraries.")

lazy val buildBridgeImpl = Def.task {
  val sbtLog = streams.value.log
  val logger: ProcessLogger = new ProcessLogger {
    override def out(s: => String): Unit = sbtLog.info(s)
    override def err(s: => String): Unit = sbtLog.info(s)
    override def buffer[T](f: => T) = sbtLog.buffer(f)
  }
  val currentDir = Process("cmd /C chcp 65001")
  currentDir.run(logger)
  val initSubmodule =  Process("git submodule init", new File("TON-SDK"))
  initSubmodule.run(logger)
  val runScript = Process("node build", new File("TON-SDK/ton_client/client/"))
  runScript.run(logger)
}
