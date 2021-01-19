package com.radiance.jvm

import com.radiance.jvm.crypto.{CryptoModule, KeyPair, ParamsOfAppSigningBox, ResultOfAppSigningBox}
import org.scalatest.flatspec.AnyFlatSpec
import cats.implicits._
import Utils._
import com.radiance.jvm.app.AppObject

class CryptoModuleTest extends AnyFlatSpec with TestBase {

  override def init(): Unit = {
    super.init()
    cryptoModule = new CryptoModule(ctx)
  }

  behavior.of("CryptoModule")

  it should "return correct result of factorization" in {
    val composite: Int = 0xda
    val factorizeResult: List[String] =
      cryptoModule.factorize(Integer.toHexString(composite)).get.factors
    println(s"Result of factorization: { arg: ${Integer
      .toHexString(composite)}; res = ${factorizeResult.mkString(", ")}}")
    assert(
      composite == factorizeResult
        .map(s => Integer.parseInt(s, 16))
        .foldLeft(1) { case (e, acc) => e * acc }
    )
  }

  it should "produce Exception when is invoked" in {
    assertThrows[Throwable] {
      cryptoModule.factorize("c7").get
    }
  }

  it should "perform correct calculation of modularPower" in {
    val params = Vector(4, 2, 6)
    val args = params.map(Integer.toHexString)
    val result =
      cryptoModule.modularPower(args(0), args(1), args(2)).get.modular_power
    println(
      s"Result of calculation modularPower: {args: ${params.mkString(", ")}, result: $result}"
    )
    assert(
      java.lang.Long
        .toHexString(Math.pow(params(0), params(1)).round % params(2)) == result
    )
  }

  it should "perform correct calculation of crc16" in {
    val crc16 = cryptoModule.tonCrc16("abcdABCD0123").get
    println(s"Crc16 result: ${crc16.crc}")
    assert(crc16.crc == 19048)
  }

  it should "perform correct calculation of hash" in {
    val (sha512, sha256) = (for {
      sha512 <-
        cryptoModule.sha512(encode("Message to hash with sha 512".getBytes))
      sha256 <-
        cryptoModule.sha256(encode("Message to hash with sha 256".getBytes))
    } yield (sha512, sha256)).get
    assert(
      sha512.hash == "2616a44e0da827f0244e93c2b0b914223737a6129bc938b8edf2780ac9482960baa9b7c7cdb11457c1cebd5ae77e295ed94577f32d4c963dc35482991442daa5"
      && sha256.hash == "16fd057308dd358d5a9b3ba2de766b2dfd5e308478fc1f7ba5988db2493852f5"
    )
  }

  it should "produce Exception because of verifying wrong data" in {
    assertThrows[Throwable] {
      val keys: KeyPair = cryptoModule.generateRandomSignKeys.get
      val randomBytes: String = cryptoModule.generateRandomBytes(15).get.bytes
      cryptoModule.verifySignature(randomBytes, keys.public).get
    }
  }

  it should "produce Exception because of extraction from random data" in {
    assertThrows[Throwable] {
      val randomBytes = cryptoModule.generateRandomBytes(15).get.bytes
      cryptoModule.hdkeyPublicFromXprv(randomBytes).get
    }
  }

  it should "produce Exception because of extraction from random data 1" in {
    assertThrows[Throwable] {
      val randomBytes = cryptoModule.generateRandomBytes(15).get.bytes
      cryptoModule.hdkeySecretFromXprv(randomBytes).get
    }
  }

  it should "show the correct behavior of random bytes" in {
    val keys: KeyPair = cryptoModule.generateRandomSignKeys.get
    println(
      s"Keys were generated { public: '${keys.public}', secret: '${keys.secret}''"
    )
    val safePublic = cryptoModule
      .convertPublicKeyToTonSafeFormat(keys.public)
      .get
      .ton_public_key
    println(s"Safe public key: '$safePublic'")
    val randomBytes = cryptoModule.generateRandomBytes(15).get.bytes
    println(s"Random bytes: '$randomBytes'")
    val sign = cryptoModule.sign(randomBytes, keys).get
    println(s"Sign: '$sign'")
    val verified: String =
      cryptoModule.verifySignature(sign.signed, keys.public).get.unsigned
    println(s"Verified: '$verified'")
    assert(randomBytes.length == 20)
  }

  it should "show the correct behavior of mnemonic words" in {
    val words = cryptoModule.mnemonicWords(1L.some).get.words
    println(s"Word: '$words'")

    val xPrivate: String = cryptoModule
      .hdkeyXprvFromMnemonic(
        "abuse boss fly battle rubber wasp afraid hamster guide essence vibrant tattoo",
        None,
        None
      )
      .get
      .xprv
    println(s"Extended private key: '$xPrivate'")

    val publicKey: String =
      cryptoModule.hdkeyPublicFromXprv(xPrivate).get.public
    println(s"Public key: '$publicKey'")

    val secretKey: String =
      cryptoModule.hdkeySecretFromXprv(xPrivate).get.secret
    println(s"Secret key: '$secretKey'")

    assert(
      xPrivate == "xprv9s21ZrQH143K25JhKqEwvJW7QAiVvkmi4WRenBZanA6kxHKtKAQQKwZG65kCyW5jWJ8NY9e3GkRoistUjjcpHNsGBUv94istDPXvqGNuWpC"
    )
  }

  it should "perform correct execution of scrypt" in {
    assert(
      cryptoModule
        .scrypt(
          encode("Test Password".getBytes),
          encode("Test Salt".getBytes),
          10,
          8,
          16,
          64
        )
        .get
        .key == "52e7fcf91356eca55fc5d52f16f5d777e3521f54e3c570c9bbb7df58fc15add73994e5db42be368de7ebed93c9d4f21f9be7cc453358d734b04a057d0ed3626d"
    )
  }

  it should "perform correct execution of nacl" in {
    assert(
      cryptoModule
        .naclBoxKeypairFromSecretKey(
          "8fb4f2d256e57138fb310b0a6dac5bbc4bee09eb4821223a720e5b8e1f3dd674"
        )
        .get
        .public ==
        "2c289ff3306793bad337d15d6b9b7d9f0a82a298de3eedadb5bbaa3259b9b77b" &&
        cryptoModule
          .naclSign(
            encode("Test Message".getBytes),
            "56b6a77093d6fdf14e593f36275d872d75de5b341942376b2a08759f3cbae78f1869b7ef29d58026217e9cf163cbfbd0de889bdf1bf4daebf5433a312f5b8d6e"
          )
          .get
          .signed ==
        "+wz+QO6l1slgZS5s65BNqKcu4vz24FCJz4NSAxef9lu0jFfs8x3PzSZRC+pn5k8+aJi3xYMA3BQzglQmjK3hA1Rlc3QgTWVzc2FnZQ==" &&
        decode(
          cryptoModule
            .naclSignOpen(
              "+wz+QO6l1slgZS5s65BNqKcu4vz24FCJz4NSAxef9lu0jFfs8x3PzSZRC+pn5k8+aJi3xYMA3BQzglQmjK3hA1Rlc3QgTWVzc2FnZQ==",
              "1869b7ef29d58026217e9cf163cbfbd0de889bdf1bf4daebf5433a312f5b8d6e"
            )
            .get
            .unsigned
        ) ==
        "Test Message"
    )
  }

  it should "perform correct execution of naclBox" in {
    val keys: KeyPair = cryptoModule.naclBoxKeypair.get
    assert(
      keys.public.length == 64 &&
      keys.secret.length == 64 &&
      keys.public != keys.secret &&
      cryptoModule
        .naclBoxKeypairFromSecretKey(
          "e207b5966fb2c5be1b71ed94ea813202706ab84253bdf4dc55232f82a1caf0d4"
        )
        .get
        .public ==
        "a53b003d3ffc1e159355cb37332d67fc235a7feb6381e36c803274074dc3933a" &&
        cryptoModule
          .naclBox(
            encode("Test Message".getBytes),
            "cd7f99924bf422544046e83595dd5803f17536f5c9a11746",
            "c4e2d9fe6a6baf8d1812b799856ef2a306291be7a7024837ad33a8530db79c6b",
            "d9b9dc5033fb416134e5d2107fdbacab5aadb297cb82dbdcd137d663bac59f7f"
          )
          .get
          .encrypted == "li4XED4kx/pjQ2qdP0eR2d/K30uN94voNADxwA==" &&
        decode(
          cryptoModule
            .naclBoxOpen(
              "li4XED4kx/pjQ2qdP0eR2d/K30uN94voNADxwA==",
              "cd7f99924bf422544046e83595dd5803f17536f5c9a11746",
              "c4e2d9fe6a6baf8d1812b799856ef2a306291be7a7024837ad33a8530db79c6b",
              "d9b9dc5033fb416134e5d2107fdbacab5aadb297cb82dbdcd137d663bac59f7f"
            )
            .get
            .decrypted
        ) == "Test Message"
    )
  }

  it should "correctly work with app object" in {
    val publicKey: String = "02a8eb63085f73c33fa31b4d1134259406347284f8dab6fc68f4bf8c96f6c39b75"

    val app_object: AppObject[ParamsOfAppSigningBox, ResultOfAppSigningBox] =
      new AppObject[ParamsOfAppSigningBox, ResultOfAppSigningBox]({
        case ParamsOfAppSigningBox.GetPublicKey   => ResultOfAppSigningBox.GetPublicKey(publicKey)
        case ParamsOfAppSigningBox.Sign(unsigned) => ResultOfAppSigningBox.Sign(unsigned)
      })
    val registeredBox = cryptoModule.registerSigningBox(app_object).get

    val publicKeyRes = cryptoModule.signingBoxGetPublicKey(registeredBox.handle).get

    println(publicKeyRes.pubkey)

    cryptoModule.removeSigningBox(registeredBox.handle).get
    println("Remove registered signing box succesfully")

    assert(publicKeyRes.pubkey == publicKey)
  }
}
