package com.radiance.scala.tonclient.net

import com.radiance.scala.tonclient.TonContext
import com.radiance.scala.tonclient.net.api.{QueryCollection, SubscribeCollection, Unsubscribe, WaitForCollection}
import com.radiance.scala.tonclient.types.in.OrderBy

import scala.concurrent.{ExecutionContext, Future}


/**
 * Network access.
 */
class Net(val context: TonContext)(implicit val ec: ExecutionContext) {

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
                       limit: Long
                     ): Future[Either[Throwable, List[String]]] = context.exec(
    QueryCollection(collection, filter, result, order, limit)
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
                         ): Future[Either[Throwable, Long]] = context.exec(
    SubscribeCollection(collection, filter, result)
  )


  /**
   * Cancels a subscription<p> Cancels a subscription specified by its handle.
   *
   * @param handle Subscription handle. Must be closed with `unsubscribe`
   */
  def unsubscribe(handle: Long): Future[Either[Throwable, String]] = context.exec(
    Unsubscribe(handle: Long)
  )

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
                         timeout: Long
                       ): Future[Either[Throwable, String]] = context.exec(
    WaitForCollection(collection, filter, result, timeout)
  )

}

