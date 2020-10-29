package com.radiance.scala.tonclient.types

import com.radiance.tonclient.DecodedOutput
import io.circe._
import io.circe.derivation._

case class ResultOfRunExecutor(
                                /**
                                 * Parsed transaction.<p> In addition to the regular transaction fields
                                 * there is a `boc` field encoded with `base64` which contains source transaction BOC.
                                 */
                                transaction: String,

                                /**
                                 * List of output messages' BOCs. Encoded as `base64`
                                 */
                                out_messages: String,

                                /**
                                 * Optional decoded message bodies according to the optional `abi` parameter.
                                 */
                                decoded: DecodedOutput,

                                /**
                                 * Updated account state BOC. Encoded as `base64`
                                 */
                                account: String,

                                /**
                                 * Transaction fees
                                 */
                                fees: String
                              )

object ResultOfRunExecutor {
  implicit val resultOfRunExecutorCodec: Codec[ResultOfRunExecutor] = deriveCodec[ResultOfRunExecutor]
}


