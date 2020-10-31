package com.radiance.scala.tonclient.boc

import com.radiance.scala.tonclient.TONContext
import com.radiance.scala.tonclient.boc.args._

import scala.concurrent.{ExecutionContext, Future}


class Boc(val ctx: TONContext)(implicit val ec: ExecutionContext) {

  /**
   *TODO fill it
   *
   * @param blockBoc Key block BOC encoded as base64
   */
  def getBlockchainConfig(blockBoc: String): Future[Either[Throwable, String]] = ctx.requestField[GetBlockchainConfigArgs, String](
    GetBlockchainConfigArgs(blockBoc)
  )

  /**
   * Parses account boc into a JSON <p> JSON structure is compatible with GraphQL API account object
   *
   * @param boc BOC encoded as base64
   */
  def parseAccount(boc: String): Future[Either[Throwable, String]] = ctx.requestField[ParseAccountArgs, String](ParseAccountArgs(boc))

  /**
   * Parses block boc into a JSON <p> JSON structure is compatible with GraphQL API block object
   *
   * @param boc BOC encoded as base64
   */
  def parseBlock(boc: String): Future[Either[Throwable, String]] = ctx.requestField[ParseBlockArgs, String](ParseBlockArgs(boc))

  /**
   * Parses message boc into a JSON <p> JSON structure is compatible with GraphQL API message object
   *
   * @param boc BOC encoded as base64
   */
  def parseMessage(boc: String): Future[Either[Throwable, String]] =ctx.requestField[ParseMessageArgs, String](ParseMessageArgs(boc))

  /**
   * Parses transaction boc into a JSON <p> JSON structure is compatible with GraphQL API transaction object
   *
   * @param boc BOC encoded as base64
   */
  def parseTransaction(boc: String): Future[Either[Throwable, String]] = ctx.requestField[ParseTransactionArgs, String](ParseTransactionArgs(boc))
}
