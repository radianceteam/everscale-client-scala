package com.radiance.scala.tonclient.processing.api

import com.radiance.scala.tonclient.Api
import com.radiance.scala.tonclient.types.in.{CallSet, DeploySet}
import com.radiance.scala.tonclient.types.out.ResultOfProcessMessage
import io.circe._
import io.circe.derivation._

private[processing] case class ProcessMessage(
                                                   abi: String,
                                                   address: String,
                                                   deploy_set: DeploySet,
                                                   call_set: CallSet,
                                                   signer: String,
                                                   processing_try_index: Long,
                                                   send_events: Boolean
                                                 ) extends Api {
  override val functionName: String = "processing.process_message"
  override type Out = ResultOfProcessMessage
  override val decoder: Decoder[ResultOfProcessMessage] = implicitly[Decoder[ResultOfProcessMessage]]
}

private[processing] object ProcessMessage {
  implicit val processMessageArgsEncoder: Encoder[ProcessMessage] = deriveEncoder[ProcessMessage]
}
