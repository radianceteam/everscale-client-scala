package com.radiance.scala.modules

import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.types.ClientTypes._

class ClientModule(private val ctx: TonContextScala) {
  /**  Returns detailed information about this build. */
  def buildInfo: Either[Throwable, ResultOfBuildInfo] = ctx.execSyncVoid[ResultOfBuildInfo]("client.build_info")

  /**  Returns Core Library version */
  def version: Either[Throwable, ResultOfVersion] = ctx.execSyncVoid[ResultOfVersion]("client.version")

  /**  Returns Core Library API reference */
  def getApiReference: Either[Throwable, ResultOfGetApiReference] =
    ctx.execSyncVoid[ResultOfGetApiReference]("client.get_api_reference")
}
