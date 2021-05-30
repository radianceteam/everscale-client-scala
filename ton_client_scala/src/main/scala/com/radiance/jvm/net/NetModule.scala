package com.radiance.jvm.net

import com.radiance.jvm._
import com.radiance.jvm.abi.AbiADT

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
    ctx.execAsync[ParamsOfAggregateCollection, ResultOfAggregateCollection](
      "net.aggregate_collection",
      ParamsOfAggregateCollection(collection, filter, fields)
    )
  }

  // TODO add test
  /**
   * Performs multiple queries per single fetch.
   * @param operations
   *   operations
   */
  def batchQuery(
    operations: List[ParamsOfQueryOperationADT.ParamsOfQueryOperation]
  ): Future[Either[Throwable, ResultOfBatchQuery]] = {
    ctx.execAsync[ParamsOfBatchQuery, ResultOfBatchQuery]("net.batch_query", ParamsOfBatchQuery(operations))
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
    ctx.execAsync[ParamsOfFindLastShardBlock, ResultOfFindLastShardBlock](
      "net.find_last_shard_block",
      ParamsOfFindLastShardBlock(address)
    )
  }

  /**
   * Requests the list of alternative endpoints from server
   */
  def getEndpoints(): Future[Either[Throwable, ResultOfGetEndpoints]] = {
    ctx.execAsync[Unit, ResultOfGetEndpoints]("net.fetch_endpoints", ())
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
    ctx.execAsync[ParamsOfQuery, ResultOfQuery]("net.query", ParamsOfQuery(query, variables))
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
    ctx.execAsync[ParamsOfQueryCollection, ResultOfQueryCollection](
      "net.query_collection",
      ParamsOfQueryCollection(collection, filter, result, order, limit)
    )
  }

  /**
   * Allows to query and paginate through the list of accounts that the specified account has interacted with, sorted by
   * the time of the last internal message between accounts *Attention* this query retrieves data from 'Counterparties'
   * service which is not supported in the opensource version of DApp Server (and will not be supported) as well as in
   * TON OS SE (will be supported in SE in future), but is always accessible via [TON OS Devnet/Mainnet
   * Clouds](https://docs.ton.dev/86757ecb2/p/85c869-networks)
   * @param account
   *   account
   * @param result
   *   result
   * @param first
   *   first
   * @param after
   *   after
   */
  def queryCounterparties(
    account: String,
    result: String,
    first: Option[Long],
    after: Option[String]
  ): Future[Either[Throwable, ResultOfQueryCollection]] = {
    ctx.execAsync[ParamsOfQueryCounterparties, ResultOfQueryCollection](
      "net.query_counterparties",
      ParamsOfQueryCounterparties(account, result, first, after)
    )
  }

  /**
   * Returns transactions tree for specific message. Performs recursive retrieval of the transactions tree produced by
   * the specific message: in_msg -> dst_transaction -> out_messages -> dst_transaction -> ...
   *
   * All retrieved messages and transactions will be included into `result.messages` and `result.transactions`
   * respectively.
   *
   * The retrieval process will stop when the retrieved transaction count is more than 50.
   *
   * It is guaranteed that each message in `result.messages` has the corresponding transaction in the
   * `result.transactions`.
   *
   * But there are no guaranties that all messages from transactions `out_msgs` are presented in `result.messages`. So
   * the application have to continue retrieval for missing messages if it requires.
   * @param in_msg
   *   in_msg
   * @param abi_registry
   *   abi_registry
   */
  def queryTransactionTree(
    in_msg: String,
    abi_registry: Option[List[AbiADT.Abi]]
  ): Future[Either[Throwable, ResultOfQueryTransactionTree]] = {
    ctx.execAsync[ParamsOfQueryTransactionTree, ResultOfQueryTransactionTree](
      "net.query_transaction_tree",
      ParamsOfQueryTransactionTree(
        in_msg,
        abi_registry
      )
    )
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
    ctx.execAsync[EndpointsSet, Unit]("net.set_endpoints", EndpointsSet(endpoints))
  }

  /**
   * Creates a subscription Triggers for each insert/update of data that satisfies the `filter` conditions. The
   * projection fields are limited to `result` fields.
   *
   * The subscription is a persistent communication channel between client and Free TON Network. All changes in the
   * blockchain will be reflected in realtime. Changes means inserts and updates of the blockchain entities.
   *
   * ### Important Notes on Subscriptions
   *
   * Unfortunately sometimes the connection with the network brakes down. In this situation the library attempts to
   * reconnect to the network. This reconnection sequence can take significant time. All of this time the client is
   * disconnected from the network.
   *
   * Bad news is that all blockchain changes that happened while the client was disconnected are lost.
   *
   * Good news is that the client report errors to the callback when it loses and resumes connection.
   *
   * So, if the lost changes are important to the application then the application must handle these error reports.
   *
   * Library reports errors with `responseType` == 101 and the error object passed via `params`.
   *
   * When the library has successfully reconnected the application receives callback with `responseType` == 101 and
   * `params.code` == 614 (NetworkModuleResumed).
   *
   * Application can use several ways to handle this situation:
   *   - If application monitors changes for the single blockchain object (for example specific account): application
   *     can perform a query for this object and handle actual data as a regular data from the subscription.
   *   - If application monitors sequence of some blockchain objects (for example transactions of the specific account):
   *     application must refresh all cached (or visible to user) lists where this sequences presents.
   * @param collection
   *   collection
   * @param filter
   *   filter
   * @param result
   *   result
   * @param callback
   *   callback
   */
  def subscribeCollection(
    collection: String,
    filter: Option[Value],
    result: String,
    callback: Request
  ): Future[Either[Throwable, ResultOfSubscribeCollection]] = {
    ctx.execAsyncWithCallback[ParamsOfSubscribeCollection, ResultOfSubscribeCollection](
      "net.subscribe_collection",
      ParamsOfSubscribeCollection(collection, filter, result),
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
    ctx.execAsync[ResultOfSubscribeCollection, Unit]("net.unsubscribe", ResultOfSubscribeCollection(handle))
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
    ctx.execAsync[ParamsOfWaitForCollection, ResultOfWaitForCollection](
      "net.wait_for_collection",
      ParamsOfWaitForCollection(collection, filter, result, timeout)
    )
  }

}
