package com.radiance.jvm

import com.radiance.jvm.abi._
import io.circe.parser._

import java.nio.file.{Files, Paths}
import scala.io.Source
import Utils._
import io.circe._
import io.circe.derivation._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

trait TestUtils {

  implicit val abiContractDecoder: Decoder[AbiContract] = deriveDecoder[AbiContract]

  protected def extractAbi(version: Version, path: String) = parse(Source.fromResource(s"${version.name}/$path").mkString)
    .flatMap(_.as[AbiContract].map(u => Abi.Serialized(u))).fold(t => throw t, r => r)

  protected def extractTvc(version: Version, path: String): String = encode(
    Files.readAllBytes(Paths.get(getClass.getResource(s"/${version.name}/$path").toURI))
  )

  implicit class FutureEitherWrapper[A](f: Future[Either[Throwable, A]]) {
    def get: A = Await.result(f, 10.minutes).fold(t => throw t, r => r)
  }

  implicit class EitherWrapper[A](e: Either[Throwable, A]) {
    def get: A = e.fold(t => throw t, r => r)
  }
}
