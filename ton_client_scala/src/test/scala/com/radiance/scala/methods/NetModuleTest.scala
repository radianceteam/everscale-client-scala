package com.radiance.scala.methods

import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

class NetModuleTest  extends AnyFlatSpec with ConfigTest {
  implicit val ec: ExecutionContext = ExecutionContext.global

  behavior.of("NetModule")
  it should "return the event" in {
    val res = net.subscribe_collection(
      "transactions",
      None,
      "id account_addr",
      e => println("Event: " + e.deepDropNullValues.spaces2)
    ).get
    println("Handle: " + res.handle)
    Thread.sleep(3000)
    net.unsubscribe(res.handle).get
  }
}
