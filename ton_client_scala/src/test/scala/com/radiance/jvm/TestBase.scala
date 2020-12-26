package com.radiance.jvm

import java.nio.file.{Files, Paths}
import java.util.Base64

import cats.data.EitherT
import com.radiance.jvm.Context
import com.radiance.jvm.abi._
import com.radiance.jvm.boc.BocModule
import com.radiance.jvm.client.{ClientConfig, ClientModule, NetworkConfig}
import com.radiance.jvm.crypto.{CryptoModule, KeyPair}
import com.radiance.jvm.net.NetModule
import com.radiance.jvm.processing.{ProcessingModule, ResultOfProcessMessage}
import com.radiance.jvm.tvm.TvmModule
import io.circe.Decoder
import io.circe.derivation.deriveDecoder
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec

import scala.io.Source
import io.circe.parser._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

trait TestBase extends BeforeAndAfter { this: AnyFlatSpec =>

  implicit val ec: ExecutionContext

  implicit val abiContractdecoder: Decoder[AbiContract] = deriveDecoder[AbiContract]

  protected def encode(arr: Array[Byte]): String = Base64.getEncoder.encodeToString(arr)

  protected def decode(str: String) = new String(Base64.getDecoder.decode(str))

  private def extractAbi(version: Version, path: String) = parse(Source.fromResource(s"${version.name}/$path").mkString)
    .flatMap(_.as[AbiContract].map(u => Abi.Serialized(u))).fold(t => throw t, r => r)

  private def extractEncodedString(version: Version, path: String): String = encode(
    Files.readAllBytes(Paths.get(getClass.getResource(s"/${version.name}/$path").toURI))
  )

  protected val eventsAbi: Abi = extractAbi(V2, "Events.abi.json")
  protected val walletAbi: Abi  = extractAbi(V2, "Wallet.abi.json")
  protected val giverAbi: Abi  = extractAbi(V1, "Giver.abi.json")
  protected val giverWalletAbi: Abi  = extractAbi(V2, "GiverWallet.abi.json")
  protected val subscriptionAbi: Abi  = extractAbi(V2, "Subscription.abi.json")

  protected val eventsTvc: String = extractEncodedString(V2, "Events.tvc")
  protected val subscriptionTvc: String = extractEncodedString(V2, "Subscription.tvc")

  private val config = ClientConfig(
    Some(NetworkConfig(/*"http://192.168.99.100:8888"*/Some("net.ton.dev"), None, None, None, None, None, None, None, None)),
    None,
    None
  )

  protected var context: Context = _
  protected var crypto: CryptoModule = _
  protected var abi: AbiModule = _
  protected var processing: ProcessingModule = _
  protected var net: NetModule = _
  protected var boc: BocModule = _
  protected var tvm: TvmModule = _
  protected var client: ClientModule = _

  implicit class FutureEitherWrapper[A](f: Future[Either[Throwable, A]]) {
    def get: A = Await.result(f, 10.minutes).fold(t => throw t, r => r)
  }

  implicit class EitherWrapper[A](e: Either[Throwable, A]) {
    def get: A = e.fold(t => throw t, r => r)
  }

  protected def signDetached(data: String , keys: KeyPair): String = (for {
    signKeys <- crypto.naclSignKeypairFromSecretKey(keys.secret)
    r <- crypto.naclSignDetached(data, signKeys.secret)
  } yield r).get.signature

  protected def getGramsFromGiver(address: String): Future[Either[Throwable, ResultOfProcessMessage]] =
    processing.processMessage(
      ParamsOfEncodeMessage(
        giverAbi,
        Some("0:841288ed3b55d9cdafa806807f02a0ae0c169aa5edfe88a789a6482429756a94"),
        None,
        Some(CallSet(
          "sendGrams",
          None,
          Some(parse(s"""{"dest":"$address","amount":500000000}""").getOrElse(throw new IllegalArgumentException("Not a Json")))
        )),
        Signer.None,
        None
      ),
      false,
      e => println("Grams from Giver: \n" + e)
    )

  protected def deployWithGiver(a: Abi, deploySet: DeploySet, callSet: CallSet, signer: Signer): Future[Either[Throwable, String]] = {
    println("Run deployWithGiver")
    (for {
      encoded <- EitherT(abi.encodeMessage(a, None, Some(deploySet), Some(callSet), signer, None))
      _ <- EitherT(getGramsFromGiver(encoded.address))
      _ <- EitherT(processing.processMessage(
        ParamsOfEncodeMessage(a, None, Some(deploySet), Some(callSet), signer, None), false, _ => ()
      ))
    } yield encoded.address).value
  }

  before {
    context = Context(config)
    crypto = new CryptoModule(context)
    abi = new AbiModule(context)
    processing = new ProcessingModule(context)
    net = new NetModule(context)
    boc = new BocModule(context)
    tvm = new TvmModule(context)
    client = new ClientModule(context)
  }

  after {
    context.destroy()
  }

}
