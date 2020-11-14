package com.radiance.scala.types

import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder, Json}
import io.circe.syntax._

object BocTypes {
  type Value = Json

  case class ParamsOfParse(boc: String) extends ApiNew {
    override type Out = ResultOfParse
    override val decoder: Decoder[ResultOfParse] = implicitly[Decoder[ResultOfParse]]
  }

  case class ResultOfParse(parsed: Value)

  case class ParamsOfParseShardstate(boc: String, id: String, workchain_id: Int) extends ApiNew {
    override type Out = ResultOfParse
    override val decoder: Decoder[ResultOfParse] = implicitly[Decoder[ResultOfParse]]
  }

  case class ParamsOfGetBlockchainConfig(block_boc: String) extends ApiNew {
    override type Out = ResultOfGetBlockchainConfig
    override val decoder: Decoder[ResultOfGetBlockchainConfig] =
      implicitly[Decoder[ResultOfGetBlockchainConfig]]
  }

  case class ResultOfGetBlockchainConfig(config_boc: String)

  case class ParamsOfGetBocHash(boc: String)

  case class ResultOfGetBocHash(hash: String)

  object ParamsOfParse {
    implicit val ParamsOfParseEncoder: Encoder[ParamsOfParse] = deriveEncoder[ParamsOfParse]
  }

  object ResultOfParse {
    implicit val ResultOfParseDecoder: Decoder[ResultOfParse] = deriveDecoder[ResultOfParse]
  }

  object ParamsOfParseShardstate {
    implicit val ParamsOfParseShardstateEncoder: Encoder[ParamsOfParseShardstate] =
      deriveEncoder[ParamsOfParseShardstate]
  }

  object ParamsOfGetBlockchainConfig {
    implicit val ParamsOfGetBlockchainConfigEncoder: Encoder[ParamsOfGetBlockchainConfig] =
      deriveEncoder[ParamsOfGetBlockchainConfig]
  }

  object ResultOfGetBlockchainConfig {
    implicit val ResultOfGetBlockchainConfigDecoder: Decoder[ResultOfGetBlockchainConfig] =
      deriveDecoder[ResultOfGetBlockchainConfig]
  }

  case object ParamsOfGetBocHash {
    implicit val ParamsOfGetBocHashEncoder: Encoder[ParamsOfGetBocHash] = deriveEncoder[ParamsOfGetBocHash]
  }

  case object ResultOfGetBocHash {
    implicit val ResultOfGetBocHashDecoder: Decoder[ResultOfGetBocHash] = deriveDecoder[ResultOfGetBocHash]
  }

}
