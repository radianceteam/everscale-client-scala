package com.radiance.scala.tonclient.types.both

import io.circe._
import io.circe.derivation._

/**
 * The ABI function header.<p> Includes several hidden function parameters that contract uses for security and replay protection reasons.<p> The actual set of header fields depends on the contract's ABI.
 */
case class FunctionHeader(
                           /**
                            * Message expiration time in seconds.
                            */
                           expire: Long,

                           /**
                            * Message creation time in milliseconds.
                            */
                           time: Long,

                           /**
                            * Public key used to sign message. Encoded with `hex`.
                            */
                           pubkey: String
                         )

object FunctionHeader {
  implicit val functionHeaderCodec: Codec[FunctionHeader] = deriveCodec[FunctionHeader]
}
