package com.radiance.jvm.client

import com.radiance.jvm.Context

class ClientModule(private val ctx: Context) {

  /**
   * Returns detailed information about this build.
   */
  def buildInfo: Either[Throwable, ResultOfBuildInfo] =
    ctx.execSync[Unit, ResultOfBuildInfo]("client.build_info", ())

  /**
   * Returns Core Library API reference
   */
  def getApiReference: Either[Throwable, ResultOfGetApiReference] =
    ctx.execSync[Unit, ResultOfGetApiReference](
      "client.get_api_reference",
      ()
    )

  /**
   * Returns Core Library version
   */
  def version: Either[Throwable, ResultOfVersion] =
    ctx.execSync[Unit, ResultOfVersion]("client.version", ())

}
