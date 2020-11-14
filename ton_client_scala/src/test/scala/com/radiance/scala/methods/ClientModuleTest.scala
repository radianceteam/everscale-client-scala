package com.radiance.scala.methods

import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

class ClientModuleTest  extends AnyFlatSpec with ConfigTest {
  implicit val ec: ExecutionContext = ExecutionContext.global

  behavior.of("ClientModule")

  it should "return correct version and buildInfo" in {
    val client = new ClientModule(context)
    val res = client.version().get
    println(s"Version: ${res.version}")
    val buildInfo = client.build_info().get
    println(s"BuildNumber: ${buildInfo.build_number}")
    assert(res.version == "1.1.0" && buildInfo.dependencies.nonEmpty)
  }
}
