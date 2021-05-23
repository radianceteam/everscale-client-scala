package com.radiance.jvm.client

import com.radiance.jvm._
import io.circe._
import io.circe.derivation._
import io.circe.generic.extras

case class AbiConfig(
  workchain: Option[Int],
  message_expiration_timeout: Option[Long],
  message_expiration_timeout_grow_factor: Option[Float]
)

object AbiConfig {
  implicit val encoder: Encoder[AbiConfig] = deriveEncoder[AbiConfig]
}

object AppRequestResultADT {

  sealed trait AppRequestResult

  case class Error(text: String) extends AppRequestResult

  case class Ok(result: Value) extends AppRequestResult

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[AppRequestResult] =
    extras.semiauto.deriveConfiguredEncoder[AppRequestResult]
}

case class BocConfig(cache_max_size: Option[Long])

object BocConfig {
  implicit val encoder: Encoder[BocConfig] = deriveEncoder[BocConfig]
}

case class BuildInfoDependency(name: String, git_commit: String)

object BuildInfoDependency {
  implicit val decoder: Decoder[BuildInfoDependency] =
    deriveDecoder[BuildInfoDependency]
}

case class ClientConfig(
  network: Option[NetworkConfig],
  crypto: Option[CryptoConfig] = None,
  abi: Option[AbiConfig] = None,
  boc: Option[BocConfig] = None
)

object ClientConfig {
  implicit val encoder: Encoder[ClientConfig] = deriveEncoder[ClientConfig]
}

case class ClientError(code: Long, message: String, data: Value)

object ClientError {
  implicit val codec: Codec[ClientError] = deriveCodec[ClientError]
}

object ClientErrorCodeEnum {

  sealed trait ClientErrorCode {
    val code: String
  }

  case object NotImplemented extends ClientErrorCode {
    val code: String = "1"
  }

  case object InvalidHex extends ClientErrorCode {
    val code: String = "2"
  }

  case object InvalidBase64 extends ClientErrorCode {
    val code: String = "3"
  }

  case object InvalidAddress extends ClientErrorCode {
    val code: String = "4"
  }

  case object CallbackParamsCantBeConvertedToJson extends ClientErrorCode {
    val code: String = "5"
  }

  case object WebsocketConnectError extends ClientErrorCode {
    val code: String = "6"
  }

  case object WebsocketReceiveError extends ClientErrorCode {
    val code: String = "7"
  }

  case object WebsocketSendError extends ClientErrorCode {
    val code: String = "8"
  }

  case object HttpClientCreateError extends ClientErrorCode {
    val code: String = "9"
  }

  case object HttpRequestCreateError extends ClientErrorCode {
    val code: String = "10"
  }

  case object HttpRequestSendError extends ClientErrorCode {
    val code: String = "11"
  }

  case object HttpRequestParseError extends ClientErrorCode {
    val code: String = "12"
  }

  case object CallbackNotRegistered extends ClientErrorCode {
    val code: String = "13"
  }

  case object NetModuleNotInit extends ClientErrorCode {
    val code: String = "14"
  }

  case object InvalidConfig extends ClientErrorCode {
    val code: String = "15"
  }

  case object CannotCreateRuntime extends ClientErrorCode {
    val code: String = "16"
  }

  case object InvalidContextHandle extends ClientErrorCode {
    val code: String = "17"
  }

  case object CannotSerializeResult extends ClientErrorCode {
    val code: String = "18"
  }

  case object CannotSerializeError extends ClientErrorCode {
    val code: String = "19"
  }

  case object CannotConvertJsValueToJson extends ClientErrorCode {
    val code: String = "20"
  }

  case object CannotReceiveSpawnedResult extends ClientErrorCode {
    val code: String = "21"
  }

  case object SetTimerError extends ClientErrorCode {
    val code: String = "22"
  }

  case object InvalidParams extends ClientErrorCode {
    val code: String = "23"
  }

  case object ContractsAddressConversionFailed extends ClientErrorCode {
    val code: String = "24"
  }

  case object UnknownFunction extends ClientErrorCode {
    val code: String = "25"
  }

  case object AppRequestError extends ClientErrorCode {
    val code: String = "26"
  }

  case object NoSuchRequest extends ClientErrorCode {
    val code: String = "27"
  }

  case object CanNotSendRequestResult extends ClientErrorCode {
    val code: String = "28"
  }

  case object CanNotReceiveRequestResult extends ClientErrorCode {
    val code: String = "29"
  }

  case object CanNotParseRequestResult extends ClientErrorCode {
    val code: String = "30"
  }

  case object UnexpectedCallbackResponse extends ClientErrorCode {
    val code: String = "31"
  }

  case object CanNotParseNumber extends ClientErrorCode {
    val code: String = "32"
  }

  case object InternalError extends ClientErrorCode {
    val code: String = "33"
  }

}

case class CryptoConfig(
  mnemonic_dictionary: Option[Long],
  mnemonic_word_count: Option[Long],
  hdkey_derivation_path: Option[String],
  hdkey_compliant: Option[Boolean]
)

object CryptoConfig {
  implicit val encoder: Encoder[CryptoConfig] = deriveEncoder[CryptoConfig]
}

case class NetworkConfig(
                          server_address: Option[String],
                          endpoints: Option[List[String]] = None,
                          network_retries_count: Option[Int] = None,
                          max_reconnect_timeout: Option[Long] = None,
                          reconnect_timeout: Option[Long] = None,
                          message_retries_count: Option[Int] = None,
                          message_processing_timeout: Option[Long] = None,
                          wait_for_timeout: Option[Long] = None,
                          out_of_sync_threshold: Option[Long] = None,
                          sending_endpoint_count: Option[Long] = None,
                          latency_detection_interval: Option[Long] = None,
                          max_latency: Option[Long] = None,
                          access_key: Option[String] = None
                        )


object NetworkConfig {
  implicit val encoder: Encoder[NetworkConfig] = deriveEncoder[NetworkConfig]
}

case class ParamsOfAppRequest(app_request_id: Long, request_data: Value)

object ParamsOfAppRequest {
  implicit val decoder: Decoder[ParamsOfAppRequest] = deriveDecoder[ParamsOfAppRequest]
}

case class ParamsOfResolveAppRequest(
  app_request_id: Long,
  result: AppRequestResultADT.AppRequestResult
)

object ParamsOfResolveAppRequest {
  implicit val encoder: Encoder[ParamsOfResolveAppRequest] =
    deriveEncoder[ParamsOfResolveAppRequest]
}

case class ResultOfBuildInfo(
  build_number: Long,
  dependencies: List[BuildInfoDependency]
)

object ResultOfBuildInfo {
  implicit val decoder: Decoder[ResultOfBuildInfo] =
    deriveDecoder[ResultOfBuildInfo]
}

case class ResultOfGetApiReference(api: API)

object ResultOfGetApiReference {
  implicit val decoder: Decoder[ResultOfGetApiReference] =
    deriveDecoder[ResultOfGetApiReference]
}

case class ResultOfVersion(version: String)
object ResultOfVersion {
  implicit val decoder: Decoder[ResultOfVersion] =
    deriveDecoder[ResultOfVersion]
}
