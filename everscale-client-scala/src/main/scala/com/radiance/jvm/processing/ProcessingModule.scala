package com.radiance.jvm.processing

import com.radiance.jvm._
import com.radiance.jvm.abi._

import scala.concurrent.Future

class ProcessingModule(private val ctx: Context) {

  /**
   * Cancels all background activity and releases all allocated system resources for the specified monitoring queue.
   *
   * @param queue
   *   queue
   */
  def cancelMonitor(queue: String): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[ParamsOfCancelMonitor, Unit]("processing.cancel_monitor", ParamsOfCancelMonitor(queue))
  }

  /**
   * Fetches next resolved results from the specified monitoring queue. Results and waiting options are depends on the
   * `wait` parameter. All returned results will be removed from the queue's resolved list.
   *
   * @param queue
   *   queue
   * @param wait_mode
   *   Default is `NO_WAIT`.
   */
  def fetchNextMonitorResults(
    queue: String,
    wait_mode: Option[MonitorFetchWaitModeEnum.MonitorFetchWaitMode]
  ): Future[Either[Throwable, ResultOfFetchNextMonitorResults]] = {
    ctx.execAsync[ParamsOfFetchNextMonitorResults, ResultOfFetchNextMonitorResults](
      "processing.fetch_next_monitor_results",
      ParamsOfFetchNextMonitorResults(queue, wait_mode)
    )
  }

  /**
   * Returns summary information about current state of the specified monitoring queue.
   *
   * @param queue
   *   queue
   */
  def getMonitorInfo(queue: String): Future[Either[Throwable, MonitoringQueueInfo]] = {
    ctx.execAsync[ParamsOfGetMonitorInfo, MonitoringQueueInfo](
      "processing.fetch_next_monitor_results",
      ParamsOfGetMonitorInfo(queue)
    )
  }

  /**
   * Starts monitoring for the processing results of the specified messages. Message monitor performs background
   * monitoring for a message processing results for the specified set of messages.
   *
   * Message monitor can serve several isolated monitoring queues. Each monitor queue has a unique application defined
   * identifier (or name) used to separate several queue's.
   *
   * There are two important lists inside of the monitoring queue:
   *
   *   - unresolved messages: contains messages requested by the application for monitoring and not yet resolved;
   *
   *   - resolved results: contains resolved processing results for monitored messages.
   *
   * Each monitoring queue tracks own unresolved and resolved lists. Application can add more messages to the monitoring
   * queue at any time.
   *
   * Message monitor accumulates resolved results. Application should fetch this results with `fetchNextMonitorResults`
   * function.
   *
   * When both unresolved and resolved lists becomes empty, monitor stops any background activity and frees all
   * allocated internal memory.
   *
   * If monitoring queue with specified name already exists then messages will be added to the unresolved list.
   *
   * If monitoring queue with specified name does not exist then monitoring queue will be created with specified
   * unresolved messages.
   *
   * @param queue
   *   queue
   * @param messages
   *   messages
   */
  def monitorMessages(queue: String, messages: List[MessageMonitoringParams]): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[ParamsOfMonitorMessages, Unit](
      "processing.monitor_messages",
      ParamsOfMonitorMessages(queue, messages)
    )
  }

  /**
   * Creates message, sends it to the network and monitors its processing. Creates ABI-compatible message, sends it to
   * the network and monitors for the result transaction. Decodes the output messages' bodies.
   *
   * If contract's ABI includes "expire" header, then SDK implements retries in case of unsuccessful message delivery
   * within the expiration timeout: SDK recreates the message, sends it and processes it again.
   *
   * The intermediate events, such as `WillFetchFirstBlock`, `WillSend`, `DidSend`, `WillFetchNextBlock`, etc - are
   * switched on/off by `send_events` flag and logged into the supplied callback function.
   *
   * The retry configuration parameters are defined in the client's `NetworkConfig` and `AbiConfig`.
   *
   * If contract's ABI does not include "expire" header then, if no transaction is found within the network timeout (see
   * config parameter ), exits with error.
   * @param message_encode_params
   *   message_encode_params
   * @param send_events
   *   send_events
   * @param request
   *   request
   */
  def processMessage(
    message_encode_params: ParamsOfEncodeMessage,
    send_events: Option[Boolean],
    request: Request
  ): Future[Either[Throwable, ResultOfProcessMessage]] = {
    ctx.execAsyncWithCallback[ParamsOfProcessMessage, ResultOfProcessMessage](
      "processing.process_message",
      ParamsOfProcessMessage(message_encode_params, send_events),
      request
    )
  }

  /**
   * Sends message to the network and returns the last generated shard block of the destination account before the
   * message was sent. It will be required later for message processing.
   * @param message
   *   message
   * @param abi
   *   If this parameter is specified and the message has the `expire` header then expiration time will be checked
   *   against the current time to prevent unnecessary sending of already expired message.
   *
   * The `message already expired` error will be returned in this case.
   *
   * Note, that specifying `abi` for ABI compliant contracts is strongly recommended, so that proper processing strategy
   * can be chosen.
   * @param send_events
   *   send_events
   * @param callback
   *   callback
   */
  def sendMessage(
    message: String,
    abi: Option[AbiADT.Abi],
    send_events: Option[Boolean],
    callback: Request
  ): Future[Either[Throwable, ResultOfSendMessage]] = {
    ctx.execAsyncWithCallback[ParamsOfSendMessage, ResultOfSendMessage](
      "processing.send_message",
      ParamsOfSendMessage(message, abi, send_events),
      callback
    )
  }

  /**
   * Sends specified messages to the blockchain.
   *
   * @param messages
   *   messages
   * @param monitor_queue
   *   monitor_queue
   */
  def sendMessages(
    messages: List[MessageSendingParams],
    monitor_queue: Option[String]
  ): Future[Either[Throwable, ResultOfSendMessages]] = {
    ctx.execAsync[ParamsOfSendMessages, ResultOfSendMessages](
      "processing.send_messages",
      ParamsOfSendMessages(messages, monitor_queue)
    )
  }

  /**
   * Performs monitoring of the network for the result transaction of the external inbound message processing.
   * `send_events` enables intermediate events, such as `WillFetchNextBlock`, `FetchNextBlockFailed` that may be useful
   * for logging of new shard blocks creation during message processing.
   *
   * Note, that presence of the `abi` parameter is critical for ABI compliant contracts. Message processing uses
   * drastically different strategy for processing message for contracts which ABI includes "expire" header.
   *
   * When the ABI header `expire` is present, the processing uses `message expiration` strategy:
   *   - The maximum block gen time is set to `message_expiration_timeout + transaction_wait_timeout`.
   *   - When maximum block gen time is reached, the processing will be finished with `MessageExpired` error.
   *
   * When the ABI header `expire` isn't present or `abi` parameter isn't specified, the processing uses `transaction
   * waiting` strategy:
   *   - The maximum block gen time is set to `now() + transaction_wait_timeout`.
   *
   *   - If maximum block gen time is reached and no result transaction is found, the processing will exit with an
   *     error.
   * @param abi
   *   If it is specified, then the output messages' bodies will be decoded according to this ABI.
   *
   * The `abi_decoded` result field will be filled out.
   * @param message
   *   Encoded with `base64`.
   * @param shard_block_id
   *   You must provide the same value as the `send_message` has returned.
   * @param send_events
   *   send_events
   * @param sending_endpoints
   *   Use this field to get more informative errors. Provide the same value as the `send_message` has returned. If the
   *   message was not delivered (expired), SDK will log the endpoint URLs, used for its sending.
   * @param callback
   *   callback
   */
  def waitForTransaction(
    abi: Option[AbiADT.Abi],
    message: String,
    shard_block_id: String,
    send_events: Option[Boolean],
    sending_endpoints: Option[List[String]],
    callback: Request
  ): Future[Either[Throwable, ResultOfProcessMessage]] = {
    ctx.execAsyncWithCallback[ParamsOfWaitForTransaction, ResultOfProcessMessage](
      "processing.wait_for_transaction",
      ParamsOfWaitForTransaction(abi, message, shard_block_id, send_events, sending_endpoints),
      callback
    )
  }
}
