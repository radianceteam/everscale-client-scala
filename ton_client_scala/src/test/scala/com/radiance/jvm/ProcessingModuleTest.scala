package com.radiance.jvm

import com.radiance.jvm.abi._
import org.scalatest.flatspec.AnyFlatSpec

import cats.implicits._
import com.radiance.jvm.crypto.CryptoModule
import com.radiance.jvm.processing.ProcessingModule

class ProcessingModuleTest extends AnyFlatSpec with TestBase {

  override def init(): Unit = {
    super.init()
    cryptoModule = new CryptoModule(ctx)
    abiModule = new AbiModule(ctx)
    processingModule = new ProcessingModule(ctx)
  }

  behavior.of("ProcessingModule")

  it should "wait for message" in {
    val keys = cryptoModule.generateRandomSignKeys.get

    val encoded = abiModule
      .encodeMessage(
        eventsAbiV2,
        None,
        DeploySet(eventsTvcV2).some,
        CallSet(
          "constructor",
          FunctionHeader(None, None, keys.public.some).some
        ).some,
        Signer.Keys(keys),
        None
      )
      .get

    println(s"Address: ${encoded.address}")

    val f = getGramsFromGiver(
      encoded.address,
      e => println("Get grams from Giver: \n" + e)
    ).get
    println(f)
    assert(true)
  }
}
