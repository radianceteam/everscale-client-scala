package com.radiance.scala.tonclient.crypto

import com.radiance.scala.tonclient.TONContext
import com.radiance.scala.tonclient.crypto.args._
import com.radiance.scala.tonclient.types.both.KeyPair
import com.radiance.scala.tonclient.types.out.ResultOfSign

import scala.concurrent.{ExecutionContext, Future}

/**
 * Crypto functions.
 */
class Crypto(val ctx: TONContext)(implicit val ec: ExecutionContext) {

  /**
   * Converts public key to ton safe_format
   *
   * @param publicKey Public key - 64 symbols hex string
   */
  def convertPublicKeyToTonSafeFormat(publicKey: String): Future[Either[Throwable, String]] = ctx
    .requestField[ConvertPublicKeyToTonSafeFormatArgs, String](ConvertPublicKeyToTonSafeFormatArgs(publicKey))

  /**
   * Performs prime factorization – decomposition of a composite number into a product of smaller prime integers (factors).
   * See <a target="_blank" href="https://en.wikipedia.org/wiki/Integer_factorization">https://en.wikipedia.org/wiki/Integer_factorization</a>
   *
   * @param composite Hexadecimal representation of u64 composite number.
   */
  def factorize(composite: String): Future[Either[Throwable, List[String]]] = ctx
    .requestField[FactorizeArgs, List[String]](FactorizeArgs(composite))

  /**
   * Generates random byte array of the specified length and returns it in `base64` format
   *
   * @param length Size of random byte array.
   */
  def generateRandomBytes(length: Long): Future[Either[Throwable, String]] = ctx
    .requestField[GenerateRandomBytesArgs, String](GenerateRandomBytesArgs(length))

  /**
   * Generates random ed25519 key pair.
   *
   */
  def generateRandomSignKeys: Future[Either[Throwable, KeyPair]] = ctx
    .requestValue[GenerateRandomSignKeysArgs, KeyPair](GenerateRandomSignKeysArgs())

  /**
   * Returns extended private key derived from the specified extended private key and child index
   *
   * @param xprv       Serialized extended private key
   * @param childIndex Child index (see BIP-0032)
   * @param hardened   Indicates the derivation of hardened/not-hardened key (see BIP-0032)
   */
  def hdkeyDeriveFromXprv(xprv: String, childIndex: Long, hardened: Boolean): Future[Either[Throwable, String]] = ctx
    .requestField[HdkeyDeriveFromXprvArgs, String](HdkeyDeriveFromXprvArgs(xprv, childIndex, hardened))

  /**
   * Derives the exented private key from the specified key and path
   *
   * @param xprv Serialized extended private key
   * @param path Derivation path, for instance "m/44'/396'/0'/0/0"
   */
  def hdkeyDeriveFromXprvPath(xprv: String, path: String): Future[Either[Throwable, String]] = ctx
    .requestField[HdkeyDeriveFromXprvPathArgs, String](HdkeyDeriveFromXprvPathArgs(xprv, path))

  /**
   * Extracts the public key from the serialized extended private key
   *
   * @param xprv Serialized extended private key
   */
  def hdkeyPublicFromXprv(xprv: String): Future[Either[Throwable, String]] = ctx
    .requestField[HdkeyPublicFromXprvArgs, String](HdkeyPublicFromXprvArgs(xprv))


  /**
   * Extracts the private key from the serialized extended private key
   *
   * @param xprv Serialized extended private key
   */
  def hdkeySecretFromXprv(xprv: String): Future[Either[Throwable, String]] = ctx
    .requestField[HdkeySecretFromXprvArgs, String](HdkeySecretFromXprvArgs(xprv))

  /**
   * Generates an extended master private key that will be the root for all the derived keys
   *
   * @param phrase     String with seed phrase
   * @param dictionary Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  def hdkeyXprvFromMnemonic(phrase: String, dictionary: Long, wordCount: Long): Future[Either[Throwable, String]] = ctx
    .requestField[HdkeyXprvFromMnemonicArgs, String](HdkeyXprvFromMnemonicArgs(phrase, dictionary, wordCount))

  /**
   * Validates the seed phrase, generates master key and then derives the key pair from the master key and the specified path
   *
   * @param phrase     Phrase
   * @param path       Derivation path, for instance "m/44'/396'/0'/0/0"
   * @param dictionary Dictionary identifier
   * @param wordCount  Word count
   */
  def mnemonicDeriveSignKeys(phrase: String, path: String, dictionary: Long, wordCount: Long): Future[Either[Throwable, KeyPair]] = ctx
    .requestValue[MnemonicDeriveSignKeysArgs, KeyPair](MnemonicDeriveSignKeysArgs(phrase, path, dictionary, wordCount))

  /**
   * Generates mnemonic from pre-generated entropy
   *
   * @param entropy    Entropy bytes. Hex encoded.
   * @param dictionary Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  def mnemonicFromEntropy(entropy: String, dictionary: Long, wordCount: Long): Future[Either[Throwable, String]] = ctx
    .requestField[MnemonicFromEntropyArgs, String](MnemonicFromEntropyArgs(entropy, dictionary, wordCount))

  /**
   * Generates a random mnemonic from the specified dictionary and word count
   *
   * @param dictionary Dictionary identifier
   * @param wordCount  Mnemonic word count
   */
  def mnemonicFromRandom(dictionary: Long, wordCount: Long): Future[Either[Throwable, String]] = ctx
    .requestField[MnemonicFromRandom, String](MnemonicFromRandom(dictionary, wordCount))

  /**
   * The phrase supplied will be checked for word length and validated according to the checksum specified in BIP0039.
   *
   * @param phrase     Phrase
   * @param dictionary Dictionary identifier
   * @param wordCount  Word count
   */
  def mnemonicVerify(phrase: String, dictionary: Long, wordCount: Long): Future[Either[Throwable, Boolean]] = ctx
    .requestField[MnemonicVerifyArgs, Boolean](MnemonicVerifyArgs(phrase, dictionary, wordCount))

  /**
   * Prints the list of words from the specified dictionary
   *
   * @param dictionary Dictionary identifier
   */
  def mnemonicWords(dictionary: Long): Future[Either[Throwable, String]] = ctx
    .requestField[MnemonicWordsArgs, String](MnemonicWordsArgs(dictionary))

  /**
   * Performs modular exponentiation for big integers (`base` ^^ `exponent` mod `modulus`). See <a target="_blank" href="https://en.wikipedia.org/wiki/Modular_exponentiation">https://en.wikipedia.org/wiki/Modular_exponentiation</a>
   *
   * @param base     `base` argument of calculation.
   * @param exponent `exponent` argument of calculation.
   * @param modulus  `modulus` argument of calculation.
   */
  def modularPower(base: String, exponent: String, modulus: String): Future[Either[Throwable, String]] = ctx
    .requestField[ModularPowerArgs, String](ModularPowerArgs(base, exponent, modulus))

  /**
   * Public key authenticated encryption<p> Encrypt and authenticate a message using the senders secret key, the recievers public key, and a nonce.
   *
   * @param decrypted   Data that must be encrypted encoded in `base64`.
   * @param nonce       Nonce, encoded in `hex`
   * @param theirPublic Receiver's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secret      Sender's private key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclBox(decrypted: String, nonce: String, theirPublic: String, secret: String): Future[Either[Throwable, String]] = ctx
    .requestField[NaclBoxArgs, String](NaclBoxArgs(decrypted, nonce, theirPublic, secret))

  /** TODO fill this
   *
   *
   */
  def naclBoxKeypair: Future[Either[Throwable, KeyPair]] = ctx
    .requestValue[NaclBoxKeypairArgs, KeyPair](NaclBoxKeypairArgs())

  /**
   * Generates key pair from a secret key
   *
   * @param secret Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclBoxKeypairFromSecretKey(secret: String): Future[Either[Throwable, KeyPair]] = ctx
    .requestValue[NaclBoxKeypairFromSecretKeyArgs, KeyPair](NaclBoxKeypairFromSecretKeyArgs(secret))

  /**
   * Decrypt and verify the cipher text using the recievers secret key, the senders public key, and the nonce.
   *
   * @param encrypted   Data that must be decrypted. Encoded with `base64`.
   * @param theirPublic Sender's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secret      Receiver's private key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclBoxOpen(encrypted: String, nonce: String, theirPublic: String, secret: String): Future[Either[Throwable, String]] = ctx
    .requestField[NaclBoxOpenArgs, String](NaclBoxOpenArgs(encrypted, nonce, theirPublic, secret))

  /**
   * Encrypt and authenticate message using nonce and secret key.
   *
   * @param decrypted Data that must be encrypted. Encoded with `base64`.
   * @param nonce     Nonce in `hex`
   * @param key       Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSecretBox(decrypted: String, nonce: String, key: String): Future[Either[Throwable, String]] = ctx
    .requestField[NaclSecretBoxArgs, String](NaclSecretBoxArgs(decrypted, nonce, key))

  /**
   * Decrypts and verifies cipher text using `nonce` and secret `key`.
   *
   * @param encrypted Data that must be decrypted. Encoded with `base64`.
   * @param nonce     Nonce in `hex`
   * @param key       Public key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSecretBoxOpen(encrypted: String, nonce: String, key: String): Future[Either[Throwable, String]] = ctx
    .requestField[NaclSecretBoxOpenArgs, String](NaclSecretBoxOpenArgs(encrypted, nonce, key))

  /**
   * Signs data using the signer's secret key.
   *
   * @param unsigned Data that must be signed encoded in `base64`.
   * @param secret   Signer's secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSign(unsigned: String, secret: String): Future[Either[Throwable, String]] = ctx
    .requestField[NaclSignArgs, String](NaclSignArgs(unsigned, secret))

  /**
   *
   *
   * @param unsigned Data that must be signed encoded in `base64`.
   * @param secret   Signer's secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSignDetached(unsigned: String, secret: String): Future[Either[Throwable, String]] = ctx
    .requestField[NaclSignDetachedArgs, String](NaclSignDetachedArgs(unsigned, secret))

  /**
   * Generates a key pair for signing from the secret key
   *
   * @param secret Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSignKeypairFromSecretKey(secret: String): Future[Either[Throwable, KeyPair]] = ctx
    .requestValue[NaclSignKeypairFromSecretKeyArgs, KeyPair](NaclSignKeypairFromSecretKeyArgs(secret))


  /** TODO fill it
   *
   *
   * @param signed Signed data that must be unsigned. Encoded with `base64`.
   * @param public Signer's public key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSignOpen(signed: String, public: String): Future[Either[Throwable, String]] = ctx
    .requestField[NaclSignOpenArgs, String](NaclSignOpenArgs(signed, public))

  /**
   * Derives key from `password` and `key` using `scrypt` algorithm. See <a target="_blank" href="https://en.wikipedia.org/wiki/Scrypt">https://en.wikipedia.org/wiki/Scrypt</a>.<p> # Arguments - `log_n` - The log2 of the Scrypt parameter `N` - `r` - The Scrypt parameter `r` - `p` - The Scrypt parameter `p` # Conditions - `log_n` must be less than `64` - `r` must be greater than `0` and less than or equal to `4294967295` - `p` must be greater than `0` and less than `4294967295` # Recommended values sufficient for most use-cases - `log_n = 15` (`n = 32768`) - `r = 8` - `p = 1`
   *
   * @param password The password bytes to be hashed. Must be encoded with `base64`.
   * @param salt     A salt bytes that modifies the hash to protect against Rainbow table attacks. Must be encoded with `base64`.
   * @param logN     CPU/memory cost parameter
   * @param r        The block size parameter, which fine-tunes sequential memory read size and performance.
   * @param p        Parallelization parameter.
   * @param dkLen    Intended output length in octets of the derived key.
   */
  def scrypt(password: String, salt: String, logN: Long, r: Double, p: Long, dkLen: Long): Future[Either[Throwable, String]] = ctx
    .requestField[ScryptArgs, String](ScryptArgs(password, salt, logN, r, p, dkLen))


  /**
   * Calculates SHA256 hash of the specified data.
   *
   * @param data Input data for hash calculation. Encoded with `base64`.
   */
  def sha256(data: String): Future[Either[Throwable, String]] = ctx.requestField[Sha256Args, String](Sha256Args(data))

  /**
   * Calculates SHA512 hash of the specified data.
   *
   * @param data Input data for hash calculation. Encoded with `base64`.
   */
  def sha512(data: String): Future[Either[Throwable, String]] = ctx.requestField[Sha512Args, String](Sha512Args(data))


  /**
   * Signs a data using the provided keys.
   *
   * @param unsigned Data that must be signed encoded in `base64`.
   * @param keys     Sign keys.
   */
  def sign(unsigned: String, keys: KeyPair): Future[Either[Throwable, ResultOfSign]] = ctx
    .requestValue[SignArgs, ResultOfSign](SignArgs(unsigned, keys))

  /**
   * Calculates CRC16 using TON algorithm.
   *
   * @param data Input data for CRC calculation. Encoded with `base64`.
   */
  def tonCrc16(data: String): Future[Either[Throwable, Long]] = ctx.requestField[TonCrc16Args, Long](TonCrc16Args(data))

  /**
   * Verifies signed data using the provided public key. Raises error if verification is failed.
   *
   * @param signed Signed data that must be verified encoded in `base64`.
   * @param public Signer's public key - 64 symbols hex string
   */
  def verifySignature(signed: String, public: String): Future[Either[Throwable, String]] = ctx
    .requestField[VerifySignatureArgs, String](VerifySignatureArgs(signed, public))
}

