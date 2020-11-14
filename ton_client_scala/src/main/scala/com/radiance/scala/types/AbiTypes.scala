package com.radiance.scala.types

import com.radiance.scala.types.CryptoTypes.{KeyPair, SigningBoxHandle}
import io.circe
import io.circe.derivation.{deriveCodec, deriveDecoder, deriveEncoder}
import io.circe.{Codec, Decoder, Encoder}
import io.circe.syntax._
import Utils._

object AbiTypes {
  type Value = circe.Json

  sealed trait Abi

  case class Contract(value: AbiContract) extends Abi

  case class Json(value: String) extends Abi

  case class Handle(value: AbiHandle) extends Abi

  case class Serialized(value: AbiContract) extends Abi

  case class AbiHandle(value: BigInt)

  /**
   * The ABI function header.
   *
   * Includes several hidden function parameters that contract
   * uses for security, message delivery monitoring and replay protection reasons.
   *
   * The actual set of header fields depends on the contract's ABI.
   * If a contract's ABI does not include some headers, then they are not filled.
   */
  case class FunctionHeader(expire: Option[Long], time: Option[BigInt], pubkey: Option[String])

  case class CallSet(function_name: String, header: Option[FunctionHeader], input: Option[Value])

  case class DeploySet(tvc: String, workchain_id: Option[Int], initial_data: Option[Value])

  sealed trait Signer

  case object SignerNone extends Signer

  case class External(public_key: String) extends Signer

  case class Keys(keys: KeyPair) extends Signer

  case class SigningBox(handle: SigningBoxHandle) extends Signer

  sealed trait MessageBodyType

  /** Message contains the input of the ABI function. */
  case object Input extends MessageBodyType

  /** Message contains the output of the ABI function. */
  case object Output extends MessageBodyType

  /**
   * Message contains the input of the imported ABI function.
   *
   * Occurs when contract sends an internal message to other
   * contract.
   */
  object InternalOutput extends MessageBodyType

  /** Message contains the input of the ABI event. */
  object Event extends MessageBodyType

  sealed trait StateInitSource

  case class Message(source: MessageSource) extends StateInitSource

  case class StateInit(code: String, data: String, library: Option[String]) extends StateInitSource

  case class Tvc(tvc: String, public_key: Option[String], init_params: Option[StateInitParams]) extends StateInitSource

  case class StateInitParams(abi: Abi, value: Value)

  sealed trait MessageSource

  case class Encoded(message: String, abi: Option[Abi]) extends MessageSource

  case class EncodingParams(value: ParamsOfEncodeMessage) extends MessageSource

  case class AbiParam(name: String, `type`: String, components: Option[List[AbiParam]])

  case class AbiEvent(name: String, inputs: List[AbiParam], id: Option[Option[Long]])

  case class AbiData(key: BigInt, name: String, `type`: String, components: Option[List[AbiParam]])

  case class AbiFunction(name: String, inputs: List[AbiParam], outputs: List[AbiParam], id: Option[Option[Long]])

  case class AbiContract(`ABI version`: Option[Long], abi_version: Option[Long], header: Option[List[String]], functions: Option[List[AbiFunction]], events: Option[List[AbiEvent]], data: Option[List[AbiData]])

  case class ParamsOfEncodeMessageBody(abi: Abi, call_set: CallSet, is_internal: Boolean, signer: Signer, processing_try_index: Option[Long]) extends ApiNew {
    override type Out = ResultOfEncodeMessageBody
    override val decoder: Decoder[ResultOfEncodeMessageBody] = implicitly[Decoder[ResultOfEncodeMessageBody]]
  }

  case class ResultOfEncodeMessageBody(body: String, data_to_sign: Option[String])

  case class ParamsOfAttachSignatureToMessageBody(abi: Abi, public_key: String, message: String, signature: String) extends ApiNew {
    override type Out = ResultOfAttachSignatureToMessageBody
    override val decoder: Decoder[ResultOfAttachSignatureToMessageBody] = implicitly[Decoder[ResultOfAttachSignatureToMessageBody]]
  }

  case class ResultOfAttachSignatureToMessageBody(body: String)

  case class ParamsOfEncodeMessage(abi: Abi, address: Option[String], deploy_set: Option[DeploySet], call_set: Option[CallSet], signer: Signer, processing_try_index: Option[Long]) extends ApiNew {
    override type Out = ResultOfEncodeMessage
    override val decoder: Decoder[ResultOfEncodeMessage] = implicitly[Decoder[ResultOfEncodeMessage]]
  }

  case class ResultOfEncodeMessage(message: String, data_to_sign: Option[String], address: String, message_id: String)

  case class ParamsOfAttachSignature(abi: Abi, public_key: String, message: String, signature: String) extends ApiNew {
    override type Out = ResultOfAttachSignature
    override val decoder: Decoder[ResultOfAttachSignature] = implicitly[Decoder[ResultOfAttachSignature]]
  }

  case class ResultOfAttachSignature(message: String, message_id: String)

  case class ParamsOfDecodeMessage(abi: Abi, message: String)

  case class DecodedMessageBody(body_type: MessageBodyType, name: String, value: Option[Value], header: Option[FunctionHeader])

  case class ParamsOfDecodeMessageBody(abi: Abi, body: String, is_internal: Boolean) extends ApiNew {
    override type Out = DecodedMessageBody
    override val decoder: Decoder[DecodedMessageBody] = implicitly[Decoder[DecodedMessageBody]]
  }

  case class ParamsOfEncodeAccount(state_init: StateInitSource, balance: Option[BigInt], last_trans_lt: Option[BigInt], last_paid: Option[Long]) extends ApiNew {
    override type Out = ResultOfEncodeAccount
    override val decoder: Decoder[ResultOfEncodeAccount] = implicitly[Decoder[ResultOfEncodeAccount]]
  }

  case class ResultOfEncodeAccount(account: String, id: String)


  object Abi {

    implicit val AbiEncoder: Encoder[Abi] = {
      case a: Contract => a.asJson.deepMerge(generateType(a))
      case a: Json => a.asJson.deepMerge(generateType(a))
      case a: Handle => a.asJson.deepMerge(generateType(a))
      case a: Serialized => a.asJson.deepMerge(generateType(a))
    }
  }

  object Contract {
    implicit val ContractEncoder: Encoder[Contract] = deriveEncoder[Contract]
  }

  object Json {
    implicit val JsonEncoder: Encoder[Json] = deriveEncoder[Json]
  }

  object Handle {
    implicit val HandleEncoder: Encoder[Handle] = deriveEncoder[Handle]
  }

  object Serialized {
    implicit val SerializedEncoder: Encoder[Serialized] = deriveEncoder[Serialized]
  }

  object AbiHandle {
    implicit val AbiHandleEncoder: Encoder[AbiHandle] = deriveEncoder[AbiHandle]
  }

  object FunctionHeader {
    implicit val FunctionHeaderEncoder: Codec[FunctionHeader] = deriveCodec[FunctionHeader]
  }

  object CallSet {
    implicit val CallSetEncoder: Encoder[CallSet] = deriveEncoder[CallSet]
  }

  object DeploySet {
    implicit val DeploySetEncoder: Encoder[DeploySet] = deriveEncoder[DeploySet]
  }

  object Signer {
    implicit val SignerEncoder: Encoder[Signer] = {
      case SignerNone => circe.Json.fromFields(Seq(("type" -> "None".asJson)))
      case a: External => a.asJson.deepMerge(generateType(a))
      case a: Keys => a.asJson.deepMerge(generateType(a))
      case a: SigningBox => a.asJson.deepMerge(generateType(a))
    }
  }

  object External {
    implicit val ExternalEncoder: Encoder[External] = deriveEncoder[External]
  }

  object Keys {
    implicit val KeysEncoder: Encoder[Keys] = deriveEncoder[Keys]
  }

  object SigningBox {
    implicit val SigningBoxEncoder: Encoder[SigningBox] = deriveEncoder[SigningBox]
  }

  object MessageBodyType {
    implicit val MessageBodyTypeDecoder: Decoder[MessageBodyType] = Decoder[String].emap {
      case "Input" => Right(Input)
      case "Output" => Right(Output)
      case "InternalOutput" => Right(InternalOutput)
      case "Event" => Right(Event)
      case x => Left(s"Can't read MessageBodyType from $x")
    }
  }

  object StateInitSource {
    implicit val StateInitSourceEncoder: Encoder[StateInitSource] = {
      case a: Message => a.asJson.deepMerge(generateType(a))
      case a: StateInit => a.asJson.deepMerge(generateType(a))
      case a: Tvc => a.asJson.deepMerge(generateType(a))
    }
  }

  object Message {
    implicit val MessageEncoder: Encoder[Message] = deriveEncoder[Message]
  }

  object StateInit {
    implicit val StateInitEncoder: Encoder[StateInit] = deriveEncoder[StateInit]
  }

  object Tvc {
    implicit val StateInitEncoder: Encoder[Tvc] = deriveEncoder[Tvc]
  }

  object StateInitParams {
    implicit val StateInitParamsEncoder: Encoder[StateInitParams] = deriveEncoder[StateInitParams]
  }

  object MessageSource {
    implicit val MessageSourceEncoder: Encoder[MessageSource] = {
      case a: Encoded => a.asJson.deepMerge(generateType(a))
      case a: EncodingParams => a.asJson.deepMerge(generateType(a))
    }
  }

  object Encoded {
    implicit val EncodedEncoder: Encoder[Encoded] = deriveEncoder[Encoded]
  }

  object EncodingParams {
    implicit val EncodingParamsEncoder: Encoder[EncodingParams] = deriveEncoder[EncodingParams]
  }

  object AbiParam {
    implicit val AbiParamCodec: Codec[AbiParam] = deriveCodec[AbiParam]
  }

  object AbiEvent {
    implicit val AbiEventCodec: Codec[AbiEvent] = deriveCodec[AbiEvent]
  }

  object AbiData {
    implicit val AbiDataCodec: Codec[AbiData] = deriveCodec[AbiData]
  }

  object AbiFunction {
    implicit val AbiFunctionCodec: Codec[AbiFunction] = deriveCodec[AbiFunction]
  }

  object AbiContract {
    implicit val AbiContractEncoder: Encoder[AbiContract] = deriveCodec[AbiContract]
  }

  object ParamsOfEncodeMessageBody {
    implicit val ParamsOfEncodeMessageBodyEncoder: Encoder[ParamsOfEncodeMessageBody] = deriveEncoder[ParamsOfEncodeMessageBody]
  }

  object ResultOfEncodeMessageBody {
    implicit val ResultOfEncodeMessageBodyDecoder: Decoder[ResultOfEncodeMessageBody] = deriveDecoder[ResultOfEncodeMessageBody]
  }

  object ParamsOfAttachSignatureToMessageBody {
    implicit val ParamsOfAttachSignatureToMessageBodyEncoder: Encoder[ParamsOfAttachSignatureToMessageBody] =
      deriveEncoder[ParamsOfAttachSignatureToMessageBody]
  }

  object ResultOfAttachSignatureToMessageBody {
    implicit val ResultOfAttachSignatureToMessageBodyDecoder: Decoder[ResultOfAttachSignatureToMessageBody] =
      deriveDecoder[ResultOfAttachSignatureToMessageBody]
  }

  object ParamsOfEncodeMessage {
    implicit val ParamsOfEncodeMessageEncoder: Encoder[ParamsOfEncodeMessage] = deriveEncoder[ParamsOfEncodeMessage]
  }

  object ResultOfEncodeMessage {
    implicit val ResultOfEncodeMessageDecoder: Decoder[ResultOfEncodeMessage] = deriveDecoder[ResultOfEncodeMessage]
  }

  object ParamsOfAttachSignature {
    implicit val ParamsOfAttachSignatureEncoder: Encoder[ParamsOfAttachSignature] = deriveEncoder[ParamsOfAttachSignature]
  }

  object ResultOfAttachSignature {
    implicit val ResultOfAttachSignatureDecoder: Decoder[ResultOfAttachSignature] = deriveDecoder[ResultOfAttachSignature]
  }

  object ParamsOfDecodeMessage {

  }

  object DecodedMessageBody {
    implicit val DecodedMessageBodyDecoder: Decoder[DecodedMessageBody] = deriveDecoder[DecodedMessageBody]
  }

  object ParamsOfDecodeMessageBody {
    implicit val ParamsOfDecodeMessageBodyEncoder: Encoder[ParamsOfDecodeMessageBody] = deriveEncoder[ParamsOfDecodeMessageBody]
  }

  object ParamsOfEncodeAccount {
    implicit val ParamsOfEncodeAccountEncoder: Encoder[ParamsOfEncodeAccount] = deriveEncoder[ParamsOfEncodeAccount]
  }

  object ResultOfEncodeAccount {
    implicit val ResultOfEncodeAccountDecoder: Decoder[ResultOfEncodeAccount] = deriveDecoder[ResultOfEncodeAccount]
  }

}
