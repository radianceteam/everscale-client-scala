package com.radiance.scala.modules

import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.types.BocTypes._

import scala.concurrent.Future

class BocModule(private val ctx: TonContextScala) {

  /**  Calculates BOC root hash@param boc  BOC encoded as base64 */
  def getBocHash(boc: String): Future[Either[Throwable, ResultOfGetBocHash]] = ???

  /** @param block_boc  Key block BOC encoded as base64 */
  def getBlockchainConfig(block_boc: String): Future[Either[Throwable, ResultOfGetBlockchainConfig]] = {
    val arg = ParamsOfGetBlockchainConfig(block_boc)
    ctx.execAsync("boc.get_blockchain_config", arg)
  }

  /**
   *  Parses shardstate boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API shardstate object@param boc  BOC encoded as base64
   * @param id  Shardstate identificator
   * @param workchain_id  Workchain shardstate belongs to
   */
  def parseShardstate(boc: String, id: String, workchain_id: Int): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParseShardstate(boc, id, workchain_id)
    ctx.execAsync("boc.get_blockchain_config", arg)
  }

  /**
   *  Parses block boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API block object@param boc  BOC encoded as base64
   */
  def parseBlock(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsync("boc.parse_block", arg)
  }
  /**
   *  Parses account boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API account object@param boc  BOC encoded as base64
   */
  def parseAccount(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsync("boc.parse_account", arg)
  }
  /**
   *  Parses transaction boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API transaction object@param boc  BOC encoded as base64
   */
  def parseTransaction(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsync("boc.parse_transaction", arg)
  }
  /**
   *  Parses message boc into a JSON
   *
   *  JSON structure is compatible with GraphQL API message object@param boc  BOC encoded as base64
   */
  def parseMessage(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsync("boc.parse_transaction", arg)
  }
}
