# Scala TON Client

**Scala TON Client** is a simple scala binding to the [TON SDK](https://github.com/tonlabs/TON-SDK). 

Features:
* All methods of the TON SDK v1.1.0 are implemented
* Interaction with the TON SDK through synchronous an asynchronous calls
* The every method contains inline-doc
* The automatic download of the TON SDK library for the current environment

## Requirements

- Scala 2.13
- cpp compiler
- cmake
- __JAVA_HOME__ environment variable need to be defined
- on Windows environment variable __PATH__ need contains absolute path to folder __TON-SDK/ton_client/build__. 

## Prerequisites

1. Install on your computer latest [nodejs](https://nodejs.org/en/download/)
2. Install on your computer latest [Rust](https://www.rust-lang.org/tools/install)
3. If you use Windows install on your computer one of below enumerated toolchains:
    1. [Mingw-w64](http://mingw-w64.org/doku.php)
    2. [Cygwin](https://cygwin.com/install.html)
    3. [Visual Studio Code](https://code.visualstudio.com/docs/cpp/config-msvc)
    Architecture: x86_amd64
    4. [msys2](https://www.msys2.org/)
    https://stackoverflow.com/questions/30069830/how-to-install-mingw-w64-and-msys2
    6. [Mingw-builds](http://mingw-w64.org/doku.php/download/mingw-builds)
    
    If you use Linux install cmake:
        
```sudo apt install cmake```  
    
## Project structure

Project contains several subprojects:
1. __native__ - cpp project, that you can edit with any cpp IDE you prefer (CLion, Visual Studio Code etc.).
2. __TON-SDK__ - [git submodule](https://git-scm.com/docs/git-submodule) of TON-SDK repo.
3. __ton_client_scala__ - Scala binding itself.

## Installation

1. By hands.
    
    * Update git submodule
     
    Select branch in TON-SDK you want to build and execute in the folder __TON-SDK__ console next commands:
    
    ```git checkout <branch>```
    
    ```git pull```
    
    * Build ton_client binary library
    
    In the folder __TON-SDK/ton_client/client__ run nodejs build script:
    
    ```node build```
    
    * Copy header file tonclient.h from __TON-SDK/ton_client/client__ to folder __native/include__
    
    * Compile project native with toolchain you prefer
    
    * Compile project ton_client_scala with sbt compile
    
2. In semi-auto mode
    
    * Set in build.sbt branch value you want to build
    
    * Select sbt project __TON-SDK__ and run sbt command 
    
    ```buildDependentLib```
    
    * Select sbt project __native__ and run sbt command
    
    ```buildBridge```
    
    * Select sbt project __ton_client_scala__ and run sbt command
    
    ```compile```

## How to use
Simple TonContextScala instantiation:
```scala
import com.radiance.scala.types._
import com.radiance.scala.tonclient._

val config = ClientConfig(
        Some(NetworkConfig(
            "net.ton.dev",                            // server_address: String
            Some(5),                                  // network_retries_count: Option[Int] 
            Some(5),                                  // message_retries_count: Option[Int]
            Some(60000),                              // message_processing_timeout: Option[Long]
            Some(60000),                              // wait_for_timeout: Option[Long]
            Some(30000),                              // out_of_sync_threshold: Option[Long]
            Some("")                                  // access_key: Option[String]
        )),
        Some(CryptoConfig(
            Some(1),                                  // mnemonic_dictionary: Option[Long]
            Some(12),                                 // mnemonic_word_count: Option[Long]
            Some("m/44'/396'/0'/0/0"),                // hdkey_derivation_path: Option[String] 
            Some(true)                                // hdkey_compliant: Option[Boolean]
        )), 
        Some(AbiConfig(
            Some(0)                                    // workchain: Option[Int]
            Some(60000)                                // message_expiration_timeout: Option[Long], 
            Some(1.35)                                 // message_expiration_timeout_grow_factor: Option[Float]
        ))
    )
val ctx = TonContextScala(config)
```

Or you can use simpler configuration: 
```scala
import com.radiance.scala.types._
import com.radiance.scala.tonclient._

val config = ClientConfig(
        Some(NetworkConfig("net.ton.dev", None, None, None, None, None, None)),
        None, 
        None
    )
val ctx = TonContextScala(config)
```

More details you can find [here](https://github.com/tonlabs/TON-SDK/blob/1.1.0/docs/mod_client.md#ClientConfig). 

## Basic usage
In package __com.radiance.scala.modules__ you can find modules that encapsulate definite functionality of Scala TON Client:
    
* AbiModule
* BocModule
* ClientModule
* CryptoModule
* NetModule
* ProcessingModule
* TvmModule
* UtilsModule
    
### Example of usage ClientModule
    
```scala
import com.radiance.scala.modules._
import com.radiance.scala.types.ClientTypes._

val ctx: TonContextScala = ???
val clientModule = new ClientModule(ctx)
// Get TON SDK version
val result1: Either[Throwable, ResultOfVersion] = clientModule.version
// Get TON SDK build info
val result2: Either[Throwable, ResultOfVersion] = clientModule.buildInfo
```

### Example of usage CryptoModule

```scala
import com.radiance.scala.modules._
import com.radiance.scala.types.CryptoTypes._

val ctx: TonContextScala = ???
val cryptoModule = new ClientModule(ctx)

// Generate random key pair
val result: Either[Throwable, KeyPair] = cryptoModule.generateRandomSignKeys
```

### Example of usage UtilsModule

```scala
import com.radiance.scala.modules._
import com.radiance.scala.types.UtilsTypes._

val ctx: TonContextScala = ???
val utilsModule = new UtilsModule(ctx)

// Convert address to hex format
val result: Either[Throwable, ResultOfConvertAddress] = utilsModule.convertAddress("ee65d170830136253ad8bd2116a28fcbd4ac462c6f222f49a1505d2fa7f7f528", Hex)
```

## Example of usage NetModule
You can use circe library for building graphql queries:

```scala
import com.radiance.scala.types.NetTypes.{ASC, OrderBy}
import io.circe._
import io.circe.parser._

val ctx: TonContextScala = ???
val netModule = new NetModule(ctx)

val query = parse("""{"last_paid":{"in":[1601332024,1601331924]}}""").getOrElse(Json.Null)
val res = netModule.waitForCollection(
  "accounts",
  Some(query),
  "id,last_paid",
  Some(60000)
).get
```

Or:

```scala
val query = parse("""{"last_paid":{"in":[1601332024,1601331924,1601332491,1601332679]}}""").getOrElse(Json.Null)
val res = net.queryCollection(
  "accounts",
  Some(query),
  "acc_type,acc_type_name,balance,boc,id,last_paid,workchain_id",
  Some(List(OrderBy("last_paid", ASC))),
  Some(2)
).get
```

Also you can observe collection, returned by query:

```scala
    val callback: (e: circe.Json) => Unit = ???
    val query = parse("""{"balance_delta":{"gt":"0x5f5e100"}}""").getOrElse(Json.Null)
    println(s"Query:\n${query.spaces2}")

    val res = net.subscribeCollection(
      "transactions",
      Some(query),
      "id,block_id,balance_delta",
      callback
    ).get
  ```
## Testing

To run junit tests run

```sbt test```

## FatJar

This application use [assembly-plugin](https://github.com/sbt/sbt-assembly).
To build fat jar use

```sbt assembly```

## License

The Apache License Version 2.0. Please see [License File](LICENSE) for more information.
    
