package com.radiance.scala.tonclient

import scala.concurrent.{ExecutionContext, Future}

/**
 * Provides information about library.
 */
class Client(val context: TONContext)(implicit val ec: ExecutionContext) {

  /**
   * Returns Core Library API reference
   *
   */
  def getApiReference: Future[String] =
    context.requestField(
      "client.get_api_reference",
      "{}"
    )("api")

  /**
   * Returns Core Library version
   *
   */
  def version: Future[String] =
    context.requestField[String](
      "client.version",
      "{}"
    )("version")

  /**
   *
   *
   */
  def buildInfo: Future[String] =
    context.requestField[String](
      "client.build_info",
      "{}"
    )("build_info")
}

