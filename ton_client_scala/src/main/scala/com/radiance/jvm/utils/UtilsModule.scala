package com.radiance.jvm.utils

import com.radiance.jvm.Context

class UtilsModule(private val ctx: Context) {

  /**
   * Calculates storage fee for an account over a specified time period
   * @param account
   *   account
   * @param period
   *   period
   */
  def calcStorageFee(account: String, period: Long): Either[Throwable, ResultOfCalcStorageFee] = {
    ctx.execSync[ParamsOfCalcStorageFee, ResultOfCalcStorageFee](
      "utils.calc_storage_fee",
      ParamsOfCalcStorageFee(account, period)
    )
  }

  /**
   * Converts address from any TON format to any TON format
   * @param address
   *   Account address in any TON format.
   * @param output_format
   *   Specify the format to convert to.
   */
  def convertAddress(
    address: String,
    output_format: AddressStringFormatADT.AddressStringFormat
  ): Either[Throwable, ResultOfConvertAddress] =
    ctx.execSync[ParamsOfConvertAddress, ResultOfConvertAddress](
      "utils.convert_address",
      ParamsOfConvertAddress(address, output_format)
    )
}
