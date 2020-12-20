package com.radiance.jvm

import com.radiance.jvm.client.ClientModule
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

class ClientModuleTest  extends AnyFlatSpec with ConfigTest {
  implicit val ec: ExecutionContext = ExecutionContext.global

  behavior.of("ClientModule")

  it should "return correct version and buildInfo" in {
    val client = new ClientModule(context)
    val res = client.version.get
    println(s"Version: ${res.version}")
    val buildInfo = client.buildInfo.get
    println(s"BuildNumber: ${buildInfo.build_number}")
    val apiReference = client.getApiReference.get.api.spaces2
    println(apiReference)
    assert(res.version.nonEmpty && buildInfo.dependencies.nonEmpty && apiReference.nonEmpty)
  }
}
