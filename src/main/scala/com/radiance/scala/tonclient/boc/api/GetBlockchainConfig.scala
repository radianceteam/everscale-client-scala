package com.radiance.scala.tonclient.boc.api

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private [boc] case class GetBlockchainConfig(block_boc: String) extends Args {
  override val functionName: String = "boc.get_blockchain_config"
  override val fieldName: Option[String] = Some("config_boc")
  override type Out = String
  override val decoder: Decoder[String] = implicitly[Decoder[String]]
}

private [boc] object GetBlockchainConfig {
  implicit val BlockchainConfigArgsEncoder: Encoder[GetBlockchainConfig] = deriveEncoder[GetBlockchainConfig]
}