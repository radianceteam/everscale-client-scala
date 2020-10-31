package com.radiance.scala.tonclient.client

import com.radiance.scala.tonclient.TONContext
import com.radiance.scala.tonclient.client.args._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Provides information about library.
 */
class Client(val ctx: TONContext)(implicit val ec: ExecutionContext) {

  /**
   *
   *
   */
  def buildInfo: Future[Either[Throwable, String]] = ctx.requestField[BuildInfoArgs, String](BuildInfoArgs())

  /**
   * Returns Core Library API reference
   *
   */
  def getApiReference: Future[Either[Throwable, String]] = ctx.requestField[GetApiReferenceArgs, String](GetApiReferenceArgs())

  /**
   * Returns Core Library version
   *
   */
  def version: Future[Either[Throwable, String]] = ctx.requestField[VersionArgs, String](VersionArgs())
}

