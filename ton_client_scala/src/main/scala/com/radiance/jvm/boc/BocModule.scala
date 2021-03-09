package com.radiance.jvm.boc

import com.radiance.jvm.Context

import scala.concurrent.Future

class BocModule(private val ctx: Context) {

  /**
   * Get BOC from cache
   * @param boc_ref
   *   boc_ref
   */
  def cacheGet(boc_ref: String): Future[Either[Throwable, ResultOfBocCacheGet]] = {
    ctx.execAsync[ParamsOfBocCacheGet, ResultOfBocCacheGet](
      "boc.cache_get",
      ParamsOfBocCacheGet(boc_ref)
    )
  }

  /**
   * Save BOC into cache
   * @param boc
   *   boc
   * @param cache_type
   *   cache_type
   */
  def cacheSet(
    boc: String,
    cache_type: BocCacheTypeADT.BocCacheType
  ): Future[Either[Throwable, ResultOfBocCacheSet]] = {
    ctx.execAsync[ParamsOfBocCacheSet, ResultOfBocCacheSet](
      "boc.cache_set",
      ParamsOfBocCacheSet(boc, cache_type)
    )
  }

  /**
   * Unpin BOCs with specified pin. BOCs which don't have another pins will be removed from cache
   * @param pin
   *   pin
   * @param boc_ref
   *   If it is provided then only referenced BOC is unpinned
   */
  def cacheUnpin(pin: String, boc_ref: Option[String]): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[ParamsOfBocCacheUnpin, Unit](
      "boc.cache_unpin",
      ParamsOfBocCacheUnpin(pin, boc_ref)
    )
  }

  /**
   * Encodes BOC from builder operations.
   * @param builder
   *   builder
   * @param boc_cache
   *   boc_cache
   */
  def encodeBoc(
    builder: List[BuilderOpADT.BuilderOp],
    boc_cache: Option[BocCacheTypeADT.BocCacheType]
  ): Future[Either[Throwable, ResultOfEncodeBoc]] = {
    ctx.execAsync[ParamsOfEncodeBoc, ResultOfEncodeBoc](
      "boc.encode_boc",
      ParamsOfEncodeBoc(builder, boc_cache)
    )
  }

  /**
   * Extract blockchain configuration from key block and also from zerostate.
   * @param block_boc
   *   block_boc
   */
  def getBlockchainConfig(
    block_boc: String
  ): Future[Either[Throwable, ResultOfGetBlockchainConfig]] = {
    ctx.execAsync[ParamsOfGetBlockchainConfig, ResultOfGetBlockchainConfig](
      "boc.get_blockchain_config",
      ParamsOfGetBlockchainConfig(block_boc)
    )
  }

  /**
   * Calculates BOC root hash
   * @param boc
   *   BOC encoded as base64
   */
  def getBocHash(boc: String): Future[Either[Throwable, ResultOfGetBocHash]] = {
    ctx.execAsync[ParamsOfGetBocHash, ResultOfGetBocHash]("boc.get_boc_hash", ParamsOfGetBocHash(boc))
  }

  /**
   * Extracts code from TVC contract image
   * @param tvc
   *   tvc
   */
  def getCodeFromTvc(
    tvc: String
  ): Future[Either[Throwable, ResultOfGetCodeFromTvc]] = {
    ctx.execAsync[ParamsOfGetCodeFromTvc, ResultOfGetCodeFromTvc]("boc.get_code_from_tvc", ParamsOfGetCodeFromTvc(tvc))
  }

  /**
   * Parses account boc into a JSON
   *
   * JSON structure is compatible with GraphQL API account object
   * @param boc
   *   BOC encoded as base64
   */
  def parseAccount(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    ctx.execAsync[ParamsOfParse, ResultOfParse]("boc.parse_account", ParamsOfParse(boc))
  }

  /**
   * Parses block boc into a JSON
   *
   * JSON structure is compatible with GraphQL API block object
   * @param boc
   *   BOC encoded as base64
   */
  def parseBlock(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    ctx.execAsync[ParamsOfParse, ResultOfParse]("boc.parse_block", ParamsOfParse(boc))
  }

  /**
   * Parses message boc into a JSON
   *
   * JSON structure is compatible with GraphQL API message object
   * @param boc
   *   BOC encoded as base64
   */
  def parseMessage(boc: String): Future[Either[Throwable, ResultOfParse]] = {
    ctx.execAsync[ParamsOfParse, ResultOfParse]("boc.parse_message", ParamsOfParse(boc))
  }

  /**
   * Parses shardstate boc into a JSON
   *
   * JSON structure is compatible with GraphQL API shardstate object
   * @param boc
   *   BOC encoded as base64
   * @param id
   *   Shardstate identificator
   * @param workchain_id
   *   Workchain shardstate belongs to
   */
  def parseShardstate(
    boc: String,
    id: String,
    workchain_id: Int
  ): Future[Either[Throwable, ResultOfParse]] = {
    ctx.execAsync[ParamsOfParseShardstate, ResultOfParse](
      "boc.parse_shardstate",
      ParamsOfParseShardstate(boc, id, workchain_id)
    )
  }

  /**
   * Parses transaction boc into a JSON
   *
   * JSON structure is compatible with GraphQL API transaction object
   * @param boc
   *   BOC encoded as base64
   */
  def parseTransaction(
    boc: String
  ): Future[Either[Throwable, ResultOfParse]] = {
    ctx.execAsync[ParamsOfParse, ResultOfParse]("boc.parse_transaction", ParamsOfParse(boc))
  }

}
