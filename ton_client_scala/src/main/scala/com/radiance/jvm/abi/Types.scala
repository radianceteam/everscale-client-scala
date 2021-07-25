package com.radiance.jvm.abi

import com.radiance.jvm._
import com.radiance.jvm.boc._
import com.radiance.jvm.crypto._
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
  implicit val encoder: Encoder[Abi] =
    extras.semiauto.deriveConfiguredEncoder[Abi]
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
  implicit val encoder: Encoder[AbiContract] = deriveCodec[AbiContract]
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
  implicit val encoder: Encoder[AbiHandle] = Encoder.instance(h => Json.fromBigInt(h.value))
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
  tvc: String,
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

case class ParamsOfDecodeAccountData(abi: AbiADT.Abi, data: String)

object ParamsOfDecodeAccountData {
  implicit val encoder: Encoder[ParamsOfDecodeAccountData] =
    deriveEncoder[ParamsOfDecodeAccountData]
}

case class ParamsOfDecodeMessage(abi: AbiADT.Abi, message: String)

object ParamsOfDecodeMessage {
  implicit val encoder: Encoder[ParamsOfDecodeMessage] =
    deriveEncoder[ParamsOfDecodeMessage]
}

case class ParamsOfDecodeMessageBody(
  abi: AbiADT.Abi,
  body: String,
  is_internal: Boolean
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
  processing_try_index: Option[Long]
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
  processing_try_index: Option[Long]
)

object ParamsOfEncodeMessageBody {
  implicit val encoder: Encoder[ParamsOfEncodeMessageBody] =
    deriveEncoder[ParamsOfEncodeMessageBody]
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

case class ResultOfDecodeData(data: Value)

object ResultOfDecodeData {
  implicit val decoder: Decoder[ResultOfDecodeData] =
    deriveDecoder[ResultOfDecodeData]
}

case class ResultOfEncodeAccount(account: String, id: String)

object ResultOfEncodeAccount {
  implicit val decoder: Decoder[ResultOfEncodeAccount] =
    deriveDecoder[ResultOfEncodeAccount]
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
