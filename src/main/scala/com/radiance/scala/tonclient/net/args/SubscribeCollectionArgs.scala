package com.radiance.scala.tonclient.net.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private[net] case class SubscribeCollectionArgs(
                                                 collection: String,
                                                 filter: String,
                                                 result: String
                                               ) extends Args {
  override val functionName: String = "net.subscribe_collection"
  override val fieldName: Option[String] = Some("result")
}

private[net] object SubscribeCollectionArgs {
  implicit val subscribeCollectionArgsEncoder: Encoder[SubscribeCollectionArgs] = deriveCodec[SubscribeCollectionArgs]
}
