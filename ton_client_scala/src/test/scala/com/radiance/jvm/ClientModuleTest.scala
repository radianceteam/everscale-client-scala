package com.radiance.jvm

import com.radiance.jvm.client.ClientModule
import com.typesafe.scalalogging.Logger
import org.scalatest.flatspec.AnyFlatSpec

class ClientModuleTest extends AnyFlatSpec with TestBase {

  private val logger = Logger[ClientModuleTest]

  override def init(): Unit = {
    super.init()
    clientModule = new ClientModule(ctx)
  }

  behavior.of("ClientModule")

  it should "return correct version and buildInfo" in {
    val res = clientModule.version.get
    logger.info(s"Version: ${res.version}")
    val buildInfo = clientModule.buildInfo.get
    logger.info(s"BuildNumber: ${buildInfo.build_number}")
    val apiReference = clientModule.getApiReference.get.api.spaces2
    assert(res.version.nonEmpty && apiReference.nonEmpty)
  }
}
