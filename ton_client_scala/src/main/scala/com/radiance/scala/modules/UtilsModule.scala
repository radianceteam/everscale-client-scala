package com.radiance.scala.modules

import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.types.UtilsTypes._


class UtilsModule(private val ctx: TonContextScala) {
  /**
   *  Converts address from any TON format to any TON format@param address  Account address in any TON format.
   * @param output_format  Specify the format to convert to.
   */
  def convertAddress(address: String, output_format: AddressStringFormat): Either[Throwable, ResultOfConvertAddress] =
    ctx.execSync("utils.convert_address", ParamsOfConvertAddress(address, output_format))
}
