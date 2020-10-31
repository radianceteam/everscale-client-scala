package com.radiance.scala.tonclient.processing.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[processing] case class WaitForTransactionArgs(
                                                       abi: String,
                                                       message: String,
                                                       shard_block_id: String,
                                                       send_events: Boolean
                                                     ) extends Args {
  override val functionName: String = "processing.wait_for_transaction"
}

private[processing] object WaitForTransactionArgs {
  implicit val WaitForTransactionArgsEncoder: Encoder[WaitForTransactionArgs] = deriveCodec[WaitForTransactionArgs]
}
