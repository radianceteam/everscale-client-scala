package com.radiance.serde

import com.radiance.jvm.crypto.ParamsOfAppSigningBoxADT
import org.scalatest.flatspec.AnyFlatSpec
import io.circe.syntax._
import io.circe.parser._
import org.scalatest.matchers.should.Matchers._
import com.radiance.TestUtils
import com.radiance.jvm.abi.{AbiHandle, MessageBodyTypeEnum}
import com.radiance.jvm.net.AggregationFnEnum

class SerdeTest extends AnyFlatSpec with TestUtils {

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
