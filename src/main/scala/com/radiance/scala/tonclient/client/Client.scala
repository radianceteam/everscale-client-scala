package com.radiance.scala.tonclient.client

import com.radiance.scala.tonclient.TonContext
import com.radiance.scala.tonclient.client.api._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Provides information about library.
 */
class Client(val ctx: TonContext)(implicit val ec: ExecutionContext) {

  /**
   *
   *
   */
  def buildInfo: Future[Either[Throwable, String]] = ctx.exec(BuildInfo())

  /**
   * Returns Core Library API reference
   *
   */
  def getApiReference: Future[Either[Throwable, String]] = ctx.exec(GetApiReference())

  /**
   * Returns Core Library version
   *
   */
  def version: Future[Either[Throwable, String]] = ctx.exec(Version())
}

