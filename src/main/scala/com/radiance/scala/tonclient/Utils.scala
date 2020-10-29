package com.radiance.scala.tonclient

import scala.concurrent.{ExecutionContext, Future}


/**
 * Misc utility Functions.
 */
class Utils(val context: TONContext)(implicit val ec: ExecutionContext) {
  /**
   * Converts address from any TON format to any TON format
   *
   * @param address      Account address in any TON format.
   * @param outputFormat Specify the format to convert to.
   */
  def convertAddress(address: String, outputFormat: String): Future[String] =
    context.requestField[String](
      "utils.convert_address",
      s"""{"address":"$address", "output_format":"$outputFormat"}"""
    )("address")
}

