package com.radiance.jvm.temp

import com.radiance.jvm.Utils._
import com.radiance.jvm._
import com.radiance.jvm.abi.{AbiADT, AbiContract}
import com.radiance.jvm.client.{ClientConfig, NetworkConfig}
import io.circe.{Decoder, Encoder}
import io.circe.derivation.deriveDecoder
import io.circe.parser.parse

import java.nio.file.{Files, Paths}
import scala.io.Source
import cats.implicits._
import com.radiance.TestUtils
import com.radiance.jvm.crypto.{KeyPair, ParamsOfNaclSign, ParamsOfNaclSignKeyPairFromSecret, ResultOfNaclSignDetached}

import scala.concurrent.ExecutionContext

object Mod {

  val EVENTS: String = "Events"

  class TestClient extends TestUtils {
    private val host = "http://localhost:80"

    private val config = ClientConfig(
      NetworkConfig(host.some).some
    )
    private implicit val ec: ExecutionContext = ExecutionContext.global

    private val ctx: Context = Context(config)

    def pack(name: String, abi_version: Option[Version]): (AbiADT.Abi, String) = {
      (abi(name, abi_version), tvc(name, abi_version))
    }

    def abi(name: String, version: Option[Version]): AbiADT.Abi = {
      read_abi(s"${contracts_path(version)}/$name.abi.json")
    }

    def read_abi(path: String): AbiADT.Abi = {
      implicit val abiContractDecoder: Decoder[AbiContract] = deriveDecoder[AbiContract]
      parse(Source.fromResource(path).mkString)
        .flatMap(_.as[AbiContract].map(u => AbiADT.Serialized(u)))
        .fold(t => throw t, identity)
    }

    def tvc(name: String, abi_version: Option[Version]): String = {
      encode(
        Files.readAllBytes(
          Paths.get(getClass.getResource(s"/${contracts_path(abi_version)}/$name.tvc").toURI)
        )
      )
    }

    def contracts_path(v: Option[Version]): String = v
      .map {
        case V1 => "abi_v1"
        case V2 => "abi_v2"
      }
      .getOrElse("abi_v2")

    def request[In: Encoder, Out: Decoder](functionName: String, arg: In): Out = {
      ctx.execAsync[In, Out](functionName, arg).get
    }

    def sign_detached(data: String, keys: KeyPair): String = {
      val sign_keys: KeyPair = request[ParamsOfNaclSignKeyPairFromSecret, KeyPair](
        "crypto.nacl_sign_keypair_from_secret_key",
        ParamsOfNaclSignKeyPairFromSecret(keys.secret)
      )
      val result: ResultOfNaclSignDetached = request[ParamsOfNaclSign, ResultOfNaclSignDetached](
        "crypto.nacl_sign_detached",
        ParamsOfNaclSign(
          data,
          sign_keys.secret
        )
      )
      result.signature
    }

  }
}
