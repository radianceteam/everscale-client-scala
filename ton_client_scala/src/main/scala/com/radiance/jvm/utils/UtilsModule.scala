package com.radiance.jvm.utils

import com.radiance.jvm.Context

class UtilsModule(private val ctx: Context) {

  /**
   * Converts address from any TON format to any TON format
   * @param address
   *   Account address in any TON format.
   * @param output_format
   *   Specify the format to convert to.
   */
  def convertAddress(
    address: String,
    output_format: AddressStringFormat
  ): Either[Throwable, ResultOfConvertAddress] =
    ctx.execSync[ParamsOfConvertAddress, ResultOfConvertAddress](
      "utils.convert_address",
      ParamsOfConvertAddress(address, output_format)
    )
}
