package methods

import context.TonContextScala
import types.BocTypes._

import scala.concurrent.Future

class BocModule(private val ctx: TonContextScala) {
  /** @param block_boc  Key block BOC encoded as base64 */
  def get_blockchain_config(block_boc: String): Future[Either[Throwable, ResultOfGetBlockchainConfig]] = {
    val arg = ParamsOfGetBlockchainConfig(block_boc)
    ctx.execAsyncNew("boc.get_blockchain_config", arg)
  }

  /**
   *  Parses shardstate boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API shardstate object@param boc  BOC encoded as base64
   * @param id  Shardstate identificator
   * @param workchain_id  Workchain shardstate belongs to
   */
  def parse_shardstate(boc: String, id: String, workchain_id: Int): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParseShardstate(boc, id, workchain_id)
    ctx.execAsyncNew("boc.get_blockchain_config", arg)
  }

  /**
   *  Parses block boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API block object@param boc  BOC encoded as base64
   */
  def parse_block(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsyncNew("boc.parse_block", arg)
  }
  /**
   *  Parses account boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API account object@param boc  BOC encoded as base64
   */
  def parse_account(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsyncNew("boc.parse_account", arg)
  }
  /**
   *  Parses transaction boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API transaction object@param boc  BOC encoded as base64
   */
  def parse_transaction(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsyncNew("boc.parse_transaction", arg)
  }
  /**
   *  Parses message boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API message object@param boc  BOC encoded as base64
   */
  def parse_message(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsyncNew("boc.parse_transaction", arg)
  }
}
