package com.radiance.scala.tonclient

import com.radiance.scala.tonclient.types.OrderBy

import scala.concurrent.{ExecutionContext, Future}


/**
 * Network access.
 */
class Net(val context: TONContext)(implicit val ec: ExecutionContext) {
  /**
   * Queries collection data<p> Queries data that satisfies the `filter` conditions,  limits the number of returned records and orders them. The projection fields are limited to  `result` fields
   *
   * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter     Collection filter
   * @param result     Projection (result) string
   * @param order      Sorting order
   * @param limit      Number of documents to return
   */
  def queryCollection(
                       collection: String,
                       filter: String,
                       result: String,
                       order: String,
                       limit: Number
                     ): Future[List[String]] =
    context.requestField[List[String]](
      "net.query_collection",
      s"""{
         |"collection":"$collection",
         |"filter":"$filter",
         |"result":"$result",
         |"order":"$order",
         |"limit":$limit
         |}""".stripMargin
    )("result")


  /**
   * Queries collection data<p> Queries data that satisfies the `filter` conditions, limits the number of returned records and orders them. The projection fields are limited to  `result` fields
   *
   * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter     Collection filter
   * @param result     Projection (result) string
   * @param order      Sorting order
   * @param limit      Number of documents to return
   */
  def queryCollection(
                       collection: String,
                       filter: String,
                       result: String,
                       order: OrderBy,
                       limit: Number
                     ): Future[List[String]] =
    context.requestField[List[String]](
      "net.query_collection",
      s"""{
         |"collection":"$collection",
         |"filter":"$filter",
         |"result":"$result",
         |"order":${order.toString},
         |"limit":$limit
         |}""".stripMargin
    )("result")

  /**
   * Returns an object that fulfills the conditions or waits for its appearance<p> Triggers only once.  If object that satisfies the `filter` conditions  already exists - returns it immediately.  If not - waits for insert/update of data withing the specified `timeout`, and returns it.  The projection fields are limited to  `result` fields
   *
   * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter     Collection filter
   * @param result     Projection (result) string
   * @param timeout    Query timeout
   */
  def waitForCollection(
                         collection: String,
                         filter: String,
                         result: String,
                         timeout: Number
                       ): Future[String] =
    context.requestField[String](
      "net.wait_for_collection",
      s"""{
         |"collection":"$collection",
         |"filter":"$filter",
         |"result":"$result",
         |"timeout":$timeout
         |}""".stripMargin
    )("result")

  /**
   * Cancels a subscription<p> Cancels a subscription specified by its handle.
   *
   * @param handle Subscription handle. Must be closed with `unsubscribe`
   */
  def unsubscribe(handle: Number): Future[String] =
    context.request(
      "net.unsubscribe",
      s"""{"handle":$handle}"""
    )

  /**
   * Creates a subscription<p> Triggers for each insert/update of data that satisfies the `filter` conditions. The projection fields are limited to  `result` fields.
   *
   * @param collection Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter     Collection filter
   * @param result     Projection (result) string
   */
  def subscribeCollection(
                           collection: String,
                           filter: String,
                           result: String
                         ): Future[Long] =
    context.requestField[Long](
      "net.subscribe_collection",
      s"""{
         |"collection":"$collection",
         |"filter":"$filter",
         |"result":"$result"
         |}""".stripMargin
    )("result")
}

