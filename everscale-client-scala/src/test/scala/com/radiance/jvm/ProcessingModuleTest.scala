package com.radiance.jvm

import com.radiance.jvm.abi._
import org.scalatest.flatspec.AnyFlatSpec
import cats.implicits._
import com.radiance.jvm.crypto.CryptoModule
import com.radiance.jvm.processing.ProcessingModule
import com.typesafe.scalalogging.Logger

class ProcessingModuleTest extends AnyFlatSpec with TestBase {
  private val logger = Logger[ProcessingModuleTest]
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
        SignerADT.Keys(keys),
        None,
        None
      )
      .get

    logger.info(s"Address: ${encoded.address}")

    val f = getGramsFromGiver(
      encoded.address,
      e => logger.info("Get grams from Giver: \n" + e)
    ).get
    logger.info(f.toString)
    assert(true)
  }
}
