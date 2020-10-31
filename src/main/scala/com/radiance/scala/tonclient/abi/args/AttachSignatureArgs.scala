package com.radiance.scala.tonclient.abi.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private [abi] case class AttachSignatureArgs(
                                             abi: String,
                                             public_key: String,
                                             message: String,
                                             signature: String
                                           ) extends Args {
  val functionName: String = "abi.attach_signature"
}

private [abi] object AttachSignatureArgs {
  implicit val attachSignatureArgsEncoder: Encoder[AttachSignatureArgs] = deriveCodec[AttachSignatureArgs]
}


