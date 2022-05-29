package com.radiance.jvm.app

import com.radiance.jvm.crypto.{ParamsOfAppSigningBoxADT, ResultOfAppSigningBoxADT}

import scala.concurrent.ExecutionContext

abstract class AppSigningBox()(implicit
  ec: ExecutionContext
) extends AppObject[ParamsOfAppSigningBoxADT.ParamsOfAppSigningBox, ResultOfAppSigningBoxADT.ResultOfAppSigningBox] {
  protected val f: ParamsOfAppSigningBoxADT.ParamsOfAppSigningBox => ResultOfAppSigningBoxADT.ResultOfAppSigningBox = {
    case ParamsOfAppSigningBoxADT.GetPublicKey   =>
      ResultOfAppSigningBoxADT.GetPublicKey(getPublicKey)
    case ParamsOfAppSigningBoxADT.Sign(unsigned) =>
      ResultOfAppSigningBoxADT.Sign(sign(unsigned))
  }

  def getPublicKey: String

  def sign(unsigned: String): String
}
