package com.radiance.samples

import com.radiance.jvm.crypto.CryptoModule

import scala.annotation.nowarn
import scala.concurrent.ExecutionContext

@nowarn
object ContractDeploy {

  def readFromFile(name: String) = ???
  def readFromFileAsObj(name: String) = ???

  import com.radiance.jvm.abi._
  import com.radiance.jvm.processing._
  import com.radiance.jvm._
  import io.circe._
  import io.circe.Json._

  import cats.implicits._
  import cats.data.EitherT

  import scala.concurrent.Future

  implicit val ec: ExecutionContext = ExecutionContext.global

  val ctx: Context = ???
  val callback: Json => Unit = ???
  val abiModule = new AbiModule(ctx)
  val processingModule = new ProcessingModule(ctx)
  val cryptoModule = new CryptoModule(ctx)

  protected val giverAbiV1: AbiADT.Abi = readFromFile("Giver.abi.json")

  val giverAddress: String =
    "0:841288ed3b55d9cdafa806807f02a0ae0c169aa5edfe88a789a6482429756a94"

  // Create keys
  val keys = cryptoModule.generateRandomSignKeys.getOrElse(throw new IllegalArgumentException())

  // Get grams for deploy
  def getGrams(
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
        CallSet("sendGrams", None, inputMsg.some).some,
        SignerADT.None,
        None,
        None
      ),
      send_events = true,
      callback
    )
  }

  def deployContract(
    a: AbiADT.Abi,
    deploySet: DeploySet,
    callSet: CallSet,
    signer: SignerADT.Signer,
    callback: Request = _ => ()
  ): Future[Either[Throwable, String]] = {
    (for {
      encoded <- EitherT(
                   abiModule
                     .encodeMessage(a, None, deploySet.some, callSet.some, signer, None, None)
                 )
      _ <- EitherT(getGrams(encoded.address))
      _ <- EitherT(
             processingModule.processMessage(
               ParamsOfEncodeMessage(
                 a,
                 None,
                 deploySet.some,
                 callSet.some,
                 signer,
                 None,
                 None
               ),
               send_events = true,
               callback
             )
           )
    } yield encoded.address).value
  }

  // load ABI from file
  val subscriptionAbiV2: AbiADT.Abi = readFromFileAsObj("Subscription.abi.json")
  // load TVC from file
  val subscriptionTvcV2: String = readFromFile("Subscription.tvc")

  // define additional data for input object
  val walletAddress: String = ???

  // Deploy it
  deployContract(
    subscriptionAbiV2,
    DeploySet(subscriptionTvcV2),
    CallSet(
      "constructor",
      None,
      fromFields(Seq("wallet" -> fromString(s"$walletAddress"))).some
    ),
    SignerADT.Keys(keys)
  )

}
