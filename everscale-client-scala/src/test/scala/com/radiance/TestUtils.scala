package com.radiance

import com.radiance.jvm.Utils.encode
import com.radiance.jvm.Version
import com.radiance.jvm.abi.{AbiADT, AbiContract}
import io.circe.Decoder
import io.circe.derivation.deriveDecoder
import io.circe.parser.parse

import java.nio.file.{Files, Paths}
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.concurrent.duration._

trait TestUtils {

  implicit val abiContractDecoder: Decoder[AbiContract] =
    deriveDecoder[AbiContract]

  protected def extractAbi(version: Version, path: String) =
    parse(Source.fromResource(s"${version.name}/$path").mkString)
      .flatMap(_.as[AbiContract].map(u => AbiADT.Serialized(u)))
      .fold(t => throw t, r => r)

  protected def extractTvc(version: Version, path: String): String =
    encode(
      Files.readAllBytes(
        Paths.get(getClass.getResource(s"/${version.name}/$path").toURI)
      )
    )

  implicit class FutureEitherWrapper[A](f: Future[Either[Throwable, A]]) {
    def get: A = Await.result(f, 10.minutes).fold(t => throw t, r => r)
  }

  implicit class EitherWrapper[A](e: Either[Throwable, A]) {
    def get: A = e.fold(t => throw t, r => r)
  }
}
