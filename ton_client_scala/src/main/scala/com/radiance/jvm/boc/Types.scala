package com.radiance.jvm.boc

import com.radiance.jvm._
import io.circe.derivation._
import io.circe._

case class ParamsOfGetBlockchainConfig(block_boc: String) extends Bind {
  override type Out = ResultOfGetBlockchainConfig
  override val decoder: Decoder[ResultOfGetBlockchainConfig] =
    implicitly[Decoder[ResultOfGetBlockchainConfig]]
}

object ParamsOfGetBlockchainConfig {
  implicit val ParamsOfGetBlockchainConfigEncoder
      : Encoder[ParamsOfGetBlockchainConfig] =
    deriveEncoder[ParamsOfGetBlockchainConfig]
}

case class ParamsOfGetBocHash(boc: String) extends Bind {
  override type Out = ResultOfGetBocHash
  override val decoder: Decoder[ResultOfGetBocHash] =
    implicitly[Decoder[ResultOfGetBocHash]]
}

object ParamsOfGetBocHash {
  implicit val ParamsOfGetBocHashEncoder: Encoder[ParamsOfGetBocHash] =
    deriveEncoder[ParamsOfGetBocHash]
}

case class ParamsOfParse(boc: String) extends Bind {
  override type Out = ResultOfParse
  override val decoder: Decoder[ResultOfParse] =
    implicitly[Decoder[ResultOfParse]]
}

object ParamsOfParse {
  implicit val encoder: Encoder[ParamsOfParse] = deriveEncoder[ParamsOfParse]
}

case class ParamsOfParseShardstate(boc: String, id: String, workchain_id: Int)
    extends Bind {
  override type Out = ResultOfParse
  override val decoder: Decoder[ResultOfParse] =
    implicitly[Decoder[ResultOfParse]]
}

object ParamsOfParseShardstate {
  implicit val ParamsOfParseShardstateEncoder
      : Encoder[ParamsOfParseShardstate] =
    deriveEncoder[ParamsOfParseShardstate]
}

case class ResultOfGetBlockchainConfig(config_boc: String)

object ResultOfGetBlockchainConfig {
  implicit val ResultOfGetBlockchainConfigDecoder
      : Decoder[ResultOfGetBlockchainConfig] =
    deriveDecoder[ResultOfGetBlockchainConfig]
}

case class ResultOfGetBocHash(hash: String)

object ResultOfGetBocHash {
  implicit val ResultOfGetBocHashDecoder: Decoder[ResultOfGetBocHash] =
    deriveDecoder[ResultOfGetBocHash]
}

case class ResultOfParse(parsed: Value)
object ResultOfParse {
  implicit val ResultOfParseDecoder: Decoder[ResultOfParse] =
    deriveDecoder[ResultOfParse]
}
