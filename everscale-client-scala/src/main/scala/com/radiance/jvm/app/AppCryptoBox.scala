package com.radiance.jvm.app

import com.radiance.jvm.crypto.{ParamsOfAppPasswordProviderADT, ResultOfAppPasswordProviderADT}

import scala.concurrent.ExecutionContext

abstract class AppCryptoBox()(implicit
  ec: ExecutionContext
) extends AppObject[
      ParamsOfAppPasswordProviderADT.ParamsOfAppPasswordProvider,
      ResultOfAppPasswordProviderADT.ResultOfAppPasswordProvider
    ] {
  override protected val f
    : ParamsOfAppPasswordProviderADT.ParamsOfAppPasswordProvider => ResultOfAppPasswordProviderADT.ResultOfAppPasswordProvider = {
    case ParamsOfAppPasswordProviderADT.GetPassword(encryption_public_key) =>
      val encrypted_password = getPassword(encryption_public_key)
      ResultOfAppPasswordProviderADT.GetPassword(encrypted_password, encryption_public_key)
  }

  def getPassword(pubKey: String): String
}
