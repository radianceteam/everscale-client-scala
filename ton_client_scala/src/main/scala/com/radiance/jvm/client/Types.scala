package com.radiance.jvm.client

import com.radiance.jvm.Utils._
import com.radiance.jvm._
import io.circe.derivation._
import io.circe._

case class AbiConfig(
    workchain: Option[Int],
    message_expiration_timeout: Option[Long],
    message_expiration_timeout_grow_factor: Option[Float]
)

object AbiConfig {
  implicit val encoder: Encoder[AbiConfig] = deriveEncoder[AbiConfig]
}

sealed trait AppRequestResult

object AppRequestResult {
  import io.circe.syntax._
  case class Error(text: String) extends AppRequestResult
  case class Ok(result: Value) extends AppRequestResult

  implicit val encoder: Encoder[AppRequestResult] = {
    case a: Error => a.asJson.deepMerge(generateType(a))
    case a: Ok => a.asJson.deepMerge(generateType(a))
  }

  object Error {
    implicit val encoder: Encoder[Error] = deriveEncoder[Error]
  }

  object Ok {
    implicit val encoder: Encoder[Ok] = deriveEncoder[Ok]
  }
}

case class BuildInfoDependency(name: String, git_commit: String)

object BuildInfoDependency {
  implicit val decoder: Decoder[BuildInfoDependency] =
    deriveDecoder[BuildInfoDependency]
}

case class ClientConfig(
    network: Option[NetworkConfig],
    crypto: Option[CryptoConfig],
    abi: Option[AbiConfig]
)

object ClientConfig {
  implicit val encoder: Encoder[ClientConfig] = deriveEncoder[ClientConfig]
}

case class ClientError(code: Long, message: String, data: Value)

object ClientError {
  implicit val codec: Codec[ClientError] = deriveCodec[ClientError]
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
    server_address: String,
    network_retries_count: Option[Int],
    message_retries_count: Option[Int],
    message_processing_timeout: Option[Long],
    wait_for_timeout: Option[Long],
    out_of_sync_threshold: Option[Long],
    access_key: Option[String]
)

object NetworkConfig {
  implicit val encoder: Encoder[NetworkConfig] = deriveEncoder[NetworkConfig]
}

// TODO Not used
case class ParamsOfAppRequest(app_request_id: Long, request_data: Value)

case class ParamsOfResolveAppRequest(
    app_request_id: Long,
    result: AppRequestResult
) extends Bind {
  override type Out = Unit
  override val decoder: Decoder[Unit] = implicitly[Decoder[Unit]]
}

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
