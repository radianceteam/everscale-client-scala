package com.radiance.jvm.boc


import com.radiance.jvm.Context

import scala.concurrent.Future

class BocModule(private val ctx: Context) {

  /**
    * @param block_boc  Key block BOC encoded as base64
    * */
  def getBlockchainConfig(
      block_boc: String
  ): Future[Either[Throwable, ResultOfGetBlockchainConfig]] = {
    val arg = ParamsOfGetBlockchainConfig(block_boc)
    ctx.execAsync("boc.get_blockchain_config", arg)
  }

  /**
    * Calculates BOC root hash
    * @param boc  BOC encoded as base64
    * */
  def getBocHash(boc: String): Future[Either[Throwable, ResultOfGetBocHash]] = {
    val arg = ParamsOfGetBocHash(boc)
    ctx.execAsync("boc.get_boc_hash", arg)
  }

  /** Extracts code from TVC contract image@param tvc  */
  def getCodeFromTvc(tvc: String): Future[Either[Throwable, ResultOfGetCodeFromTvc]] = {
    val arg = ParamsOfGetCodeFromTvc(tvc)
    ctx.execAsync("boc.get_code_from_tvc", arg)
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
    *  Parses block boc into a JSON
    *
    *  JSON structure is compatible with GraphQL API block object@param boc  BOC encoded as base64
    */
  def parseBlock(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsync("boc.parse_block", arg)
  }

  /**
    *  Parses message boc into a JSON
    *
    *  JSON structure is compatible with GraphQL API message object@param boc  BOC encoded as base64
    */
  def parseMessage(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsync("boc.parse_message", arg)
  }

  /**
    *  Parses shardstate boc into a JSON
    *
    *  JSON structure is compatible with GraphQL API shardstate object@param boc  BOC encoded as base64
    * @param id  Shardstate identificator
    * @param workchain_id  Workchain shardstate belongs to
   **/
  def parseShardstate(
      boc: String,
      id: String,
      workchain_id: Int
  ): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParseShardstate(boc, id, workchain_id)
    ctx.execAsync("boc.parse_shardstate", arg)
  }

  /**
    *  Parses transaction boc into a JSON
    *
    *  JSON structure is compatible with GraphQL API transaction object@param boc  BOC encoded as base64
    */
  def parseTransaction(
      boc: String
  ): Future[Either[Throwable, ResultOfParse]] = {
    val arg = ParamsOfParse(boc)
    ctx.execAsync("boc.parse_transaction", arg)
  }

}
