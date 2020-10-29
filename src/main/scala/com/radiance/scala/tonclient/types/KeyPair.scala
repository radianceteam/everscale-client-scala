package com.radiance.scala.tonclient.types

import io.circe._
import io.circe.derivation._

case class KeyPair(
                    /**
                     * Public key - 64 symbols hex string
                     */
                    public: String,

                    /**
                     * Private key - u64 symbols hex string
                     */
                    secret: String
                  )

object KeyPair {
  implicit val keyPairCodec: Codec[KeyPair] = deriveCodec[KeyPair]
}
