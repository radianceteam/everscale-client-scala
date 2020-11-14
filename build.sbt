import com.sun.javafx.PlatformUtil
import scala.sys.process.{Process, ProcessLogger}

lazy val pathToExternalDll = SettingKey[File]("pathToExternalDll")
lazy val pathToBridgeDll = SettingKey[File]("pathToBridgeDll")

lazy val buildDependentLib = taskKey[Unit]("Build dependent libraries.")
lazy val buildBridge = taskKey[Unit]("Build bridge library.")

lazy val root = project in file(".")

lazy val ton_client_scala = project
  .settings(
    scalaVersion := "2.13.3",
    version := "1.1.0",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.14.0-M1",
      "io.circe" %% "circe-derivation" % "0.13.0-M4",
      "io.circe" %% "circe-parser" % "0.14.0-M1",

      "org.scalatest" %% "scalatest-flatspec" % "3.2.3" % Test,
      "org.typelevel" %% "cats-core" % "2.3.0-M2" % Test
    ),

    pathToExternalDll := baseDirectory.in(`TON-SDK`).value.getAbsoluteFile  / "ton_client" / "client" / "build",
    pathToBridgeDll := baseDirectory.in(native).value.getAbsoluteFile / "build",
    unmanagedResourceDirectories in Compile ++= Seq(
      pathToExternalDll.value,
      pathToBridgeDll.value
    ),
    unmanagedResourceDirectories in Runtime ++= Seq(
      pathToExternalDll.value,
      pathToBridgeDll.value
    ),
    unmanagedResourceDirectories in Test ++= Seq(
      pathToExternalDll.value,
      pathToBridgeDll.value
    ),
    mainClass in assembly := Some("com.radiance.scala.tonclient.TonContextScala")
  )

lazy val native = project
  .settings(
    buildBridge := buildBridgeImpl.value
  )

lazy val `TON-SDK` = project
  .settings(
    buildDependentLib := buildDllImpl.value
  )

lazy val buildDllImpl = Def.task {
  val sbtLog = streams.value.log
  val logger: ProcessLogger = new ProcessLogger {
    override def out(s: => String): Unit = sbtLog.info(s)

    override def err(s: => String): Unit = sbtLog.info(s)

    override def buffer[T](f: => T) = sbtLog.buffer(f)
  }
  if (PlatformUtil.isWindows) {
    val currentDir = Process("cmd /C chcp 65001")
    currentDir.run(logger)
  }

  val initSubmodule = Process("git submodule init", new File("TON-SDK"))
  initSubmodule.run(logger)
  val runScript = Process("node build", new File("TON-SDK/ton_client/client/"))
  runScript.run(logger)
}

lazy val buildBridgeImpl = Def.task {
  val sbtLog = streams.value.log
  val logger: ProcessLogger = new ProcessLogger {
    override def out(s: => String): Unit = sbtLog.info(s)
    override def err(s: => String): Unit = sbtLog.info(s)
    override def buffer[T](f: => T) = sbtLog.buffer(f)
  }

  // TODO
}
