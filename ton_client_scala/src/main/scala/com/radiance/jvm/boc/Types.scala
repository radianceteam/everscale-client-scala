package com.radiance.jvm.boc

import com.radiance.jvm._
import io.circe.derivation._
import io.circe._

sealed trait BocErrorCode {
  val code: String
}

object BocErrorCode {

  case object InvalidBoc extends BocErrorCode {
    override val code: String = "201"
  }

  case object SerializationError extends BocErrorCode {
    override val code: String = "202"
  }

  case object InappropriateBlock extends BocErrorCode {
    override val code: String = "203"
  }

  case object MissingSourceBoc extends BocErrorCode {
    override val code: String = "204"
  }

}
case class ParamsOfGetBlockchainConfig(block_boc: String)

object ParamsOfGetBlockchainConfig {
  implicit val encoder: Encoder[ParamsOfGetBlockchainConfig] =
    deriveEncoder[ParamsOfGetBlockchainConfig]
}

case class ParamsOfGetBocHash(boc: String)

object ParamsOfGetBocHash {
  implicit val encoder: Encoder[ParamsOfGetBocHash] =
    deriveEncoder[ParamsOfGetBocHash]
}

case class ParamsOfGetCodeFromTvc(tvc: String)

object ParamsOfGetCodeFromTvc {
  implicit val encoder: Encoder[ParamsOfGetCodeFromTvc] =
    deriveEncoder[ParamsOfGetCodeFromTvc]
}

case class ParamsOfParse(boc: String)

object ParamsOfParse {
  implicit val encoder: Encoder[ParamsOfParse] = deriveEncoder[ParamsOfParse]
}

case class ParamsOfParseShardstate(boc: String, id: String, workchain_id: Int)

object ParamsOfParseShardstate {
  implicit val encoder: Encoder[ParamsOfParseShardstate] =
    deriveEncoder[ParamsOfParseShardstate]
}

case class ResultOfGetBlockchainConfig(config_boc: String)

object ResultOfGetBlockchainConfig {
  implicit val decoder: Decoder[ResultOfGetBlockchainConfig] =
    deriveDecoder[ResultOfGetBlockchainConfig]
}

case class ResultOfGetBocHash(hash: String)

object ResultOfGetBocHash {
  implicit val decoder: Decoder[ResultOfGetBocHash] =
    deriveDecoder[ResultOfGetBocHash]
}

case class ResultOfGetCodeFromTvc(code: String)

object ResultOfGetCodeFromTvc {
  implicit val decoder: Decoder[ResultOfGetCodeFromTvc] =
    deriveDecoder[ResultOfGetCodeFromTvc]
}

case class ResultOfParse(parsed: Value)
object ResultOfParse {
  implicit val decoder: Decoder[ResultOfParse] =
    deriveDecoder[ResultOfParse]
}
