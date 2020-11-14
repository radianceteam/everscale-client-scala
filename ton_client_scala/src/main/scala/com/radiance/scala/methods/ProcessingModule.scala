package com.radiance.scala.methods

import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.tonclient.TonContextScala.Request
import com.radiance.scala.types.AbiTypes._
import com.radiance.scala.types.ProcessingTypes._

import scala.concurrent.Future

class ProcessingModule(private val ctx: TonContextScala) {

  /**
   *  Creates message, sends it to the network and monitors its processing.
   *
   *  Creates ABI-compatible message,
   *  sends it to the network and monitors for the result transaction.
   *  Decodes the output messages' bodies.
   *
   *  If contract's ABI includes "expire" header, then
   *  SDK implements retries in case of unsuccessful message delivery within the expiration
   *  timeout: SDK recreates the message, sends it and processes it again.
   *
   *  The intermediate events, such as `WillFetchFirstBlock`, `WillSend`, `DidSend`,
   *  `WillFetchNextBlock`, etc - are switched on/off by `send_events` flag
   *  and logged into the supplied callback function.
   *  The retry configuration parameters are defined in config:
   *  <add correct config params here>
   *  pub const DEFAULT_EXPIRATION_RETRIES_LIMIT: i8 = 3; - max number of retries
   *  pub const DEFAULT_EXPIRATION_TIMEOUT: u32 = 40000;  - message expiration timeout in ms.
   *  pub const DEFAULT_....expiration_timeout_grow_factor... = 1.5 - factor that increases the expiration timeout for each retry
   *
   *  If contract's ABI does not include "expire" header
   *  then, if no transaction is found within the network timeout (see config parameter ), exits with error.@param message_encode_params  Message encode parameters.
   * @param send_events  Flag for requesting events sending
   * @param request
   */
  def process_message(message_encode_params: ParamsOfEncodeMessage, send_events: Boolean, callback: Request): Future[Either[Throwable, ResultOfProcessMessage]] = {
    val arg = ParamsOfProcessMessage(message_encode_params, send_events)
    ctx.execAsyncWithCallback("processing.process_message", arg, callback)
  }


  /**
   *  Performs monitoring of the network for the result transaction
   *  of the external inbound message processing.
   *
   *  `send_events` enables intermediate events, such as `WillFetchNextBlock`,
   *  `FetchNextBlockFailed` that may be useful for logging of new shard blocks creation
   *  during message processing.
   *
   *  Note, that presence of the `abi` parameter is critical for ABI
   *  compliant contracts. Message processing uses drastically
   *  different strategy for processing message for contracts which
   *  ABI includes "expire" header.
   *
   *  When the ABI header `expire` is present, the processing uses
   *  `message expiration` strategy:
   *  - The maximum block gen time is set to
   *    `message_expiration_timeout + transaction_wait_timeout`.
   *  - When maximum block gen time is reached, the processing will
   *    be finished with `MessageExpired` error.
   *
   *  When the ABI header `expire` isn't present or `abi` parameter
   *  isn't specified, the processing uses `transaction waiting`
   *  strategy:
   *  - The maximum block gen time is set to
   *    `now() + transaction_wait_timeout`.
   *
   *  - If maximum block gen time is reached and no result transaction is found,
   *  the processing will exit with an error.@param abi  Optional ABI for decoding the transaction result.
   *
   *  If it is specified, then the output messages' bodies will be
   *  decoded according to this ABI.
   *
   *  The `abi_decoded` result field will be filled out.
   * @param message  Message BOC. Encoded with `base64`.
   * @param shard_block_id  The last generated block id of the destination account shard before the message was sent.
   *
   *  You must provide the same value as the `send_message` has returned.
   * @param send_events  Flag that enables/disables intermediate events
   * @param callback
   */
  def wait_for_transaction(abi: Option[Abi], message: String, shard_block_id: String, send_events: Boolean, callback: Request):  Future[Either[Throwable, ResultOfProcessMessage]] = {
    val arg = ParamsOfWaitForTransaction(abi, message, shard_block_id, send_events)
    ctx.execAsync("processing.wait_for_transaction", arg)
  }
  /**
   *  Sends message to the network
   *
   *  Sends message to the network and returns the last generated shard block of the destination account
   *  before the message was sent. It will be required later for message processing.@param message  Message BOC.
   * @param abi  Optional message ABI.
   *
   *  If this parameter is specified and the message has the
   *  `expire` header then expiration time will be checked against
   *  the current time to prevent unnecessary sending of already expired message.
   *
   *  The `message already expired` error will be returned in this
   *  case.
   *
   *  Note, that specifying `abi` for ABI compliant contracts is
   *  strongly recommended, so that proper processing strategy can be
   *  chosen.
   * @param send_events  Flag for requesting events sending
   * @param callback
   */
  def send_message(message: String, abi: Option[Abi], send_events: Boolean, callback: Request): Future[Either[Throwable, ResultOfSendMessage]] = {
    val arg = ParamsOfSendMessage(message, abi, send_events)
    ctx.execAsync("processing.send_message", arg)
  }
}
