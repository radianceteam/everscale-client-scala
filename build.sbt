name := "TonClient"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.3",
  "io.circe" %% "circe-core" % "0.14.0-M1",
  "io.circe" %% "circe-derivation" % "0.13.0-M4",
  "io.circe" %% "circe-parser" % "0.14.0-M1"

)
