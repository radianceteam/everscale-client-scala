package com.radiance.jvm

import com.radiance.jvm.abi._
import com.radiance.jvm.boc._
import com.radiance.jvm.client._
import com.radiance.jvm.crypto._
import com.radiance.jvm.debot._
import com.radiance.jvm.net._
import com.radiance.jvm.processing._
import com.radiance.jvm.tvm._
import io.circe._
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec

import io.circe.Json._

import scala.concurrent.{ExecutionContext, Future}
import cats.implicits._
import cats.data.EitherT
import com.radiance.jvm.utils.UtilsModule

trait TestBase extends BeforeAndAfter with TestUtils { this: AnyFlatSpec =>

  protected val host = "http://localhost:6453"

  protected implicit val ec: ExecutionContext = ExecutionContext.global

  protected val eventsAbiV2: Abi = extractAbi(V2, "Events.abi.json")
  protected val eventsTvcV2: String = extractTvc(V2, "Events.tvc")

  protected val subscriptionAbiV2: Abi = extractAbi(V2, "Subscription.abi.json")
  protected val subscriptionTvcV2: String = extractTvc(V2, "Subscription.tvc")

  protected val giverAbiV1: Abi = extractAbi(V1, "Giver.abi.json")
  protected val giverAddress: String =
    "0:841288ed3b55d9cdafa806807f02a0ae0c169aa5edfe88a789a6482429756a94"

  protected var ctx: Context = _
  protected var cryptoModule: CryptoModule = _
  protected var abiModule: AbiModule = _
  protected var processingModule: ProcessingModule = _
  protected var netModule: NetModule = _
  protected var bocModule: BocModule = _
  protected var tvmModule: TvmModule = _
  protected var clientModule: ClientModule = _
  protected var utilsModule: UtilsModule = _
  protected var debotModule: DebotModule = _

  protected val config = ClientConfig(
    NetworkConfig(host.some).some
  )

  protected def init(): Unit = {
    ctx = Context(config)
  }

  before {
    init()
  }

  after {
    ctx.destroy()
  }

  protected def signDetached(data: String, keys: KeyPair): String =
    (for {
      signKeys <- cryptoModule.naclSignKeypairFromSecretKey(keys.secret)
      r <- cryptoModule.naclSignDetached(data, signKeys.secret)
    } yield r).get.signature

  protected def deployWithGiver(
      a: Abi,
      deploySet: DeploySet,
      callSet: CallSet,
      signer: Signer,
      callback: Request = _ => ()
  ): Future[Either[Throwable, String]] = {
    (for {
      encoded <- EitherT(
        abiModule
          .encodeMessage(a, None, deploySet.some, callSet.some, signer, None)
      )
      _ <- EitherT(getGramsFromGiver(encoded.address))
      _ <- EitherT(
        processingModule.processMessage(
          ParamsOfEncodeMessage(
            a,
            None,
            deploySet.some,
            callSet.some,
            signer,
            None
          ),
          false,
          callback
        )
      )
    } yield encoded.address).value
  }

  protected def getGramsFromGiver(
      address: String,
      callback: Request = _ => ()
  ): Future[Either[Throwable, ResultOfProcessMessage]] = {
    val inputMsg: Json = fromFields(
      Seq("dest" -> fromString(address), "amount" -> fromInt(500000000))
    )
    processingModule.processMessage(
      ParamsOfEncodeMessage(
        giverAbiV1,
        giverAddress.some,
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
      callback
    )
  }
}
