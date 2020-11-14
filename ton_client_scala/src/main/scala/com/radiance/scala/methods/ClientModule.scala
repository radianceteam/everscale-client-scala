package com.radiance.scala.methods

import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.types.ClientTypes._

import scala.concurrent.Future

class ClientModule(private val ctx: TonContextScala) {
  /**  Returns detailed information about this build. */
  def build_info(): Future[Either[Throwable, ResultOfBuildInfo]] =
    ctx.execAsyncVoid[ResultOfBuildInfo]("client.build_info")

  /**  Returns Core Library version */
  def version(): Future[Either[Throwable, ResultOfVersion]] = {
    ctx.execAsyncVoid[ResultOfVersion]("client.version")
  }

  /**  Returns Core Library API reference */
  def get_api_reference(): Future[Either[Throwable, ResultOfGetApiReference]] =
    ctx.execAsyncVoid[ResultOfGetApiReference]("client.get_api_reference")
}
