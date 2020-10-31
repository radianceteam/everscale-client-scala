package com.radiance.scala.tonclient.net.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[net] case class Unsubscribe(handle: Long) extends Args {
  override val functionName: String = "net.unsubscribe"
  override val fieldName: Option[String] = Some("result")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[net] object Unsubscribe {
  implicit val unsubscribeArgsEncoder: Encoder[Unsubscribe] = deriveCodec[Unsubscribe]
}