package com.radiance.scala.methods

import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.types.UtilsTypes._

import scala.concurrent.Future

class UtilsModule(private val ctx: TonContextScala) {
  /**
   *  Converts address from any TON format to any TON format@param address  Account address in any TON format.
   * @param output_format  Specify the format to convert to.
   */
  def convert_address(address: String, output_format: AddressStringFormat): Future[Either[Throwable, ResultOfConvertAddress]] = {
    val arg = ParamsOfConvertAddress(address, output_format)
    ctx.execAsync("utils.convert_address", arg)
  }
}
