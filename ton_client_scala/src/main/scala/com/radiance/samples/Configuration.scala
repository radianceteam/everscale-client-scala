package com.radiance.samples

object Configuration {

  object Sample1 {
    import com.radiance.jvm.Context
    import com.radiance.jvm.client._
    import cats.implicits._
    import scala.concurrent.ExecutionContext

    val networkConfig: NetworkConfig = NetworkConfig(
      "net.ton.dev".some, // server_address:             Option[String]
      None, // endpoints:                  Option[List[String]]
      5.some, // network_retries_count:      Option[Int]
      30000L.some, // max_reconnect_timeout:      Option[Long]
      30000L.some, // reconnect_timeout:          Option[Long]
      5.some, // message_retries_count:      Option[Int]
      60000L.some, // message_processing_timeout: Option[Long]
      60000L.some, // wait_for_timeout:           Option[Long]
      30000L.some, // out_of_sync_threshold:      Option[Long]
      1L.some, // sending_endpoint_count:    Option[Long]
      "".some // access_key:                 Option[String]
    )

    val cryptoConfig: CryptoConfig = CryptoConfig(
      1L.some, // mnemonic_dictionary:        Option[Long]
      12L.some, // mnemonic_word_count:        Option[Long]
      "m/44'/396'/0'/0/0".some, // hdkey_derivation_path:      Option[String]
      true.some // hdkey_compliant:            Option[Boolean]
    )

    val abiConfig: AbiConfig = AbiConfig(
      0.some, // workchain:                              Option[Int]
      60000L.some, // message_expiration_timeout:             Option[Long]
      1.35f.some // message_expiration_timeout_grow_factor: Option[Float]
    )

    val clientConfig: ClientConfig = ClientConfig(
      networkConfig.some,
      cryptoConfig.some,
      abiConfig.some
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
      NetworkConfig("net.ton.dev".some).some
    )
    implicit val ec: ExecutionContext = ExecutionContext.global
    val ctx: Context = Context(clientConfig)
  }

}
