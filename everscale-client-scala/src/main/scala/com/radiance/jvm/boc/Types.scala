package com.radiance.jvm.boc

import com.radiance.jvm._
import io.circe.derivation._
import io.circe._
import io.circe.generic.extras

object BocCacheTypeADT {
  sealed trait BocCacheType

  case class Pinned(pin: String) extends BocCacheType

  /**
   * BOC is placed into a common BOC pool with limited size regulated by LRU (least recently used) cache lifecycle. BOC
   * resides there until it is replaced with other BOCs if it is not used
   */
  case object Unpinned extends BocCacheType

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val codec: Codec[BocCacheType] =
    extras.semiauto.deriveConfiguredCodec[BocCacheType]
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
  case class Address(address: String) extends BuilderOp

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

case class ParamsOfDecodeStateInit(state_init: String, boc_cache: Option[BocCacheTypeADT.BocCacheType])

object ParamsOfDecodeStateInit {
  implicit val encoder: Encoder[ParamsOfDecodeStateInit] =
    deriveEncoder[ParamsOfDecodeStateInit]
}

case class ParamsOfDecodeTvc(tvc: String)

object ParamsOfDecodeTvc {
  implicit val encoder: Encoder[ParamsOfDecodeTvc] =
    deriveEncoder[ParamsOfDecodeTvc]
}

case class ParamsOfEncodeBoc(builder: List[BuilderOpADT.BuilderOp], boc_cache: Option[BocCacheTypeADT.BocCacheType])

object ParamsOfEncodeBoc {
  implicit val encoder: Encoder[ParamsOfEncodeBoc] =
    deriveEncoder[ParamsOfEncodeBoc]
}

case class ParamsOfEncodeExternalInMessage(
  src: Option[String],
  dst: String,
  init: Option[String],
  body: Option[String],
  boc_cache: Option[BocCacheTypeADT.BocCacheType]
)

object ParamsOfEncodeExternalInMessage {
  implicit val encoder: Encoder[ParamsOfEncodeExternalInMessage] =
    deriveEncoder[ParamsOfEncodeExternalInMessage]
}

case class ParamsOfEncodeStateInit(
  code: Option[String],
  data: Option[String],
  library: Option[String],
  tick: Option[Boolean],
  tock: Option[Boolean],
  split_depth: Option[Long],
  boc_cache: Option[BocCacheTypeADT.BocCacheType]
)

object ParamsOfEncodeStateInit {
  implicit val encoder: Encoder[ParamsOfEncodeStateInit] =
    deriveEncoder[ParamsOfEncodeStateInit]
}

case class ParamsOfGetBlockchainConfig(block_boc: String)

object ParamsOfGetBlockchainConfig {
  implicit val encoder: Encoder[ParamsOfGetBlockchainConfig] =
    deriveEncoder[ParamsOfGetBlockchainConfig]
}

case class ParamsOfGetBocDepth(boc: String)

object ParamsOfGetBocDepth {
  implicit val encoder: Encoder[ParamsOfGetBocDepth] =
    deriveEncoder[ParamsOfGetBocDepth]
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

case class ParamsOfGetCodeSalt(code: String, boc_cache: Option[BocCacheTypeADT.BocCacheType])

object ParamsOfGetCodeSalt {
  implicit val encoder: Encoder[ParamsOfGetCodeSalt] =
    deriveEncoder[ParamsOfGetCodeSalt]
}

case class ParamsOfGetCompilerVersion(code: String)

object ParamsOfGetCompilerVersion {
  implicit val encoder: Encoder[ParamsOfGetCompilerVersion] =
    deriveEncoder[ParamsOfGetCompilerVersion]
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

case class ParamsOfSetCodeSalt(code: String, salt: String, boc_cache: Option[BocCacheTypeADT.BocCacheType])

object ParamsOfSetCodeSalt {
  implicit val encoder: Encoder[ParamsOfSetCodeSalt] =
    deriveEncoder[ParamsOfSetCodeSalt]
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

case class ResultOfDecodeStateInit(
  code: Option[String],
  code_hash: Option[String],
  code_depth: Option[Long],
  data: Option[String],
  data_hash: Option[String],
  data_depth: Option[Long],
  library: Option[String],
  tick: Option[Boolean],
  tock: Option[Boolean],
  split_depth: Option[Long],
  compiler_version: Option[String]
)

object ResultOfDecodeStateInit {
  implicit val decoder: Decoder[ResultOfDecodeStateInit] =
    deriveDecoder[ResultOfDecodeStateInit]
}

case class ResultOfDecodeTvc(tvc: TvcADT.Tvc)

object ResultOfDecodeTvc {
  implicit val decoder: Decoder[ResultOfDecodeTvc] =
    deriveDecoder[ResultOfDecodeTvc]
}

case class ResultOfEncodeBoc(boc: String)

object ResultOfEncodeBoc {
  implicit val decoder: Decoder[ResultOfEncodeBoc] =
    deriveDecoder[ResultOfEncodeBoc]
}

case class ResultOfEncodeExternalInMessage(message: String, message_id: String)

object ResultOfEncodeExternalInMessage {
  implicit val decoder: Decoder[ResultOfEncodeExternalInMessage] =
    deriveDecoder[ResultOfEncodeExternalInMessage]
}

case class ResultOfEncodeStateInit(state_init: String)

object ResultOfEncodeStateInit {
  implicit val decoder: Decoder[ResultOfEncodeStateInit] =
    deriveDecoder[ResultOfEncodeStateInit]
}

case class ResultOfGetBlockchainConfig(config_boc: String)

object ResultOfGetBlockchainConfig {
  implicit val decoder: Decoder[ResultOfGetBlockchainConfig] =
    deriveDecoder[ResultOfGetBlockchainConfig]
}

case class ResultOfGetBocDepth(depth: Long)

object ResultOfGetBocDepth {
  implicit val decoder: Decoder[ResultOfGetBocDepth] =
    deriveDecoder[ResultOfGetBocDepth]
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

case class ResultOfGetCodeSalt(salt: Option[String])

object ResultOfGetCodeSalt {
  implicit val decoder: Decoder[ResultOfGetCodeSalt] =
    deriveDecoder[ResultOfGetCodeSalt]
}

case class ResultOfGetCompilerVersion(version: Option[String])

object ResultOfGetCompilerVersion {
  implicit val decoder: Decoder[ResultOfGetCompilerVersion] =
    deriveDecoder[ResultOfGetCompilerVersion]
}

case class ResultOfParse(parsed: Value)

object ResultOfParse {
  implicit val decoder: Decoder[ResultOfParse] =
    deriveDecoder[ResultOfParse]
}

case class ResultOfSetCodeSalt(code: String)

object ResultOfSetCodeSalt {
  implicit val decoder: Decoder[ResultOfSetCodeSalt] = deriveDecoder[ResultOfSetCodeSalt]
}

// TODO sdvornik add serializer
object TvcADT {
  sealed trait Tvc

  case class V1(value: TvcV1) extends Tvc

  import com.radiance.jvm.DiscriminatorConfig._

  implicit val decoder: Decoder[Tvc] =
    extras.semiauto.deriveConfiguredDecoder[Tvc]
}

case class TvcV1(code: Option[String], description: Option[String])

object TvcV1 {
  implicit val decoder: Decoder[TvcV1] =
    deriveDecoder[TvcV1]
}
