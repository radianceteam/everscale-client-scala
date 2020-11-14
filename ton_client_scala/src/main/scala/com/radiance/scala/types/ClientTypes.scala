package com.radiance.scala.types

import io.circe.{Codec, Decoder, Encoder, Json, JsonObject}
import io.circe.derivation.{deriveCodec, deriveDecoder, deriveEncoder}

object ClientTypes {
  type Value = JsonObject
  type API = String

  case class ClientError(code: Long, message: String, data: Value)

  case class ClientConfig(network: Option[NetworkConfig], crypto: Option[CryptoConfig], abi: Option[AbiConfig])

  case class NetworkConfig(server_address: String, network_retries_count: Option[Int], message_retries_count: Option[Int], message_processing_timeout: Option[Long], wait_for_timeout: Option[Long], out_of_sync_threshold: Option[Long], access_key: Option[String])

  case class CryptoConfig(mnemonic_dictionary: Option[Long], mnemonic_word_count: Option[Long], hdkey_derivation_path: Option[String], hdkey_compliant: Option[Boolean])

  case class AbiConfig(workchain: Option[Int], message_expiration_timeout: Option[Long], message_expiration_timeout_grow_factor: Option[Float])

  case class BuildInfoDependency(name: String, git_commit: String)

  case class ResultOfGetApiReference(api: API)

  case class ResultOfVersion(version: String)

  case class ResultOfBuildInfo(build_number: Long, dependencies: List[BuildInfoDependency])


  object ClientError {
    implicit val ClientErrorCodec: Codec[ClientError] = deriveCodec[ClientError]
  }

  object ClientConfig {
    implicit val ClientConfigEncoder: Encoder[ClientConfig] = deriveEncoder[ClientConfig]
  }

  object NetworkConfig {
    implicit val NetworkConfigEncoder: Encoder[NetworkConfig] = deriveEncoder[NetworkConfig]
  }

  object CryptoConfig {
    implicit val CryptoConfigEncoder: Encoder[CryptoConfig] = deriveEncoder[CryptoConfig]
  }

  object AbiConfig {
    implicit val CryptoConfigEncoder: Encoder[AbiConfig] = deriveEncoder[AbiConfig]
  }

  object BuildInfoDependency {
    implicit val BuildInfoDependencyDecoder: Decoder[BuildInfoDependency] = deriveDecoder[BuildInfoDependency]
  }

  object ResultOfGetApiReference {
    implicit val ResultOfGetApiReferenceDecoder: Decoder[ResultOfGetApiReference] = deriveDecoder[ResultOfGetApiReference]
  }

  object ResultOfVersion {
    implicit val ResultOfVersionDecoder: Decoder[ResultOfVersion] = deriveDecoder[ResultOfVersion]
  }

  object ResultOfBuildInfo {
    implicit val ResultOfBuildInfoDecoder: Decoder[ResultOfBuildInfo] = deriveDecoder[ResultOfBuildInfo]
  }

}
