package com.radiance.scala.tonclient.net.api

import com.radiance.scala.tonclient.Api
import io.circe.{Decoder, Encoder}
import io.circe.derivation.deriveCodec

private[net] case class WaitForCollection(
                                               collection: String,
                                               filter: String,
                                               result: String,
                                               timeout: Long
                                             ) extends Api {
  override val functionName: String = "net.wait_for_collection"
  override val fieldName: Option[String] = Some("result")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private[net] object WaitForCollection {
  implicit val waitForCollectionArgsEncoder: Encoder[WaitForCollection] = deriveCodec[WaitForCollection]
}