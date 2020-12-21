package com.radiance.jvm.client

import com.radiance.jvm.Context

class ClientModule(private val ctx: Context) {

  /**  Returns detailed information about this build. */
  def buildInfo: Either[Throwable, ResultOfBuildInfo] =
    ctx.execSyncParameterless[ResultOfBuildInfo]("client.build_info")

  /**  Returns Core Library API reference */
  def getApiReference: Either[Throwable, ResultOfGetApiReference] =
    ctx.execSyncParameterless[ResultOfGetApiReference]("client.get_api_reference")

  /**
    * @param app_request_id
    * @param result
    */
  def resolveAppRequest(
      app_request_id: Long,
      result: AppRequestResult
  ): Unit = {
    val arg = ParamsOfResolveAppRequest(app_request_id, result)
    ctx.execSync[ParamsOfResolveAppRequest]("client.resolve_app_request", arg)
  }

  /**  Returns Core Library version */
  def version: Either[Throwable, ResultOfVersion] =
    ctx.execSyncParameterless[ResultOfVersion]("client.version")

}
