package com.radiance.scala.tonclient.processing.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.out.ResultOfProcessMessage
import io.circe._
import io.circe.derivation._

private[processing] case class WaitForTransaction(
                                                       abi: String,
                                                       message: String,
                                                       shard_block_id: String,
                                                       send_events: Boolean
                                                     ) extends Api {
  override val functionName: String = "processing.wait_for_transaction"
  override type Out = ResultOfProcessMessage
  override val decoder: Decoder[ResultOfProcessMessage] = implicitly[Decoder[ResultOfProcessMessage]]
}

private[processing] object WaitForTransaction {
  implicit val WaitForTransactionArgsEncoder: Encoder[WaitForTransaction] = deriveCodec[WaitForTransaction]
}
