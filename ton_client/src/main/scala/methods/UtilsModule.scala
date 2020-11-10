package methods

import context.TonContextScala
import types.UtilsTypes._

import scala.concurrent.Future

class UtilsModule(private val ctx: TonContextScala) {
  /**
   *  Converts address from any TON format to any TON format@param address  Account address in any TON format.
   * @param output_format  Specify the format to convert to.
   */
  def convert_address(address: String, output_format: AddressStringFormat): Future[Either[Throwable, ResultOfConvertAddress]] = {
    val arg = ParamsOfConvertAddress(address, output_format)
    ctx.execAsyncNew("utils.convert_address", arg)
  }
}
