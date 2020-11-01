package com.radiance.scala.tonclient.boc.api

import com.radiance.scala.tonclient.Api
import io.circe._
import io.circe.derivation._

private [boc] case class GetBlockchainConfig(block_boc: String) extends Api {
  override val functionName: String = "boc.get_blockchain_config"
  override val fieldName: Option[String] = Some("config_boc")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private [boc] object GetBlockchainConfig {
  implicit val BlockchainConfigArgsEncoder: Encoder[GetBlockchainConfig] = deriveEncoder[GetBlockchainConfig]
}