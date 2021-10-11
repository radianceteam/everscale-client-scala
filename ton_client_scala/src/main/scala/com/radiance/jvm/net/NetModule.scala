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
   * Creates block iterator. Block iterator uses robust iteration methods that guaranties that every block in the
   * specified range isn't missed or iterated twice.
   *
   * Iterated range can be reduced with some filters:
   *   - `start_time` – the bottom time range. Only blocks with `gen_utime` more or equal to this value is iterated. If
   *     this parameter is omitted then there is no bottom time edge, so all blocks since zero state is iterated.
   *   - `end_time` – the upper time range. Only blocks with `gen_utime` less then this value is iterated. If this
   *     parameter is omitted then there is no upper time edge, so iterator never finishes.
   *   - `shard_filter` – workchains and shard prefixes that reduce the set of interesting blocks. Block conforms to the
   *     shard filter if it belongs to the filter workchain and the first bits of block's `shard` fields matches to the
   *     shard prefix. Only blocks with suitable shard are iterated.
   *
   * Items iterated is a JSON objects with block data. The minimal set of returned fields is: ```text id gen_utime
   * workchain_id shard after_split after_merge prev_ref { root_hash } prev_alt_ref { root_hash } ``` Application can
   * request additional fields in the `result` parameter.
   *
   * Application should call the `remove_iterator` when iterator is no longer required.
   * @param start_time
   *   If the application specifies this parameter then the iteration includes blocks with `gen_utime` >= `start_time`.
   *   Otherwise the iteration starts from zero state.
   *
   * Must be specified in seconds.
   * @param end_time
   *   If the application specifies this parameter then the iteration includes blocks with `gen_utime` < `end_time`.
   *   Otherwise the iteration never stops.
   *
   * Must be specified in seconds.
   * @param shard_filter
   *   If the application specifies this parameter and it is not the empty array then the iteration will include items
   *   related to accounts that belongs to the specified shard prefixes. Shard prefix must be represented as a string
   *   "workchain:prefix". Where `workchain` is a signed integer and the `prefix` if a hexadecimal representation if the
   *   64-bit unsigned integer with tagged shard prefix. For example: "0:3800000000000000".
   * @param result
   *   List of the fields that must be returned for iterated items. This field is the same as the `result` parameter of
   *   the `query_collection` function. Note that iterated items can contains additional fields that are not requested
   *   in the `result`.
   */
  def createBlockIterator(
    start_time: Option[Long],
    end_time: Option[Long],
    shard_filter: Option[List[String]],
    result: Option[String]
  ): Future[Either[Throwable, RegisteredIterator]] = {
    ctx.execAsync[ParamsOfCreateBlockIterator, RegisteredIterator](
      "net.create_block_iterator",
      ParamsOfCreateBlockIterator(
        start_time,
        end_time,
        shard_filter,
        result
      )
    )
  }

  /**
   * Creates transaction iterator. Transaction iterator uses robust iteration methods that guaranty that every
   * transaction in the specified range isn't missed or iterated twice.
   *
   * Iterated range can be reduced with some filters:
   *   - `start_time` – the bottom time range. Only transactions with `now` more or equal to this value are iterated. If
   *     this parameter is omitted then there is no bottom time edge, so all the transactions since zero state are
   *     iterated.
   *   - `end_time` – the upper time range. Only transactions with `now` less then this value are iterated. If this
   *     parameter is omitted then there is no upper time edge, so iterator never finishes.
   *   - `shard_filter` – workchains and shard prefixes that reduce the set of interesting accounts. Account address
   *     conforms to the shard filter if it belongs to the filter workchain and the first bits of address match to the
   *     shard prefix. Only transactions with suitable account addresses are iterated.
   *   - `accounts_filter` – set of account addresses whose transactions must be iterated. Note that accounts filter can
   *     conflict with shard filter so application must combine these filters carefully.
   *
   * Iterated item is a JSON objects with transaction data. The minimal set of returned fields is: ```text id
   * account_addr now balance_delta(format:DEC) bounce { bounce_type } in_message { id value(format:DEC) msg_type src }
   * out_messages { id value(format:DEC) msg_type dst } ``` Application can request an additional fields in the `result`
   * parameter.
   *
   * Another parameter that affects on the returned fields is the `include_transfers`. When this parameter is `true` the
   * iterator computes and adds `transfer` field containing list of the useful `TransactionTransfer` objects. Each
   * transfer is calculated from the particular message related to the transaction and has the following structure:
   *   - message – source message identifier.
   *   - isBounced – indicates that the transaction is bounced, which means the value will be returned back to the
   *     sender.
   *   - isDeposit – indicates that this transfer is the deposit (true) or withdraw (false).
   *   - counterparty – account address of the transfer source or destination depending on `isDeposit`.
   *   - value – amount of nano tokens transferred. The value is represented as a decimal string because the actual
   *     value can be more precise than the JSON number can represent. Application must use this string carefully –
   *     conversion to number can follow to loose of precision.
   *
   * Application should call the `remove_iterator` when iterator is no longer required.
   * @param start_time
   *   If the application specifies this parameter then the iteration includes blocks with `gen_utime` >= `start_time`.
   *   Otherwise the iteration starts from zero state.
   *
   * Must be specified in seconds.
   * @param end_time
   *   If the application specifies this parameter then the iteration includes blocks with `gen_utime` < `end_time`.
   *   Otherwise the iteration never stops.
   *
   * Must be specified in seconds.
   * @param shard_filter
   *   If the application specifies this parameter and it is not an empty array then the iteration will include items
   *   related to accounts that belongs to the specified shard prefixes. Shard prefix must be represented as a string
   *   "workchain:prefix". Where `workchain` is a signed integer and the `prefix` if a hexadecimal representation if the
   *   64-bit unsigned integer with tagged shard prefix. For example: "0:3800000000000000". Account address conforms to
   *   the shard filter if it belongs to the filter workchain and the first bits of address match to the shard prefix.
   *   Only transactions with suitable account addresses are iterated.
   * @param accounts_filter
   *   Application can specify the list of accounts for which it wants to iterate transactions.
   *
   * If this parameter is missing or an empty list then the library iterates transactions for all accounts that pass the
   * shard filter.
   *
   * Note that the library doesn't detect conflicts between the account filter and the shard filter if both are
   * specified. So it is an application responsibility to specify the correct filter combination.
   * @param result
   *   List of the fields that must be returned for iterated items. This field is the same as the `result` parameter of
   *   the `query_collection` function. Note that iterated items can contain additional fields that are not requested in
   *   the `result`.
   * @param include_transfers
   *   If this parameter is `true` then each transaction contains field `transfers` with list of transfer. See more
   *   about this structure in function description.
   */
  def createTransactionIterator(
    start_time: Option[Long],
    end_time: Option[Long],
    shard_filter: Option[List[String]],
    accounts_filter: Option[List[String]],
    result: Option[String],
    include_transfers: Option[Boolean]
  ): Future[Either[Throwable, RegisteredIterator]] = {
    ctx.execAsync[ParamsOfCreateTransactionIterator, RegisteredIterator](
      "net.create_transaction_iterator",
      ParamsOfCreateTransactionIterator(
        start_time,
        end_time,
        shard_filter,
        accounts_filter,
        result,
        include_transfers
      )
    )
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
   * Returns next available items. In addition to available items this function returns the `has_more` flag indicating
   * that the iterator isn't reach the end of the iterated range yet.
   *
   * This function can return the empty list of available items but indicates that there are more items is available.
   * This situation appears when the iterator doesn't reach iterated range but database doesn't contains available items
   * yet.
   *
   * If application requests resume state in `return_resume_state` parameter then this function returns `resume_state`
   * that can be used later to resume the iteration from the position after returned items.
   *
   * The structure of the items returned depends on the iterator used. See the description to the appropriated iterator
   * creation function.
   * @param iterator
   *   iterator
   * @param limit
   *   If value is missing or is less than 1 the library uses 1.
   * @param return_resume_state
   *   return_resume_state
   */
  def iteratorNext(
    iterator: Long,
    limit: Option[Long],
    return_resume_state: Option[Boolean]
  ): Future[Either[Throwable, ResultOfIteratorNext]] = {
    ctx.execAsync[ParamsOfIteratorNext, ResultOfIteratorNext](
      "net.iterator_next",
      ParamsOfIteratorNext(
        iterator,
        limit,
        return_resume_state
      )
    )
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
   * Returns a tree of transactions triggered by a specific message. Performs recursive retrieval of a transactions tree
   * produced by a specific message: in_msg -> dst_transaction -> out_messages -> dst_transaction -> ... If the chain of
   * transactions execution is in progress while the function is running, it will wait for the next transactions to
   * appear until the full tree or more than 50 transactions are received.
   *
   * All the retrieved messages and transactions are included into `result.messages` and `result.transactions`
   * respectively.
   *
   * Function reads transactions layer by layer, by pages of 20 transactions.
   *
   * The retrieval prosess goes like this: Let's assume we have an infinite chain of transactions and each transaction
   * generates 5 messages.
   *   1. Retrieve 1st message (input parameter) and corresponding transaction - put it into result. It is the first
   *      level of the tree of transactions - its root. Retrieve 5 out message ids from the transaction for next steps.
   *      2. Retrieve 5 messages and corresponding transactions on the 2nd layer. Put them into result. Retrieve 5*5 out
   *      message ids from these transactions for next steps 3. Retrieve 20 (size of the page) messages and transactions
   *      (3rd layer) and 20*5=100 message ids (4th layer). 4. Retrieve the last 5 messages and 5 transactions on the
   *      3rd layer + 15 messages and transactions (of 100) from the 4th layer + 25 message ids of the 4th layer + 75
   *      message ids of the 5th layer. 5. Retrieve 20 more messages and 20 more transactions of the 4th layer + 100
   *      more message ids of the 5th layer. 6. Now we have 1+5+20+20+20 = 66 transactions, which is more than 50.
   *      Function exits with the tree of 1m->1t->5m->5t->25m->25t->35m->35t. If we see any message ids in the last
   *      transactions out_msgs, which don't have corresponding messages in the function result, it means that the full
   *      tree was not received and we need to continue iteration.
   *
   * To summarize, it is guaranteed that each message in `result.messages` has the corresponding transaction in the
   * `result.transactions`. But there is no guarantee that all messages from transactions `out_msgs` are presented in
   * `result.messages`. So the application has to continue retrieval for missing messages if it requires.
   * @param in_msg
   *   in_msg
   * @param abi_registry
   *   abi_registry
   * @param timeout
   *   If some of the following messages and transactions are missing yet The maximum waiting time is regulated by this
   *   option.
   */
  def queryTransactionTree(
    in_msg: String,
    abi_registry: Option[List[AbiADT.Abi]],
    timeout: Option[Long]
  ): Future[Either[Throwable, ResultOfQueryTransactionTree]] = {
    ctx.execAsync[ParamsOfQueryTransactionTree, ResultOfQueryTransactionTree](
      "net.query_transaction_tree",
      ParamsOfQueryTransactionTree(
        in_msg,
        abi_registry,
        timeout
      )
    )
  }

  /**
   * Removes an iterator Frees all resources allocated in library to serve iterator.
   *
   * Application always should call the `remove_iterator` when iterator is no longer required.
   * @param handle
   *   Must be removed using `remove_iterator` when it is no more needed for the application.
   */
  def removeIterator(handle: Long): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[RegisteredIterator, Unit](
      "net.remove_iterator",
      RegisteredIterator(handle)
    )
  }

  /**
   * Resumes network module to enable network activity
   */
  def resume(): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[Unit, Unit]("net.resume", ())
  }

  /**
   * Resumes block iterator. The iterator stays exactly at the same position where the `resume_state` was catched.
   *
   * Application should call the `remove_iterator` when iterator is no longer required.
   * @param resume_state
   *   Same as value returned from `iterator_next`.
   */
  def resumeBlockIterator(
    resume_state: Value
  ): Future[Either[Throwable, RegisteredIterator]] = {
    ctx.execAsync[Value, RegisteredIterator](
      "net.resume_block_iterator",
      resume_state
    )
  }

  /**
   * Resumes transaction iterator. The iterator stays exactly at the same position where the `resume_state` was caught.
   * Note that `resume_state` doesn't store the account filter. If the application requires to use the same account
   * filter as it was when the iterator was created then the application must pass the account filter again in
   * `accounts_filter` parameter.
   *
   * Application should call the `remove_iterator` when iterator is no longer required.
   * @param resume_state
   *   Same as value returned from `iterator_next`.
   * @param accounts_filter
   *   Application can specify the list of accounts for which it wants to iterate transactions.
   *
   * If this parameter is missing or an empty list then the library iterates transactions for all accounts that passes
   * the shard filter.
   *
   * Note that the library doesn't detect conflicts between the account filter and the shard filter if both are
   * specified. So it is the application's responsibility to specify the correct filter combination.
   */
  def resumeTransactionIterator(
    resume_state: Value,
    accounts_filter: Option[List[String]]
  ): Future[Either[Throwable, RegisteredIterator]] = {
    ctx.execAsync[ParamsOfResumeTransactionIterator, RegisteredIterator](
      "net.resume_transaction_iterator",
      ParamsOfResumeTransactionIterator(
        resume_state,
        accounts_filter
      )
    )
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
