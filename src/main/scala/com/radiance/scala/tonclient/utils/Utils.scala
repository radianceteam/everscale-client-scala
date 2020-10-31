package com.radiance.scala.tonclient.utils

import com.radiance.scala.tonclient.TONContext
import com.radiance.scala.tonclient.utils.args.ConvertAddressArgs

import scala.concurrent.{ExecutionContext, Future}

/**
 * Misc utility Functions.
 */
class Utils(val ctx: TONContext)(implicit val ec: ExecutionContext) {
  /**
   * Converts address from any TON format to any TON format
   *
   * @param address      Account address in any TON format.
   * @param outputFormat Specify the format to convert to.
   */
  def convertAddress(address: String, outputFormat: String): Future[Either[Throwable, String]] = ctx
    .requestField[ConvertAddressArgs, String](ConvertAddressArgs(address: String, outputFormat: String))
}

