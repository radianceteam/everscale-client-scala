package com.radiance.scala.modules

import com.radiance.scala.types.UtilsTypes
import com.radiance.scala.types.UtilsTypes._
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

class UtilsModuleTest extends AnyFlatSpec with ConfigTest {
  implicit val ec: ExecutionContext = ExecutionContext.global

  behavior.of("UtilsModule")

  it should "return correct value" in {
    val utilsModule = new UtilsModule(context)

    val result = utilsModule.convertAddress("ee65d170830136253ad8bd2116a28fcbd4ac462c6f222f49a1505d2fa7f7f528", Hex).get
    val result1 = utilsModule.convertAddress("ee65d170830136253ad8bd2116a28fcbd4ac462c6f222f49a1505d2fa7f7f528", AccountId).get
    val result2 = utilsModule.convertAddress(
      "ee65d170830136253ad8bd2116a28fcbd4ac462c6f222f49a1505d2fa7f7f528",
      Base64(true, true, true)
    ).get

    assert(
      result.address == "0:ee65d170830136253ad8bd2116a28fcbd4ac462c6f222f49a1505d2fa7f7f528" &&
      result1.address == "ee65d170830136253ad8bd2116a28fcbd4ac462c6f222f49a1505d2fa7f7f528" &&
      result2.address == "kQDuZdFwgwE2JTrYvSEWoo_L1KxGLG8iL0mhUF0vp_f1KGjN"
    )
  }
}
