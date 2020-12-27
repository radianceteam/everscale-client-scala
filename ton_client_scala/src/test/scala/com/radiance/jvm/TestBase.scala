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
import io.circe.{Decoder, Json}
import io.circe.derivation.deriveDecoder
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec

import scala.io.Source
import io.circe.parser._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import cats.implicits._

trait TestBase extends BeforeAndAfter { this: AnyFlatSpec =>

  implicit val ec: ExecutionContext

  implicit val abiContractDecoder: Decoder[AbiContract] = deriveDecoder[AbiContract]

  protected def encode(arr: Array[Byte]): String = Base64.getEncoder.encodeToString(arr)

  protected def decode(str: String) = new String(Base64.getDecoder.decode(str))

  private def extractAbi(version: Version, path: String) = parse(Source.fromResource(s"${version.name}/$path").mkString)
    .flatMap(_.as[AbiContract].map(u => Abi.Serialized(u))).fold(t => throw t, r => r)

  private def extractEncodedString(version: Version, path: String): String = encode(
    Files.readAllBytes(Paths.get(getClass.getResource(s"/${version.name}/$path").toURI))
  )

  protected val eventsAbiV2: Abi = extractAbi(V2, "Events.abi.json")
  protected val walletAbiV2: Abi  = extractAbi(V2, "Wallet.abi.json")
  protected val giverAbiV1: Abi  = extractAbi(V1, "Giver.abi.json")
  protected val giverWalletAbiV2: Abi  = extractAbi(V2, "GiverWallet.abi.json")
  protected val subscriptionAbiV2: Abi  = extractAbi(V2, "Subscription.abi.json")

  protected val eventsTvcV2: String = extractEncodedString(V2, "Events.tvc")
  protected val subscriptionTvcV2: String = extractEncodedString(V2, "Subscription.tvc")

  private val config = ClientConfig(
    NetworkConfig(/*"http://192.168.99.100:8888"*/"net.ton.dev".some).some
  )

  protected var ctx: Context = _
  protected var cryptoModule: CryptoModule = _
  protected var abiModule: AbiModule = _
  protected var processingModule: ProcessingModule = _
  protected var netModule: NetModule = _
  protected var bocModule: BocModule = _
  protected var tvmModule: TvmModule = _
  protected var clientModule: ClientModule = _

  implicit class FutureEitherWrapper[A](f: Future[Either[Throwable, A]]) {
    def get: A = Await.result(f, 10.minutes).fold(t => throw t, r => r)
  }

  implicit class EitherWrapper[A](e: Either[Throwable, A]) {
    def get: A = e.fold(t => throw t, r => r)
  }

  protected def signDetached(data: String , keys: KeyPair): String = (for {
    signKeys <- cryptoModule.naclSignKeypairFromSecretKey(keys.secret)
    r <- cryptoModule.naclSignDetached(data, signKeys.secret)
  } yield r).get.signature

  protected def getGramsFromGiver(address: String): Future[Either[Throwable, ResultOfProcessMessage]] = {
    val inputMsg: Json = parse(s"""{
                                  |  "dest": "$address",
                                  |  "amount": 500000000
                                  |}""".stripMargin
    ).getOrElse(throw new IllegalArgumentException("Not a Json"))

    processingModule.processMessage(
      ParamsOfEncodeMessage(
        giverAbiV1,
        "0:841288ed3b55d9cdafa806807f02a0ae0c169aa5edfe88a789a6482429756a94".some,
        None,
        CallSet(
          "sendGrams",
          None,
          inputMsg.some
        ).some,
        Signer.None,
        None
      ),
      false,
      e => println("Grams from Giver: \n" + e)
    )
  }

  protected def deployWithGiver(a: Abi, deploySet: DeploySet, callSet: CallSet, signer: Signer): Future[Either[Throwable, String]] = {
    println("Run deployWithGiver")
    (for {
      encoded <- EitherT(abiModule.encodeMessage(a, None, deploySet.some, callSet.some, signer, None))
      _ <- EitherT(Future.successful(println(encoded).asRight))
      _ <- EitherT(getGramsFromGiver(encoded.address))
      _ <- EitherT(processingModule.processMessage(
        ParamsOfEncodeMessage(a, None, deploySet.some, callSet.some, signer, None), false, e => println(e)
      ))
    } yield encoded.address).value
  }

  before {
    ctx = Context(config)
    cryptoModule = new CryptoModule(ctx)
    abiModule = new AbiModule(ctx)
    processingModule = new ProcessingModule(ctx)
    netModule = new NetModule(ctx)
    bocModule = new BocModule(ctx)
    tvmModule = new TvmModule(ctx)
    clientModule = new ClientModule(ctx)
  }

  after {
    ctx.destroy()
  }

}
