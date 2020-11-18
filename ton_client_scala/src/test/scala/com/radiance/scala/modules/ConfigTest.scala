package com.radiance.scala.modules

import java.nio.file.{Files, Paths}
import java.util.Base64

import cats.data.EitherT
import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.types.AbiTypes.{Abi, AbiContract, CallSet, DeploySet, ParamsOfEncodeMessage, Serialized, Signer, SignerNone}
import com.radiance.scala.types.ClientTypes.{ClientConfig, NetworkConfig}
import com.radiance.scala.types.CryptoTypes.KeyPair
import com.radiance.scala.types.ProcessingTypes.ResultOfProcessMessage
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec

import scala.io.Source
import io.circe.parser._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

trait ConfigTest extends BeforeAndAfter { this: AnyFlatSpec =>
  import TestDecoders._
  implicit val ec: ExecutionContext

  protected def encode(arr: Array[Byte]): String = Base64.getEncoder.encodeToString(arr)

  protected def decode(str: String) = new String(Base64.getDecoder.decode(str))

  private def extractAbi(path: String) = parse(Source.fromResource(path).mkString)
    .flatMap(_.as[AbiContract].map(u => Serialized(u))).fold(t => throw t, r => r)

  private def extractEncodedString(path: String): String = encode(
    Files.readAllBytes(Paths.get(getClass.getResource(s"/$path").toURI))
  )

  protected val eventsAbi: Abi = extractAbi("Events.abi.json")
  protected val walletAbi: Abi  = extractAbi("Wallet.abi.json")
  protected val giverAbi: Abi  = extractAbi("Giver.abi.json")
  protected val giverWalletAbi: Abi  = extractAbi("GiverWallet.abi.json")
  protected val multisigWalletAbi: Abi  = extractAbi("SetcodeMultisigWallet.abi.json")
  protected val subscriptionAbi: Abi  = extractAbi("Subscription.abi.json")

  protected val eventsTvc: String = extractEncodedString("Events.tvc")
  protected val subscriptionTvc: String = extractEncodedString("Subscription.tvc")

  private val config = ClientConfig(Some(NetworkConfig("net.ton.dev", None, None, None, None, None, None)), None, None)

  protected var context: TonContextScala = _
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
        SignerNone,
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
    context = TonContextScala(config)
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
