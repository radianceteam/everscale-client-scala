package com.radiance.scala.tonclient.types.config

import io.circe._
import io.circe.derivation._

case class NetworkConfig(
                          server_address: String,
                          network_retries_count: Int,
                          message_retries_count: Int,
                          message_processing_timeout: Long,
                          wait_for_timeout: Long,
                          outOfSyncThreshold: Long,
                          accessKey: String
                        )

object NetworkConfig {
  implicit val networkConfigCodec: Codec[NetworkConfig] = deriveCodec[NetworkConfig]
}