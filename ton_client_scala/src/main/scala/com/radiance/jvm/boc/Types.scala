package com.radiance.jvm.boc

import com.radiance.jvm._
import io.circe.derivation._
import io.circe._
import io.circe.generic.extras

object BocCacheTypeADT {
  sealed trait BocCacheType

  case class Pinned(pin: String) extends BocCacheType

  case object Unpinned extends BocCacheType

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[BocCacheType] =
    extras.semiauto.deriveConfiguredEncoder[BocCacheType]
}

object BocErrorCodeEnum {
  sealed trait BocErrorCode {
    val code: String
  }

  case object BocRefNotFound extends BocErrorCode {
    override val code: String = "206"
  }

  case object InappropriateBlock extends BocErrorCode {
    override val code: String = "203"
  }

  case object InsufficientCacheSize extends BocErrorCode {
    override val code: String = "205"
  }

  case object InvalidBoc extends BocErrorCode {
    override val code: String = "201"
  }

  case object InvalidBocRef extends BocErrorCode {
    override val code: String = "207"
  }

  case object MissingSourceBoc extends BocErrorCode {
    override val code: String = "204"
  }

  case object SerializationError extends BocErrorCode {
    override val code: String = "202"
  }
}

object BuilderOpADT {

  /**
   * Cell builder operation.
   */
  sealed trait BuilderOp

  /**
   * Cell builder operation.
   */
  case class BitString(value: String) extends BuilderOp

  /**
   * Cell builder operation.
   */
  case class Cell(builder: List[BuilderOp]) extends BuilderOp

  /**
   * Cell builder operation.
   */
  case class CellBoc(boc: String) extends BuilderOp

  /**
   * Cell builder operation.
   */
  case class Integer(size: Long, value: Value) extends BuilderOp

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[BuilderOp] =
    extras.semiauto.deriveConfiguredEncoder[BuilderOp]
}

case class ParamsOfBocCacheGet(boc_ref: String)

object ParamsOfBocCacheGet {
  implicit val encoder: Encoder[ParamsOfBocCacheGet] =
    deriveEncoder[ParamsOfBocCacheGet]
}

case class ParamsOfBocCacheSet(boc: String, cache_type: BocCacheTypeADT.BocCacheType)

object ParamsOfBocCacheSet {
  implicit val encoder: Encoder[ParamsOfBocCacheSet] =
    deriveEncoder[ParamsOfBocCacheSet]
}

case class ParamsOfBocCacheUnpin(pin: String, boc_ref: Option[String])

object ParamsOfBocCacheUnpin {
  implicit val encoder: Encoder[ParamsOfBocCacheUnpin] =
    deriveEncoder[ParamsOfBocCacheUnpin]
}

case class ParamsOfEncodeBoc(builder: List[BuilderOpADT.BuilderOp], boc_cache: Option[BocCacheTypeADT.BocCacheType])

object ParamsOfEncodeBoc {
  implicit val encoder: Encoder[ParamsOfEncodeBoc] =
    deriveEncoder[ParamsOfEncodeBoc]
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
case class ResultOfBocCacheGet(boc: Option[String])

object ResultOfBocCacheGet {
  implicit val decoder: Decoder[ResultOfBocCacheGet] =
    deriveDecoder[ResultOfBocCacheGet]
}

case class ResultOfBocCacheSet(boc_ref: String)

object ResultOfBocCacheSet {
  implicit val decoder: Decoder[ResultOfBocCacheSet] =
    deriveDecoder[ResultOfBocCacheSet]
}

case class ResultOfEncodeBoc(boc: String)

object ResultOfEncodeBoc {
  implicit val decoder: Decoder[ResultOfEncodeBoc] =
    deriveDecoder[ResultOfEncodeBoc]
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
