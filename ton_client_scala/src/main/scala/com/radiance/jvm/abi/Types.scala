package com.radiance.jvm.abi

import com.radiance.jvm.Utils.generateType
import com.radiance.jvm._
import com.radiance.jvm.crypto._
import io.circe._
import io.circe.derivation._

sealed trait Abi

object Abi {
  import io.circe.syntax._
  case class Contract(value: AbiContract) extends Abi
  object Contract {
    implicit val encoder: Encoder[Contract] = deriveEncoder[Contract]
  }

  case class Json(value: String) extends Abi
  object Json {
    implicit val encoder: Encoder[Json] = deriveEncoder[Json]
  }

  case class Handle(value: AbiHandle) extends Abi
  object Handle {
    implicit val encoder: Encoder[Handle] = deriveEncoder[Handle]
  }

  case class Serialized(value: AbiContract) extends Abi
  object Serialized {
    implicit val encoder: Encoder[Serialized] = deriveEncoder[Serialized]
  }

  implicit val encoder: Encoder[Abi] = {
    case a: Contract   => a.asJson.deepMerge(generateType(a))
    case a: Json       => a.asJson.deepMerge(generateType(a))
    case a: Handle     => a.asJson.deepMerge(generateType(a))
    case a: Serialized => a.asJson.deepMerge(generateType(a))
  }
}

case class AbiContract(
  `ABI version`: Option[Long],
  abi_version: Option[Long],
  header: Option[List[String]],
  functions: Option[List[AbiFunction]],
  events: Option[List[AbiEvent]],
  data: Option[List[AbiData]]
)

object AbiContract {
  implicit val encoder: Encoder[AbiContract] = deriveCodec[AbiContract]
}

case class AbiData(
  key: BigInt,
  name: String,
  `type`: String,
  components: Option[List[AbiParam]]
)

object AbiData {
  implicit val codec: Codec[AbiData] = deriveCodec[AbiData]
}

sealed trait AbiErrorCode {
  val code: String
}

object AbiErrorCode {

  case object RequiredAddressMissingForEncodeMessage extends AbiErrorCode {
    override val code: String = "301"
  }

  case object RequiredCallSetMissingForEncodeMessage extends AbiErrorCode {
    override val code: String = "302"
  }

  case object InvalidJson extends AbiErrorCode {
    override val code: String = "303"
  }

  case object InvalidMessage extends AbiErrorCode {
    override val code: String = "304"
  }

  case object EncodeDeployMessageFailed extends AbiErrorCode {
    override val code: String = "305"
  }

  case object EncodeRunMessageFailed extends AbiErrorCode {
    override val code: String = "306"
  }

  case object AttachSignatureFailed extends AbiErrorCode {
    override val code: String = "307"
  }

  case object InvalidTvcImage extends AbiErrorCode {
    override val code: String = "308"
  }

  case object RequiredPublicKeyMissingForFunctionHeader extends AbiErrorCode {
    override val code: String = "309"
  }

  case object InvalidSigner extends AbiErrorCode {
    override val code: String = "310"
  }

  case object InvalidAbi extends AbiErrorCode {
    override val code: String = "311"
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

case class AbiHandle(value: BigInt)

object AbiHandle {
  implicit val encoder: Encoder[AbiHandle] = deriveEncoder[AbiHandle]
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
  body_type: MessageBodyType,
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
  initial_data: Option[Value] = None
)

object DeploySet {
  implicit val encoder: Encoder[DeploySet] = deriveEncoder[DeploySet]
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

sealed trait MessageBodyType

object MessageBodyType {

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

  implicit val decoder: Decoder[MessageBodyType] = Decoder[String].emap {
    case "Input"          => Right(Input)
    case "Output"         => Right(Output)
    case "InternalOutput" => Right(InternalOutput)
    case "Event"          => Right(Event)
    case x                => Left(s"Can't read MessageBodyType from $x")
  }
}

sealed trait MessageSource

object MessageSource {
  import io.circe.syntax._

  case class Encoded(message: String, abi: Option[Abi]) extends MessageSource
  object Encoded {
    implicit val encoder: Encoder[Encoded] = deriveEncoder[Encoded]
  }

  case class EncodingParams(value: ParamsOfEncodeMessage) extends MessageSource
  object EncodingParams {
    implicit val encoder: Encoder[EncodingParams] =
      deriveEncoder[EncodingParams]
  }

  implicit val encoder: Encoder[MessageSource] = {
    case a: Encoded        => a.asJson.deepMerge(generateType(a))
    case a: EncodingParams => a.asJson.deepMerge(generateType(a))
  }
}

case class ParamsOfAttachSignature(
  abi: Abi,
  public_key: String,
  message: String,
  signature: String
) extends Bind {
  override type Out = ResultOfAttachSignature
  override val decoder: Decoder[ResultOfAttachSignature] =
    implicitly[Decoder[ResultOfAttachSignature]]
}

object ParamsOfAttachSignature {
  implicit val encoder: Encoder[ParamsOfAttachSignature] =
    deriveEncoder[ParamsOfAttachSignature]
}

case class ParamsOfAttachSignatureToMessageBody(
  abi: Abi,
  public_key: String,
  message: String,
  signature: String
) extends Bind {
  override type Out = ResultOfAttachSignatureToMessageBody
  override val decoder: Decoder[ResultOfAttachSignatureToMessageBody] =
    implicitly[Decoder[ResultOfAttachSignatureToMessageBody]]
}

object ParamsOfAttachSignatureToMessageBody {
  implicit val encoder: Encoder[ParamsOfAttachSignatureToMessageBody] =
    deriveEncoder[ParamsOfAttachSignatureToMessageBody]
}

case class ParamsOfDecodeMessage(abi: Abi, message: String) extends Bind {
  override type Out = DecodedMessageBody
  override val decoder: Decoder[DecodedMessageBody] =
    implicitly[Decoder[DecodedMessageBody]]
}

object ParamsOfDecodeMessage {
  implicit val encoder: Encoder[ParamsOfDecodeMessage] =
    deriveEncoder[ParamsOfDecodeMessage]
}

case class ParamsOfDecodeMessageBody(
  abi: Abi,
  body: String,
  is_internal: Boolean
) extends Bind {
  override type Out = DecodedMessageBody
  override val decoder: Decoder[DecodedMessageBody] =
    implicitly[Decoder[DecodedMessageBody]]
}

object ParamsOfDecodeMessageBody {
  implicit val encoder: Encoder[ParamsOfDecodeMessageBody] =
    deriveEncoder[ParamsOfDecodeMessageBody]
}

case class ParamsOfEncodeAccount(
  state_init: StateInitSource,
  balance: Option[BigInt],
  last_trans_lt: Option[BigInt],
  last_paid: Option[Long]
) extends Bind {
  override type Out = ResultOfEncodeAccount
  override val decoder: Decoder[ResultOfEncodeAccount] =
    implicitly[Decoder[ResultOfEncodeAccount]]
}

object ParamsOfEncodeAccount {
  implicit val encoder: Encoder[ParamsOfEncodeAccount] =
    deriveEncoder[ParamsOfEncodeAccount]
}

case class ParamsOfEncodeMessage(
  abi: Abi,
  address: Option[String],
  deploy_set: Option[DeploySet],
  call_set: Option[CallSet],
  signer: Signer,
  processing_try_index: Option[Long]
) extends Bind {
  override type Out = ResultOfEncodeMessage
  override val decoder: Decoder[ResultOfEncodeMessage] =
    implicitly[Decoder[ResultOfEncodeMessage]]
}

object ParamsOfEncodeMessage {
  implicit val encoder: Encoder[ParamsOfEncodeMessage] =
    deriveEncoder[ParamsOfEncodeMessage]
}

case class ParamsOfEncodeMessageBody(
  abi: Abi,
  call_set: CallSet,
  is_internal: Boolean,
  signer: Signer,
  processing_try_index: Option[Long]
) extends Bind {
  override type Out = ResultOfEncodeMessageBody
  override val decoder: Decoder[ResultOfEncodeMessageBody] =
    implicitly[Decoder[ResultOfEncodeMessageBody]]
}

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

case class ResultOfEncodeAccount(account: String, id: String)

object ResultOfEncodeAccount {
  implicit val decoder: Decoder[ResultOfEncodeAccount] =
    deriveDecoder[ResultOfEncodeAccount]
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

sealed trait Signer

object Signer {
  import io.circe.Json._
  import io.circe.syntax._
  case object None extends Signer

  case class External(public_key: String) extends Signer
  object External {
    implicit val encoder: Encoder[External] = deriveEncoder[External]
  }

  case class Keys(keys: KeyPair) extends Signer
  object Keys {
    implicit val encoder: Encoder[Keys] = deriveEncoder[Keys]
  }

  case class SigningBox(handle: SigningBoxHandle) extends Signer
  object SigningBox {
    implicit val encoder: Encoder[SigningBox] = deriveEncoder[SigningBox]
  }

  implicit val encoder: Encoder[Signer] = {
    case None          => fromFields(Seq("type" -> fromString("None")))
    case a: External   => a.asJson.deepMerge(generateType(a))
    case a: Keys       => a.asJson.deepMerge(generateType(a))
    case a: SigningBox => a.asJson.deepMerge(generateType(a))
  }
}

case class StateInitParams(abi: Abi, value: Value)

object StateInitParams {
  implicit val encoder: Encoder[StateInitParams] =
    deriveEncoder[StateInitParams]
}

sealed trait StateInitSource

object StateInitSource {
  import io.circe.syntax._

  case class Message(source: MessageSource) extends StateInitSource
  object Message {
    implicit val encoder: Encoder[Message] = deriveEncoder[Message]
  }

  case class StateInit(code: String, data: String, library: Option[String]) extends StateInitSource
  object StateInit {
    implicit val encoder: Encoder[StateInit] = deriveEncoder[StateInit]
  }

  case class Tvc(
    tvc: String,
    public_key: Option[String],
    init_params: Option[StateInitParams]
  ) extends StateInitSource
  object Tvc {
    implicit val encoder: Encoder[Tvc] = deriveEncoder[Tvc]
  }

  implicit val encoder: Encoder[StateInitSource] = {
    case a: Message   => a.asJson.deepMerge(generateType(a))
    case a: StateInit => a.asJson.deepMerge(generateType(a))
    case a: Tvc       => a.asJson.deepMerge(generateType(a))
  }
}
