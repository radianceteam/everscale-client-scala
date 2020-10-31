package com.radiance.scala.tonclient.net.args

import com.radiance.scala.tonclient.Args
import com.radiance.scala.tonclient.types.in.OrderBy
import io.circe._
import io.circe.derivation._

private[net] case class QueryCollectionArgs(
                                             collection: String,
                                             filter: String,
                                             result: String,
                                             order: OrderBy,
                                             limit: Long
                                           ) extends Args {
  override val functionName: String = "net.query_collection"
  override val fieldName: Option[String] = Some("result")
}

private[net] object QueryCollectionArgs {
  implicit val queryCollectionArgsEncoder: Encoder[QueryCollectionArgs] = deriveEncoder[QueryCollectionArgs]
}
