package com.radiance.jvm

import com.radiance.jvm.abi.{AbiADT, _}
import com.radiance.jvm.crypto.{CryptoModule, KeyPair}
import com.radiance.jvm.debot.DebotModule
import org.scalatest.flatspec.AnyFlatSpec
import com.radiance.jvm.Utils._
import cats.implicits._
import com.radiance.jvm.processing.ProcessingModule
import com.typesafe.scalalogging.Logger
import io.circe.parser._
import io.circe.syntax._

import java.nio.charset.Charset
import scala.annotation.nowarn

@nowarn
class DebotModuleTest extends AnyFlatSpec with TestBase {

  private val logger = Logger[DebotModuleTest]

  override def init(): Unit = {
    super.init()
    debotModule = new DebotModule(ctx)
    cryptoModule = new CryptoModule(ctx)
    abiModule = new AbiModule(ctx)
    processingModule = new ProcessingModule(ctx)
  }

  private def initDebot: (String, String, KeyPair) = {
    val keys = cryptoModule.generateRandomSignKeys.get

    val targetAbi: AbiADT.Abi = extractAbi(V2, "testDebotTarget.abi.json")
    val debotAbi: AbiADT.Abi = extractAbi(V2, "testDebot.abi.json")

    val targetAddr = deployWithGiver(
      targetAbi,
      DeploySet(extractTvc(V2, "testDebotTarget.tvc")),
      CallSet("constructor"),
      SignerADT.Keys(keys)
    ).get

    val debotAddr = deployWithGiver(
      debotAbi,
      DeploySet(extractTvc(V2, "testDebot.tvc")),
      CallSet(
        "constructor",
        None,
        parse(
          s"""{
             |"debotAbi": "${hexEncode(
            debotAbi.asJson.noSpaces.getBytes(Charset.forName("UTF-8"))
          )}",
             |"targetAbi": "${hexEncode(
            targetAbi.asJson.noSpaces.getBytes(Charset.forName("UTF-8"))
          )}",
             |"targetAddr": "$targetAddr"
        }""".stripMargin
        ).getOrElse(throw new IllegalArgumentException("Not a json")).some
      ),
      SignerADT.Keys(keys)
    ).get

    (debotAddr, targetAddr, keys)
  }

  behavior.of("DebotModule")

  // TODO implement it
  it should "be executed correctly" ignore {
    val (debotAddr, targetAddr, keys) = initDebot
    val res = debotModule.start(debotAddr, null).get
    logger.info(res.toString)

    assert(true)
  }
}
