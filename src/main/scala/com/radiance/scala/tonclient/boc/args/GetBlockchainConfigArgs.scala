package com.radiance.scala.tonclient.boc.args

import com.radiance.scala.tonclient.Args
import io.circe._
import io.circe.derivation._

private [boc] case class GetBlockchainConfigArgs(block_boc: String) extends Args {
  override val functionName: String = "boc.get_blockchain_config"
  override val fieldName: Option[String] = Some("config_boc")
}

private [boc] object GetBlockchainConfigArgs {
  implicit val BlockchainConfigArgsEncoder: Encoder[GetBlockchainConfigArgs] = deriveEncoder[GetBlockchainConfigArgs]
}