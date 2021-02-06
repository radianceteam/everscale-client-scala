package com.radiance.jvm.net

import com.radiance.jvm._
import scala.concurrent.Future

class NetModule(private val ctx: Context) {

  // TODO add test
  /**
   * Aggregates collection data. Aggregates values from the specified `fields` for records that satisfies the `filter`
   * conditions,
   * @param collection
   *   collection
   * @param filter
   *   filter
   * @param fields
   *   fields
   */
  def aggregateCollection(
    collection: String,
    filter: Option[Value],
    fields: Option[List[FieldAggregation]]
  ): Future[Either[Throwable, ResultOfAggregateCollection]] = {
    val arg = ParamsOfAggregateCollection(collection, filter, fields)
    ctx.execAsync[ParamsOfAggregateCollection, ResultOfAggregateCollection]("net.aggregate_collection", arg)
  }

  // TODO add test
  /**
   * Performs multiple queries per single fetch.
   * @param operations
   *   operations
   */
  def batchQuery(
    operations: List[ParamsOfQueryOperation]
  ): Future[Either[Throwable, ResultOfBatchQuery]] = {
    val arg = ParamsOfBatchQuery(operations)
    ctx.execAsync[ParamsOfBatchQuery, ResultOfBatchQuery]("net.batch_query", arg)
  }

  /**
   * Requests the list of alternative endpoints from server
   */
  def fetchEndpoints(): Future[Either[Throwable, EndpointsSet]] = {
    ctx.execAsync[Unit, EndpointsSet]("net.fetch_endpoints", ())
  }

  /**
   * Returns ID of the last block in a specified account shard
   * @param address
   *   address
   */
  def findLastShardBlock(
    address: String
  ): Future[Either[Throwable, ResultOfFindLastShardBlock]] = {
    val arg = ParamsOfFindLastShardBlock(address)
    ctx.execAsync[ParamsOfFindLastShardBlock, ResultOfFindLastShardBlock]("net.find_last_shard_block", arg)
  }

  /**
   * @param query
   *   query
   * @param variables
   *   Must be a map with named values that can be used in query.
   */
  def query(
    query: String,
    variables: Option[Value]
  ): Future[Either[Throwable, ResultOfQuery]] = {
    val arg = ParamsOfQuery(query, variables)
    ctx.execAsync[ParamsOfQuery, ResultOfQuery]("net.query", arg)
  }

  /**
   * Queries collection data
   *
   * Queries data that satisfies the `filter` conditions, limits the number of returned records and orders them. The
   * projection fields are limited to `result` fields
   * @param collection
   *   Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter
   *   Collection filter
   * @param result
   *   Projection (result) string
   * @param order
   *   Sorting order
   * @param limit
   *   Number of documents to return
   */
  def queryCollection(
    collection: String,
    filter: Option[Value],
    result: String,
    order: Option[List[OrderBy]],
    limit: Option[Long]
  ): Future[Either[Throwable, ResultOfQueryCollection]] = {
    val arg = ParamsOfQueryCollection(collection, filter, result, order, limit)
    ctx.execAsync[ParamsOfQueryCollection, ResultOfQueryCollection]("net.query_collection", arg)
  }

  /**
   * Resumes network module to enable network activity
   */
  def resume(): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[Unit, Unit]("net.resume", ())
  }

  /**
   * Sets the list of endpoints to use on reinit
   * @param endpoints
   *   endpoints
   */
  def setEndpoints(endpoints: List[String]): Future[Either[Throwable, Unit]] = {
    val arg = EndpointsSet(endpoints)
    ctx.execAsync[EndpointsSet, Unit]("net.set_endpoints", arg)
  }

  /**
   * Creates a subscription
   *
   * Triggers for each insert/update of data that satisfies the `filter` conditions. The projection fields are limited
   * to `result` fields.
   * @param collection
   *   Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter
   *   Collection filter
   * @param result
   *   Projection (result) string
   * @param callback
   *   Callback io.circe.Json => Unit
   */
  def subscribeCollection(
    collection: String,
    filter: Option[Value],
    result: String,
    callback: Request
  ): Future[Either[Throwable, ResultOfSubscribeCollection]] = {
    val arg = ParamsOfSubscribeCollection(collection, filter, result)
    ctx.execAsyncWithCallback[ParamsOfSubscribeCollection, ResultOfSubscribeCollection](
      "net.subscribe_collection",
      arg,
      callback
    )
  }

  /**
   * Suspends network module to stop any network activity
   */
  def suspend(): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[Unit, Unit]("net.suspend", ())
  }

  /**
   * Cancels a subscription
   *
   * Cancels a subscription specified by its handle.
   * @param handle
   *   Subscription handle. Must be closed with `unsubscribe`
   */
  def unsubscribe(handle: Long): Future[Either[Throwable, Unit]] = {
    val arg = ResultOfSubscribeCollection(handle)
    ctx.execAsync[ResultOfSubscribeCollection, Unit]("net.unsubscribe", arg)
  }

  /**
   * Returns an object that fulfills the conditions or waits for its appearance
   *
   * Triggers only once. If object that satisfies the `filter` conditions already exists - returns it immediately. If
   * not - waits for insert/update of data within the specified `timeout`, and returns it. The projection fields are
   * limited to `result` fields
   * @param collection
   *   Collection name (accounts, blocks, transactions, messages, block_signatures)
   * @param filter
   *   Collection filter
   * @param result
   *   Projection (result) string
   * @param timeout
   *   Query timeout
   */
  def waitForCollection(
    collection: String,
    filter: Option[Value],
    result: String,
    timeout: Option[Long]
  ): Future[Either[Throwable, ResultOfWaitForCollection]] = {
    val arg = ParamsOfWaitForCollection(collection, filter, result, timeout)
    ctx.execAsync[ParamsOfWaitForCollection, ResultOfWaitForCollection]("net.wait_for_collection", arg)
  }

}
