package com.radiance.scala.tonclient.processing.args

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.in.{CallSet, DeploySet}
import io.circe._
import io.circe.derivation._

private[processing] case class ProcessMessageArgs(
                                                   abi: String,
                                                   address: String,
                                                   deploy_set: DeploySet,
                                                   call_set: CallSet,
                                                   signer: String,
                                                   processing_try_index: Long,
                                                   send_events: Boolean
                                                 ) extends Args {
  override val functionName: String = "processing.process_message"
}

private[processing] object ProcessMessageArgs {
  implicit val processMessageArgsEncoder: Encoder[ProcessMessageArgs] = deriveEncoder[ProcessMessageArgs]
}
