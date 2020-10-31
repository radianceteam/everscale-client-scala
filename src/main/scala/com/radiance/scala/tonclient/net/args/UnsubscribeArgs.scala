package com.radiance.scala.tonclient.net.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[net] case class UnsubscribeArgs(handle: Long) extends Args {
  override val functionName: String = "net.unsubscribe"
  override val fieldName: Option[String] = Some("result")
}

private[net] object UnsubscribeArgs {
  implicit val unsubscribeArgsEncoder: Encoder[UnsubscribeArgs] = deriveCodec[UnsubscribeArgs]
}