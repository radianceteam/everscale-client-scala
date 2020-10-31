package com.radiance.scala.tonclient.net.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[net] case class SubscribeCollection(
                                                 collection: String,
                                                 filter: String,
                                                 result: String
                                               ) extends Args {
  override val functionName: String = "net.subscribe_collection"
  override val fieldName: Option[String] = Some("result")
  override type Out = Long
  override val decoder: Decoder[Long] = implicitly[Decoder[Long]]
}

private[net] object SubscribeCollection {
  implicit val subscribeCollectionArgsEncoder: Encoder[SubscribeCollection] = deriveCodec[SubscribeCollection]
}
