package com.radiance.jvm

import com.radiance.jvm.abi._
import com.radiance.jvm.crypto.{CryptoModule, KeyPair}
import com.radiance.jvm.debot.DebotModule
import org.scalatest.flatspec.AnyFlatSpec
import Utils._
import cats.implicits._
import com.radiance.jvm.processing.ProcessingModule
import io.circe.parser._
import io.circe.syntax._

import java.nio.charset.Charset

class DebotModuleTest extends AnyFlatSpec with TestBase {

  override def init(): Unit = {
    super.init()
    debotModule = new DebotModule(ctx)
    cryptoModule = new CryptoModule(ctx)
    abiModule = new AbiModule(ctx)
    processingModule = new ProcessingModule(ctx)
  }

  private def initDebot: (String, String, KeyPair) = {
    val keys = cryptoModule.generateRandomSignKeys.get

    val targetAbi = extractAbi(V2, "testDebotTarget.abi.json")
    val debotAbi = extractAbi(V2, "testDebot.abi.json")

    val targetAddr = deployWithGiver(
      targetAbi,
      DeploySet(extractTvc(V2, "testDebotTarget.tvc")),
      CallSet("constructor"),
      Signer.Keys(keys)
    ).get

    val debotAddr = deployWithGiver(
      debotAbi,
      DeploySet(extractTvc(V2, "testDebot.tvc")),
      CallSet(
        "constructor",
        None,
        parse(s"""{
          |"debotAbi": "${hexEncode(debotAbi.asJson.noSpaces.getBytes(Charset.forName("UTF-8")))}",
          |"targetAbi": "${hexEncode(targetAbi.asJson.noSpaces.getBytes(Charset.forName("UTF-8")))}",
          |"targetAddr": "$targetAddr"
        }""".stripMargin).getOrElse(throw new IllegalArgumentException("Not a json")).some
      ),
      Signer.Keys(keys)
    ).get

    (debotAddr, targetAddr, keys)
  }

  behavior.of("DebotModule")

  it should "be executed correctly" ignore {
    val (debotAddr, targetAddr, keys) = initDebot
    val res = debotModule.start(debotAddr, null).get
    println(res)

    assert(true)
  }
}
