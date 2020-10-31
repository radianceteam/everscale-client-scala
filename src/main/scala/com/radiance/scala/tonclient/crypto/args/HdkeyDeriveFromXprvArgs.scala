package com.radiance.scala.tonclient.crypto.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[crypto] case class HdkeyDeriveFromXprvArgs(
                                                    xprv: String,
                                                    child_index: Long,
                                                    hardened: Boolean
                                                  ) extends Args {
  override val functionName: String = "crypto.hdkey_derive_from_xprv"
  override val fieldName: Option[String] = Some("xprv")
}

private[crypto] object HdkeyDeriveFromXprvArgs {
  implicit val hdkeyDeriveFromXprvArgsEncoder: Encoder[HdkeyDeriveFromXprvArgs] = deriveEncoder[HdkeyDeriveFromXprvArgs ]
}
