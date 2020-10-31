package com.radiance.scala.tonclient.net.api

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.in.OrderBy
import io.circe._
import io.circe.derivation._

private[net] case class QueryCollection(
                                             collection: String,
                                             filter: String,
                                             result: String,
                                             order: OrderBy,
                                             limit: Long
                                           ) extends Args {
  override val functionName: String = "net.query_collection"
  override val fieldName: Option[String] = Some("result")
  override type Out = List[String]
  override val decoder: Decoder[List[String]] = implicitly[Decoder[List[String]]]
}

private[net] object QueryCollection {
  implicit val queryCollectionArgsEncoder: Encoder[QueryCollection] = deriveEncoder[QueryCollection]
}
