package com.radiance.jvm

import com.radiance.jvm.abi.{CallSet, DeploySet, FunctionHeader, ParamsOfEncodeMessage, Signer}
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.{Await, ExecutionContext}
import cats.implicits._
import io.circe.Json
import io.circe.parser.parse

import scala.concurrent.duration._

class ProcessingModuleTest extends AnyFlatSpec with TestBase {
  implicit val ec: ExecutionContext = ExecutionContext.global

  behavior.of("ProcessingModule")

  it should "wait for message" in {
    val keys = cryptoModule.generateRandomSignKeys.get
    val encoded = abiModule.encodeMessage(
      eventsAbiV2,
      None,
      DeploySet(eventsTvcV2).some,
      CallSet(
        "constructor",
        FunctionHeader(None, None, keys.public.some).some
      ).some,
      Signer.Keys(keys),
      None
    ).get

    println(s"Address: ${encoded.address}")

    val inputMsg: Json = parse(s"""{
                                  |  "dest": "${encoded.address}",
                                  |  "amount": 500000000
                                  |}""".stripMargin
    ).getOrElse(throw new IllegalArgumentException("Not a Json"))

    processingModule.processMessage(
      ParamsOfEncodeMessage(
        giverAbiV1,
        encoded.address.some,
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

    val f = getGramsFromGiver(encoded.address)
    val res = Await.result(f, 1.minute)
    println(res)
    assert(true)
  }
}
