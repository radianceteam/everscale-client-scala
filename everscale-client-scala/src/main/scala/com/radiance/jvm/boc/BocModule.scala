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
   * Save BOC into cache or increase pin counter for existing pinned BOC
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
   * Unpin BOCs with specified pin defined in the `cache_set`. Decrease pin reference counter for BOCs with specified
   * pin defined in the `cache_set`. BOCs which have only 1 pin and its reference counter become 0 will be removed from
   * cache
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
   * Decodes contract's initial state into code, data, libraries and special options.
   *
   * @param state_init
   *   state_init
   * @param boc_cache
   *   boc_cache
   */
  def decodeStateInit(
    state_init: String,
    boc_cache: Option[BocCacheTypeADT.BocCacheType]
  ): Future[Either[Throwable, ResultOfDecodeStateInit]] = {
    ctx.execAsync[ParamsOfDecodeStateInit, ResultOfDecodeStateInit](
      "boc.decode_state_init",
      ParamsOfDecodeStateInit(state_init, boc_cache)
    )
  }

  /**
   * Decodes tvc according to the tvc spec. Read more about tvc structure here
   * https://github.com/tonlabs/ever-struct/blob/main/src/scheme/mod.rs#L30
   *
   * @param tvc
   *   tvc
   */
  def decodeTvc(tvc: String): Future[Either[Throwable, ResultOfDecodeTvc]] = {
    ctx.execAsync[ParamsOfDecodeTvc, ResultOfDecodeTvc](
      "boc.decode_tvc",
      ParamsOfDecodeTvc(tvc)
    )
  }

  /**
   * Encodes bag of cells (BOC) with builder operations. This method provides the same functionality as Solidity
   * TvmBuilder. Resulting BOC of this method can be passed into Solidity and C++ contracts as TvmCell type.
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
   * Encodes a message Allows to encode any external inbound message.
   * @param src
   *   src
   * @param dst
   *   dst
   * @param init
   *   init
   * @param body
   *   body
   * @param boc_cache
   *   The BOC itself returned if no cache type provided
   */
  def encodeExternalInMessage(
    src: Option[String],
    dst: String,
    init: Option[String],
    body: Option[String],
    boc_cache: Option[BocCacheTypeADT.BocCacheType]
  ): Future[Either[Throwable, ResultOfEncodeExternalInMessage]] = {
    ctx.execAsync[ParamsOfEncodeExternalInMessage, ResultOfEncodeExternalInMessage](
      "boc.encode_external_in_message",
      ParamsOfEncodeExternalInMessage(src, dst, init, body, boc_cache)
    )
  }

  /**
   * Encodes initial contract state from code, data, libraries ans special options (see input params)
   *
   * @param code
   *   code
   * @param data
   *   data
   * @param library
   *   library
   * @param tick
   *   Specifies the contract ability to handle tick transactions
   * @param tock
   *   Specifies the contract ability to handle tock transactions
   * @param split_depth
   *   split_depth
   * @param boc_cache
   *   boc_cache
   */
  def encodeStateInit(
    code: Option[String],
    data: Option[String],
    library: Option[String],
    tick: Option[Boolean],
    tock: Option[Boolean],
    split_depth: Option[Long],
    boc_cache: Option[BocCacheTypeADT.BocCacheType]
  ): Future[Either[Throwable, ResultOfEncodeStateInit]] = {
    ctx.execAsync[ParamsOfEncodeStateInit, ResultOfEncodeStateInit](
      "boc.encode_state_init",
      ParamsOfEncodeStateInit(code, data, library, tick, tock, split_depth, boc_cache)
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
   * Calculates BOC depth
   * @param boc
   *   boc
   */
  def getBocDepth(boc: String): Future[Either[Throwable, ResultOfGetBocDepth]] = {
    ctx.execAsync[ParamsOfGetBocDepth, ResultOfGetBocDepth](
      "boc.get_boc_depth",
      ParamsOfGetBocDepth(boc)
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
   * Returns the contract code's salt if it is present.
   * @param code
   *   code
   * @param boc_cache
   *   boc_cache
   */
  def getCodeSalt(
    code: String,
    boc_cache: Option[BocCacheTypeADT.BocCacheType]
  ): Future[Either[Throwable, ResultOfGetCodeSalt]] = {
    ctx.execAsync[ParamsOfGetCodeSalt, ResultOfGetCodeSalt]("boc.get_code_salt", ParamsOfGetCodeSalt(code, boc_cache))
  }

  /**
   * Returns the compiler version used to compile the code.
   * @param code
   *   code
   */
  def getCompilerVersion(code: String): Future[Either[Throwable, ResultOfGetCompilerVersion]] = {
    ctx.execAsync[ParamsOfGetCompilerVersion, ResultOfGetCompilerVersion](
      "boc.get_code_salt",
      ParamsOfGetCompilerVersion(code)
    )
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

  /**
   * Sets new salt to contract code. Returns the new contract code with salt.
   * @param code
   *   code
   * @param salt
   *   BOC encoded as base64 or BOC handle
   * @param boc_cache
   *   boc_cache
   */
  def setCodeSalt(
    code: String,
    salt: String,
    boc_cache: Option[BocCacheTypeADT.BocCacheType]
  ): Future[Either[Throwable, ResultOfSetCodeSalt]] = {
    ctx.execAsync[ParamsOfSetCodeSalt, ResultOfSetCodeSalt](
      "boc.set_code_salt",
      ParamsOfSetCodeSalt(code, salt, boc_cache)
    )
  }

}
