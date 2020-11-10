package methods

import context.TonContextScala
import types.ClientTypes._

import scala.concurrent.Future

class ClientModule(private val ctx: TonContextScala) {
  /**  Returns detailed information about this build. */
  def build_info(): Future[Either[Throwable, ResultOfBuildInfo]] =
    ctx.execAsyncVoidNew[ResultOfBuildInfo]("client.build_info")

  /**  Returns Core Library version */
  def version(): Future[Either[Throwable, ResultOfVersion]] =
    ctx.execAsyncVoidNew[ResultOfVersion]("client.version")

  /**  Returns Core Library API reference */
  def get_api_reference(): Future[Either[Throwable, ResultOfGetApiReference]] =
    ctx.execAsyncVoidNew[ResultOfGetApiReference]("client.get_api_reference")
}
