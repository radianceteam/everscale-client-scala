package com.radiance.serde

import com.radiance.jvm.crypto.ParamsOfAppSigningBoxADT
import com.radiance.jvm.debot.DebotAction
import org.scalatest.flatspec.AnyFlatSpec
import io.circe.syntax._
import io.circe.parser._
import org.scalatest.matchers.should.Matchers._
import com.radiance.TestUtils
import com.radiance.jvm.abi.{AbiHandle, MessageBodyTypeEnum}
import com.radiance.jvm.debot.ParamsOfAppDebotBrowserADT
import com.radiance.jvm.net.AggregationFnEnum

class SerdeTest extends AnyFlatSpec with TestUtils {

  "ParamsOfAppDebotBrowser" should "serialize correctly" in {

    val a: ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser =
      ParamsOfAppDebotBrowserADT.Log("test")
    val b: ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser =
      ParamsOfAppDebotBrowserADT.Send("test")
    val c: ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser =
      ParamsOfAppDebotBrowserADT.Switch(5L)
    val d: ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser =
      ParamsOfAppDebotBrowserADT.ShowAction(
        DebotAction("description", "name", 1L, 2L, "attributes", "misc")
      )
    val e: ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser =
      ParamsOfAppDebotBrowserADT.Input("test")
    val f: ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser =
      ParamsOfAppDebotBrowserADT.GetSigningBox
    val g: ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser =
      ParamsOfAppDebotBrowserADT.InvokeDebot(
        "debot_addr",
        DebotAction("description", "name", 1L, 2L, "attributes", "misc")
      )
    val h: ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser =
      ParamsOfAppDebotBrowserADT.SwitchCompleted
    import ParamsOfAppDebotBrowserADT._

    a.asJson.noSpaces shouldBe """{"msg":"test","type":"Log"}"""
    b.asJson.noSpaces shouldBe """{"message":"test","type":"Send"}"""
    c.asJson.noSpaces shouldBe """{"context_id":5,"type":"Switch"}"""
    d.asJson.noSpaces shouldBe """{"action":{"description":"description","name":"name","action_type":1,"to":2,"attributes":"attributes","misc":"misc"},"type":"ShowAction"}"""
    e.asJson.noSpaces shouldBe """{"prompt":"test","type":"Input"}"""
    f.asJson.noSpaces shouldBe """{"type":"GetSigningBox"}"""
    g.asJson.noSpaces shouldBe """{"debot_addr":"debot_addr","action":{"description":"description","name":"name","action_type":1,"to":2,"attributes":"attributes","misc":"misc"},"type":"InvokeDebot"}"""
    h.asJson.noSpaces shouldBe """{"type":"SwitchCompleted"}"""
  }

  "AggregationFn" should "serialize correctly" in {
    val a: AggregationFnEnum.AggregationFn = AggregationFnEnum.AVERAGE
    val b: AggregationFnEnum.AggregationFn = AggregationFnEnum.COUNT
    val c: AggregationFnEnum.AggregationFn = AggregationFnEnum.MAX
    val d: AggregationFnEnum.AggregationFn = AggregationFnEnum.MIN
    val e: AggregationFnEnum.AggregationFn = AggregationFnEnum.SUM

    a.asJson.noSpaces shouldBe """"AVERAGE""""
    b.asJson.noSpaces shouldBe """"COUNT""""
    c.asJson.noSpaces shouldBe """"MAX""""
    d.asJson.noSpaces shouldBe """"MIN""""
    e.asJson.noSpaces shouldBe """"SUM""""
  }

  "ParamsOfAppSigningBox" should "deserialize correctly" in {
    val a: ParamsOfAppSigningBoxADT.ParamsOfAppSigningBox =
      parse("""{"type":"GetPublicKey"}""")
        .flatMap(_.as[ParamsOfAppSigningBoxADT.ParamsOfAppSigningBox])
        .get
    val b: ParamsOfAppSigningBoxADT.ParamsOfAppSigningBox =
      parse("""{"type":"Sign","unsigned":"str"}""")
        .flatMap(_.as[ParamsOfAppSigningBoxADT.ParamsOfAppSigningBox])
        .get
    a shouldBe ParamsOfAppSigningBoxADT.GetPublicKey
    b shouldBe ParamsOfAppSigningBoxADT.Sign("str")
  }

  "MessageBodyType" should "deserialize correctly" in {
    val a: MessageBodyTypeEnum.MessageBodyType = parse(""""Input"""")
      .flatMap(_.as[MessageBodyTypeEnum.MessageBodyType])
      .get
    val b: MessageBodyTypeEnum.MessageBodyType = parse(""""Output"""")
      .flatMap(_.as[MessageBodyTypeEnum.MessageBodyType])
      .get
    val c: MessageBodyTypeEnum.MessageBodyType = parse(""""InternalOutput"""")
      .flatMap(_.as[MessageBodyTypeEnum.MessageBodyType])
      .get
    val d: MessageBodyTypeEnum.MessageBodyType = parse(""""Event"""")
      .flatMap(_.as[MessageBodyTypeEnum.MessageBodyType])
      .get

    a shouldBe MessageBodyTypeEnum.Input
    b shouldBe MessageBodyTypeEnum.Output
    c shouldBe MessageBodyTypeEnum.InternalOutput
    d shouldBe MessageBodyTypeEnum.Event
  }

  "AbiHandle" should "serialize and deserialize correctly" in {
    val handle: AbiHandle = AbiHandle(1)
    val json = handle.asJson.noSpaces
    val handleRestored = parse(json).flatMap(_.as[AbiHandle]).get
    println(handleRestored)
    json shouldBe "1"
    handleRestored shouldBe AbiHandle(1)
  }
}
