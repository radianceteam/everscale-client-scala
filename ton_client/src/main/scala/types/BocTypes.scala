package types
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

object BocTypes {
  type Value = String

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

}
