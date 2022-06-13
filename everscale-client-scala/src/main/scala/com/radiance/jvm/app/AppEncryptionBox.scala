package com.radiance.jvm.app

import com.radiance.jvm.crypto.{EncryptionBoxInfo, ParamsOfAppEncryptionBoxADT, ResultOfAppEncryptionBoxADT}

import scala.concurrent.ExecutionContext

abstract class AppEncryptionBox()(implicit
  ec: ExecutionContext
) extends AppObject[
      ParamsOfAppEncryptionBoxADT.ParamsOfAppEncryptionBox,
      ResultOfAppEncryptionBoxADT.ResultOfAppEncryptionBox
    ] {

  override protected val f
    : ParamsOfAppEncryptionBoxADT.ParamsOfAppEncryptionBox => ResultOfAppEncryptionBoxADT.ResultOfAppEncryptionBox = {

    case ParamsOfAppEncryptionBoxADT.Decrypt(data) =>
      ResultOfAppEncryptionBoxADT.Decrypt(decrypt(data))

    case ParamsOfAppEncryptionBoxADT.Encrypt(data) =>
      ResultOfAppEncryptionBoxADT.Encrypt(encrypt(data))

    case ParamsOfAppEncryptionBoxADT.GetInfo =>
      ResultOfAppEncryptionBoxADT.GetInfo(getInfo)
  }

  def getInfo: EncryptionBoxInfo

  def encrypt(data: String): String

  def decrypt(data: String): String
}
