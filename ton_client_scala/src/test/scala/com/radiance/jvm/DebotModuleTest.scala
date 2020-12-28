package com.radiance.jvm

import com.radiance.jvm.debot.DebotModule
import org.scalatest.flatspec.AnyFlatSpec

class DebotModuleTest extends AnyFlatSpec with TestBase {

  override def init(): Unit = {
    ctx = Context(config)
    debotModule = new DebotModule(ctx)
  }


}
