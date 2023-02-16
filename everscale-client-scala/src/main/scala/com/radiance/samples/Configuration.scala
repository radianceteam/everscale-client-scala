package com.radiance.samples

import com.radiance.jvm.crypto.MnemonicDictionaryEnum

object Configuration {

  object Sample1 {
    import com.radiance.jvm.Context
    import com.radiance.jvm.client._
    import cats.implicits._
    import scala.concurrent.ExecutionContext

    val networkConfig: NetworkConfig = NetworkConfig(
      server_address = "net.ton.dev".some: Option[String],
      endpoints = None: Option[List[String]],
      network_retries_count = 5.some: Option[Int],
      max_reconnect_timeout = 30000L.some: Option[Long],
      reconnect_timeout = 30000L.some: Option[Long],
      message_retries_count = 5.some: Option[Int],
      message_processing_timeout = 60000L.some: Option[Long],
      wait_for_timeout = 60000L.some: Option[Long],
      out_of_sync_threshold = 30000L.some: Option[Long],
      sending_endpoint_count = 1L.some: Option[Long],
      latency_detection_interval = 5000L.some: Option[Long],
      max_latency = 5000L.some: Option[Long],
      query_timeout = 3000L.some: Option[Long],
      queries_protocol =
        NetworkQueriesProtocolEnum.HTTP.some: Option[NetworkQueriesProtocolEnum.NetworkQueriesProtocol],
      first_remp_status_timeout = 60000L.some: Option[Long],
      next_remp_status_timeout = 60000L.some: Option[Long],
      signature_id = None,
      access_key = "access_key".some: Option[String]
    )

    val cryptoConfig: CryptoConfig = CryptoConfig(
      mnemonic_dictionary = MnemonicDictionaryEnum.Ton.some,
      mnemonic_word_count = 12L.some,
      hdkey_derivation_path = "m/44'/396'/0'/0/0".some
    )

    val abiConfig: AbiConfig = AbiConfig(
      0.some, // workchain:                              Option[Int]
      60000L.some, // message_expiration_timeout:             Option[Long]
      1.35f.some // message_expiration_timeout_grow_factor: Option[Float]
    )

    val clientConfig: ClientConfig = ClientConfig(
      binding = None,
      network = networkConfig.some,
      crypto = cryptoConfig.some,
      abi = abiConfig.some,
      boc = None,
      proofs = None,
      local_storage_path = None
    )
    implicit val ec: ExecutionContext = ExecutionContext.global
    val ctx: Context = Context(clientConfig)
  }

  object Sample2 {
    import com.radiance.jvm.Context
    import com.radiance.jvm.client._
    import cats.implicits._
    import scala.concurrent.ExecutionContext

    val clientConfig: ClientConfig = ClientConfig(
      None,
      NetworkConfig("net.ton.dev".some).some
    )
    implicit val ec: ExecutionContext = ExecutionContext.global
    val ctx: Context = Context(clientConfig)
  }

}
