package com.radiance.scala.tonclient.utils

import com.radiance.scala.tonclient.TonContext
import com.radiance.scala.tonclient.utils.api.ConvertAddress

import scala.concurrent.{ExecutionContext, Future}

/**
 * Misc utility Functions.
 */
class Utils(val ctx: TonContext)(implicit val ec: ExecutionContext) {
  /**
   * Converts address from any TON format to any TON format
   *
   * @param address      Account address in any TON format.
   * @param outputFormat Specify the format to convert to.
   */
  def convertAddress(address: String, outputFormat: String): Future[Either[Throwable, String]] = ctx
    .exec(ConvertAddress(address, outputFormat))
}

