package com.radiance.jvm

import com.radiance.jvm.client.ClientModule
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

class ClientModuleTest extends AnyFlatSpec with TestBase {

  override def init(): Unit = {
    super.init()
    clientModule = new ClientModule(ctx)
  }

  behavior.of("ClientModule")

  it should "return correct version and buildInfo" in {
    val res = clientModule.version.get
    println(s"Version: ${res.version}")
    val buildInfo = clientModule.buildInfo.get
    println(s"BuildNumber: ${buildInfo.build_number}")
    val apiReference = clientModule.getApiReference.get.api.spaces2
    println(apiReference)
    assert(res.version.nonEmpty && apiReference.nonEmpty)
  }
}
