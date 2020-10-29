package com.radiance.scala.tonclient

import scala.concurrent.{ExecutionContext, Future}


class Boc(val context: TONContext)(implicit val ec: ExecutionContext) {

  /**
   * Parses message boc into a JSON <p> JSON structure is compatible with GraphQL API message object
   *
   * @param boc BOC encoded as base64
   */
  def parseMessage(boc: String): Future[String] =
    context.requestField[String](
      "boc.parse_message",
      s"""{"boc":"$boc"}"""
    )("parsed")

  /**
   * Parses transaction boc into a JSON <p> JSON structure is compatible with GraphQL API transaction object
   *
   * @param boc BOC encoded as base64
   */
  def parseTransaction(boc: String): Future[String] =
    context.requestField(
      "boc.parse_transaction",
      s"""{"boc":"$boc"}"""
    )("parsed")

  /**
   * Parses account boc into a JSON <p> JSON structure is compatible with GraphQL API account object
   *
   * @param boc BOC encoded as base64
   */
  def parseAccount(boc: String): Future[String] =
    context.requestField[String](
      "boc.parse_account",
      s"""{"boc":"$boc"}"""
    )("parsed")

  /**
   * Parses block boc into a JSON <p> JSON structure is compatible with GraphQL API block object
   *
   * @param boc BOC encoded as base64
   */
  def parseBlock(boc: String): Future[String] =
    context.requestField[String](
      "boc.parse_block",
      s"""{"boc":"$boc"}"""
    )("parsed")

  /**
   *
   *
   * @param blockBoc Key block BOC encoded as base64
   */
  def getBlockchainConfig(blockBoc: String): Future[String] =
    context.requestField(
      "boc.get_blockchain_config",
      s"""{"block_boc":"$blockBoc"}"""
    )("config_boc")
}
