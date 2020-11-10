package methods

import context.TonContextScala
import types.NetTypes._

import scala.concurrent.Future

class NetModule(private val ctx: TonContextScala) {
  type Value = String
  type Request = String
  /**
   *  Creates a subscription
   *
   *  Triggers for each insert/update of data
   *  that satisfies the `filter` conditions.
   *  The projection fields are limited to `result` fields.@param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param result  Projection (result) string
   * @param callback
   */
  def subscribe_collection(collection: String, filter: Option[Value], result: String, callback: Request): Future[Either[Throwable, ResultOfSubscribeCollection]] = {
    val arg = ParamsOfSubscribeCollection(collection, filter, result)
    ctx.execAsyncNew("net.subscribe_collection", arg)
  }
  /**
   *  Cancels a subscription
   *
   *  Cancels a subscription specified by its handle.@param handle  Subscription handle. Must be closed with `unsubscribe`
   */
  def unsubscribe(handle: Long): Future[Either[Throwable, Unit]] = {
    val arg = ParamsOfUnsubscribeCollection(handle)
    ctx.execAsyncNew("net.unsubscribe", arg)
  }
  /**
   *  Returns an object that fulfills the conditions or waits for its appearance
   *
   *  Triggers only once.
   *  If object that satisfies the `filter` conditions
   *  already exists - returns it immediately.
   *  If not - waits for insert/update of data within the specified `timeout`,
   *  and returns it.
   *  The projection fields are limited to `result` fields@param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param result  Projection (result) string
   * @param timeout  Query timeout
   */
  def wait_for_collection(collection: String, filter: Option[Value], result: String, timeout: Option[Long]): Future[Either[Throwable, ResultOfWaitForCollection]] = {
    val arg = ParamsOfWaitForCollection(collection, filter, result, timeout)
    ctx.execAsyncNew("net.wait_for_collection", arg)
  }
  /**
   *  Queries collection data
   *
   *  Queries data that satisfies the `filter` conditions,
   *  limits the number of returned records and orders them.
   *  The projection fields are limited to `result` fields@param collection  Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter  Collection filter
   * @param result  Projection (result) string
   * @param order  Sorting order
   * @param limit  Number of documents to return
   */
  def query_collection(collection: String, filter: Option[Value], result: String, order: Option[List[OrderBy]], limit: Option[Long]): Future[Either[Throwable, ResultOfQueryCollection]] ={
    val arg = ParamsOfQueryCollection(collection, filter, result, order, limit)
    ctx.execAsyncNew("net.query_collection", arg)
  }

}
