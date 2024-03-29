package com.radiance.jvm.abi

import com.radiance.jvm._
import com.radiance.jvm.boc._
import com.radiance.jvm.crypto._
import io.circe.Decoder.Result
import io.circe._
import io.circe.derivation._
import io.circe.generic.extras

object AbiADT {

  sealed trait Abi

  case class Contract(value: AbiContract) extends Abi

  case class Json(value: String) extends Abi

  case class Handle(value: AbiHandle) extends Abi

  case class Serialized(value: AbiContract) extends Abi

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val codec: Codec[Abi] =
    extras.semiauto.deriveConfiguredCodec[Abi]
}

case class AbiContract(
  `ABI version`: Option[Long],
  abi_version: Option[Long],
  version: Option[Option[String]],
  header: Option[List[String]],
  functions: Option[List[AbiFunction]],
  events: Option[List[AbiEvent]],
  data: Option[List[AbiData]],
  fields: Option[List[AbiParam]]
)

object AbiContract {
  implicit val codec: Codec[AbiContract] = deriveCodec[AbiContract]
}

case class AbiData(
  key: Long,
  name: String,
  `type`: String,
  components: Option[List[AbiParam]]
)

object AbiData {
  implicit val codec: Codec[AbiData] = deriveCodec[AbiData]
}

object AbiErrorCodeEnum {
  sealed trait AbiErrorCode {
    val code: String
  }
  case object AttachSignatureFailed extends AbiErrorCode {
    override val code: String = "307"
  }
  case object EncodeDeployMessageFailed extends AbiErrorCode {
    override val code: String = "305"
  }
  case object EncodeInitialDataFailed extends AbiErrorCode {
    override val code: String = "314"
  }
  case object EncodeRunMessageFailed extends AbiErrorCode {
    override val code: String = "306"
  }
  case object InvalidAbi extends AbiErrorCode {
    override val code: String = "311"
  }
  case object InvalidData extends AbiErrorCode {
    override val code: String = "313"
  }
  case object InvalidFunctionId extends AbiErrorCode {
    override val code: String = "312"
  }
  case object InvalidFunctionName extends AbiErrorCode {
    override val code: String = "315"
  }
  case object InvalidJson extends AbiErrorCode {
    override val code: String = "303"
  }
  case object InvalidMessage extends AbiErrorCode {
    override val code: String = "304"
  }
  case object InvalidSigner extends AbiErrorCode {
    override val code: String = "310"
  }
  case object InvalidTvcImage extends AbiErrorCode {
    override val code: String = "308"
  }
  case object RequiredAddressMissingForEncodeMessage extends AbiErrorCode {
    override val code: String = "301"
  }
  case object RequiredCallSetMissingForEncodeMessage extends AbiErrorCode {
    override val code: String = "302"
  }
  case object RequiredPublicKeyMissingForFunctionHeader extends AbiErrorCode {
    override val code: String = "309"
  }
}

case class AbiEvent(
  name: String,
  inputs: List[AbiParam],
  id: Option[Option[String]]
)

object AbiEvent {
  implicit val codec: Codec[AbiEvent] = deriveCodec[AbiEvent]
}

case class AbiFunction(
  name: String,
  inputs: List[AbiParam],
  outputs: List[AbiParam],
  id: Option[Option[String]]
)

object AbiFunction {
  implicit val codec: Codec[AbiFunction] = deriveCodec[AbiFunction]
}

case class AbiHandle(value: BigInt) extends AnyVal

object AbiHandle {
  implicit val codec: Codec[AbiHandle] = new Codec[AbiHandle]() {
    override def apply(c: HCursor): Result[AbiHandle] = c.value.as[BigInt].map(AbiHandle(_))
    override def apply(a: AbiHandle): Value = Json.fromBigInt(a.value)
  }
}

case class AbiParam(
  name: String,
  `type`: String,
  components: Option[List[AbiParam]]
)

object AbiParam {
  implicit val codec: Codec[AbiParam] = deriveCodec[AbiParam]
}

case class CallSet(
  function_name: String,
  header: Option[FunctionHeader] = None,
  input: Option[Value] = None
)

object CallSet {
  implicit val encoder: Encoder[CallSet] = deriveEncoder[CallSet]
}

object DataLayoutEnum {
  sealed trait DataLayout

  /**
   * Decode message body as function input parameters.
   */
  case object Input extends DataLayout

  /**
   * Decode message body as function output.
   */
  case object Output extends DataLayout

  implicit val codec: Codec[DataLayout] =
    extras.semiauto.deriveEnumerationCodec[DataLayout]
}

case class DecodedMessageBody(
  body_type: MessageBodyTypeEnum.MessageBodyType,
  name: String,
  value: Option[Value],
  header: Option[FunctionHeader]
)

object DecodedMessageBody {
  implicit val decoder: Decoder[DecodedMessageBody] =
    deriveDecoder[DecodedMessageBody]
}

case class DeploySet(
  tvc: Option[String],
  code: Option[String] = None,
  state_init: Option[String] = None,
  workchain_id: Option[Int] = None,
  initial_data: Option[Value] = None,
  initial_pubkey: Option[String] = None
)

object DeploySet {
  implicit val codec: Codec[DeploySet] = deriveCodec[DeploySet]
}

/**
 * The ABI function header.
 *
 * Includes several hidden function parameters that contract uses for security, message delivery monitoring and replay
 * protection reasons.
 *
 * The actual set of header fields depends on the contract's ABI. If a contract's ABI does not include some headers,
 * then they are not filled.
 */
case class FunctionHeader(
  expire: Option[Long],
  time: Option[BigInt],
  pubkey: Option[String]
)

object FunctionHeader {
  implicit val encoder: Codec[FunctionHeader] = deriveCodec[FunctionHeader]
}

object MessageBodyTypeEnum {

  sealed trait MessageBodyType

  /**
   * Message contains the input of the ABI function.
   */
  case object Input extends MessageBodyType

  /**
   * Message contains the output of the ABI function.
   */
  case object Output extends MessageBodyType

  /**
   * Message contains the input of the imported ABI function.
   *
   * Occurs when contract sends an internal message to other contract.
   */
  case object InternalOutput extends MessageBodyType

  /**
   * Message contains the input of the ABI event.
   */
  case object Event extends MessageBodyType

  implicit val decoder: Decoder[MessageBodyType] =
    extras.semiauto.deriveEnumerationDecoder[MessageBodyType]
}

object MessageSourceADT {

  sealed trait MessageSource

  case class Encoded(message: String, abi: Option[AbiADT.Abi]) extends MessageSource

  case class EncodingParams(value: ParamsOfEncodeMessage) extends MessageSource

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[MessageSource] =
    extras.semiauto.deriveConfiguredEncoder[MessageSource]
}

case class ParamsOfAbiEncodeBoc(params: List[AbiParam], data: Value, boc_cache: Option[BocCacheTypeADT.BocCacheType])

object ParamsOfAbiEncodeBoc {
  implicit val encoder: Encoder[ParamsOfAbiEncodeBoc] =
    deriveEncoder[ParamsOfAbiEncodeBoc]
}

case class ParamsOfAttachSignature(
  abi: AbiADT.Abi,
  public_key: String,
  message: String,
  signature: String
)

object ParamsOfAttachSignature {
  implicit val encoder: Encoder[ParamsOfAttachSignature] =
    deriveEncoder[ParamsOfAttachSignature]
}

case class ParamsOfAttachSignatureToMessageBody(
  abi: AbiADT.Abi,
  public_key: String,
  message: String,
  signature: String
)

object ParamsOfAttachSignatureToMessageBody {
  implicit val encoder: Encoder[ParamsOfAttachSignatureToMessageBody] =
    deriveEncoder[ParamsOfAttachSignatureToMessageBody]
}

case class ParamsOfCalcFunctionId(abi: AbiADT.Abi, function_name: String, output: Option[Boolean])

object ParamsOfCalcFunctionId {
  implicit val encoder: Encoder[ParamsOfCalcFunctionId] =
    deriveEncoder[ParamsOfCalcFunctionId]
}

case class ParamsOfDecodeAccountData(abi: AbiADT.Abi, data: String, allow_partial: Option[Boolean])

object ParamsOfDecodeAccountData {
  implicit val encoder: Encoder[ParamsOfDecodeAccountData] =
    deriveEncoder[ParamsOfDecodeAccountData]
}

case class ParamsOfDecodeBoc(params: List[AbiParam], boc: String, allow_partial: Boolean)

object ParamsOfDecodeBoc {
  implicit val encoder: Encoder[ParamsOfDecodeBoc] =
    deriveEncoder[ParamsOfDecodeBoc]
}

case class ParamsOfDecodeInitialData(abi: Option[AbiADT.Abi], data: String, allow_partial: Option[Boolean])

object ParamsOfDecodeInitialData {
  implicit val encoder: Encoder[ParamsOfDecodeInitialData] =
    deriveEncoder[ParamsOfDecodeInitialData]
}

case class ParamsOfDecodeMessage(
  abi: AbiADT.Abi,
  message: String,
  allow_partial: Option[Boolean],
  function_name: Option[String],
  data_layout: Option[DataLayoutEnum.DataLayout]
)

object ParamsOfDecodeMessage {
  implicit val encoder: Encoder[ParamsOfDecodeMessage] =
    deriveEncoder[ParamsOfDecodeMessage]
}

case class ParamsOfDecodeMessageBody(
  abi: AbiADT.Abi,
  body: String,
  is_internal: Boolean,
  allow_partial: Option[Boolean],
  function_name: Option[String],
  data_layout: Option[DataLayoutEnum.DataLayout]
)

object ParamsOfDecodeMessageBody {
  implicit val encoder: Encoder[ParamsOfDecodeMessageBody] =
    deriveEncoder[ParamsOfDecodeMessageBody]
}

case class ParamsOfEncodeAccount(
  state_init: StateInitSourceADT.StateInitSource,
  balance: Option[BigInt],
  last_trans_lt: Option[BigInt],
  last_paid: Option[Long],
  boc_cache: Option[BocCacheTypeADT.BocCacheType]
)

object ParamsOfEncodeAccount {
  implicit val encoder: Encoder[ParamsOfEncodeAccount] =
    deriveEncoder[ParamsOfEncodeAccount]
}

case class ParamsOfEncodeInitialData(
  abi: Option[AbiADT.Abi],
  initial_data: Option[Value],
  initial_pubkey: Option[String],
  boc_cache: Option[BocCacheTypeADT.BocCacheType]
)

object ParamsOfEncodeInitialData {
  implicit val encoder: Encoder[ParamsOfEncodeInitialData] =
    deriveEncoder[ParamsOfEncodeInitialData]
}

case class ParamsOfEncodeInternalMessage(
  abi: Option[AbiADT.Abi],
  address: Option[String],
  src_address: Option[String],
  deploy_set: Option[DeploySet],
  call_set: Option[CallSet],
  value: String,
  bounce: Option[Boolean],
  enable_ihr: Option[Boolean]
)

object ParamsOfEncodeInternalMessage {
  implicit val encoder: Encoder[ParamsOfEncodeInternalMessage] =
    deriveEncoder[ParamsOfEncodeInternalMessage]
}

case class ParamsOfEncodeMessage(
  abi: AbiADT.Abi,
  address: Option[String],
  deploy_set: Option[DeploySet],
  call_set: Option[CallSet],
  signer: SignerADT.Signer,
  processing_try_index: Option[Long],
  signature_id: Option[Int]
)

object ParamsOfEncodeMessage {
  implicit val encoder: Encoder[ParamsOfEncodeMessage] =
    deriveEncoder[ParamsOfEncodeMessage]
}

case class ParamsOfEncodeMessageBody(
  abi: AbiADT.Abi,
  call_set: CallSet,
  is_internal: Boolean,
  signer: SignerADT.Signer,
  processing_try_index: Option[Long],
  address: Option[String],
  signature_id: Option[Int]
)

object ParamsOfEncodeMessageBody {
  implicit val encoder: Encoder[ParamsOfEncodeMessageBody] =
    deriveEncoder[ParamsOfEncodeMessageBody]
}

case class ParamsOfGetSignatureData(abi: AbiADT.Abi, message: String, signature_id: Option[Int])

object ParamsOfGetSignatureData {
  implicit val encoder: Encoder[ParamsOfGetSignatureData] =
    deriveEncoder[ParamsOfGetSignatureData]
}

case class ParamsOfUpdateInitialData(
  abi: Option[AbiADT.Abi],
  data: String,
  initial_data: Option[Value],
  initial_pubkey: Option[String],
  boc_cache: Option[BocCacheTypeADT.BocCacheType]
)

object ParamsOfUpdateInitialData {
  implicit val encoder: Encoder[ParamsOfUpdateInitialData] =
    deriveEncoder[ParamsOfUpdateInitialData]
}

case class ResultOfAbiEncodeBoc(boc: String)

object ResultOfAbiEncodeBoc {
  implicit val decoder: Decoder[ResultOfAbiEncodeBoc] =
    deriveDecoder[ResultOfAbiEncodeBoc]
}

case class ResultOfAttachSignature(message: String, message_id: String)

object ResultOfAttachSignature {
  implicit val decoder: Decoder[ResultOfAttachSignature] =
    deriveDecoder[ResultOfAttachSignature]
}

case class ResultOfAttachSignatureToMessageBody(body: String)

object ResultOfAttachSignatureToMessageBody {
  implicit val decoder: Decoder[ResultOfAttachSignatureToMessageBody] =
    deriveDecoder[ResultOfAttachSignatureToMessageBody]
}

case class ResultOfCalcFunctionId(function_id: Long)

object ResultOfCalcFunctionId {
  implicit val decoder: Decoder[ResultOfCalcFunctionId] =
    deriveDecoder[ResultOfCalcFunctionId]
}

case class ResultOfDecodeAccountData(data: Value)

object ResultOfDecodeAccountData {
  implicit val decoder: Decoder[ResultOfDecodeAccountData] =
    deriveDecoder[ResultOfDecodeAccountData]
}

case class ResultOfDecodeBoc(data: Value)

object ResultOfDecodeBoc {
  implicit val decoder: Decoder[ResultOfDecodeBoc] =
    deriveDecoder[ResultOfDecodeBoc]
}

case class ResultOfDecodeInitialData(initial_data: Option[Value], initial_pubkey: String)

object ResultOfDecodeInitialData {
  implicit val decoder: Decoder[ResultOfDecodeInitialData] =
    deriveDecoder[ResultOfDecodeInitialData]
}

case class ResultOfEncodeAccount(account: String, id: String)

object ResultOfEncodeAccount {
  implicit val decoder: Decoder[ResultOfEncodeAccount] =
    deriveDecoder[ResultOfEncodeAccount]
}

case class ResultOfEncodeInitialData(data: String)

object ResultOfEncodeInitialData {
  implicit val decoder: Decoder[ResultOfEncodeInitialData] =
    deriveDecoder[ResultOfEncodeInitialData]
}

case class ResultOfEncodeInternalMessage(
  message: String,
  address: String,
  message_id: String
)

object ResultOfEncodeInternalMessage {
  implicit val decoder: Decoder[ResultOfEncodeInternalMessage] =
    deriveDecoder[ResultOfEncodeInternalMessage]
}

case class ResultOfEncodeMessage(
  message: String,
  data_to_sign: Option[String],
  address: String,
  message_id: String
)

object ResultOfEncodeMessage {
  implicit val decoder: Decoder[ResultOfEncodeMessage] =
    deriveDecoder[ResultOfEncodeMessage]
}

case class ResultOfEncodeMessageBody(
  body: String,
  data_to_sign: Option[String]
)

object ResultOfEncodeMessageBody {
  implicit val decoder: Decoder[ResultOfEncodeMessageBody] =
    deriveDecoder[ResultOfEncodeMessageBody]
}

case class ResultOfGetSignatureData(signature: String, unsigned: String)

object ResultOfGetSignatureData {
  implicit val decoder: Decoder[ResultOfGetSignatureData] =
    deriveDecoder[ResultOfGetSignatureData]
}

case class ResultOfUpdateInitialData(data: String)

object ResultOfUpdateInitialData {
  implicit val decoder: Decoder[ResultOfUpdateInitialData] =
    deriveDecoder[ResultOfUpdateInitialData]
}

object SignerADT {

  sealed trait Signer

  case object None extends Signer

  case class External(public_key: String) extends Signer

  case class Keys(keys: KeyPair) extends Signer

  case class SigningBox(handle: SigningBoxHandle) extends Signer

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[Signer] =
    extras.semiauto.deriveConfiguredEncoder[Signer]
}

case class StateInitParams(abi: AbiADT.Abi, value: Value)

object StateInitParams {
  implicit val encoder: Encoder[StateInitParams] =
    deriveEncoder[StateInitParams]
}

object StateInitSourceADT {

  sealed trait StateInitSource

  case class Message(source: MessageSourceADT.MessageSource) extends StateInitSource

  case class StateInit(code: String, data: String, library: Option[String]) extends StateInitSource

  case class Tvc(
    tvc: String,
    public_key: Option[String],
    init_params: Option[StateInitParams]
  ) extends StateInitSource

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[StateInitSource] =
    extras.semiauto.deriveConfiguredEncoder[StateInitSource]
}
