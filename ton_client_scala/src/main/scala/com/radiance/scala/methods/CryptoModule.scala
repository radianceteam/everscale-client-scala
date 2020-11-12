package com.radiance.scala.methods

import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.types.CryptoTypes._

import scala.concurrent.Future

class CryptoModule(private val ctx: TonContextScala) {
  /**
   *  Performs symmetric `chacha20` encryption.@param data  Source data to be encrypted or decrypted. Must be encoded with `base64`.
   * @param key  256-bit key. Must be encoded with `hex`.
   * @param nonce  96-bit nonce. Must be encoded with `hex`.
   */
  def chacha20(data: String, key: String, nonce: String): Future[Either[Throwable, ResultOfChaCha20]] = {
    val arg = ParamsOfChaCha20(data, key, nonce)
    ctx.execAsync("crypto.chacha20", arg)
  }
  /**  Extracts the public key from the serialized extended private key@param xprv  Serialized extended private key */
  def hdkey_public_from_xprv(xprv: String): Future[Either[Throwable, ResultOfHDKeyPublicFromXPrv]] = {
    val arg = ParamsOfHDKeyPublicFromXPrv(xprv: String)
    ctx.execAsync("crypto.hdkey_public_from_xprv", arg)
  }
  /**  Extracts the private key from the serialized extended private key@param xprv  Serialized extended private key */
  def hdkey_secret_from_xprv(xprv: String): Future[Either[Throwable, ResultOfHDKeySecretFromXPrv]] = {
    val arg = ParamsOfHDKeySecretFromXPrv(xprv)
    ctx.execAsync("crypto.hdkey_secret_from_xprv", arg)
  }
  /**
   *  Derives the exented private key from the specified key and path@param xprv  Serialized extended private key
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   */
  def hdkey_derive_from_xprv_path(xprv: String, path: String): Future[Either[Throwable, ResultOfHDKeyDeriveFromXPrvPath]] = {
    val arg = ParamsOfHDKeyDeriveFromXPrvPath(xprv, path)
    ctx.execAsync("crypto.hdkey_derive_from_xprv_path", arg)
  }
  /**
   *  Returns extended private key derived from the specified extended private key and child index@param xprv  Serialized extended private key
   * @param child_index  Child index (see BIP-0032)
   * @param hardened  Indicates the derivation of hardened/not-hardened key (see BIP-0032)
   */
  def hdkey_derive_from_xprv(xprv: String, child_index: Long, hardened: Boolean): Future[Either[Throwable, ResultOfHDKeyDeriveFromXPrv]] = {
    val arg = ParamsOfHDKeyDeriveFromXPrv(xprv, child_index, hardened)
    ctx.execAsync("crypto.hdkey_derive_from_xprv", arg)
  }
  /**
   *  Generates an extended master private key that will be the root for all the derived keys@param phrase  String with seed phrase
   * @param dictionary  Dictionary identifier
   * @param word_count  Mnemonic word count
   */
  def hdkey_xprv_from_mnemonic(phrase: String, dictionary: Option[Long], word_count: Option[Long]): Future[Either[Throwable, ResultOfHDKeyXPrvFromMnemonic]] = {
    val arg = ParamsOfHDKeyXPrvFromMnemonic(phrase, dictionary, word_count)
    ctx.execAsync("crypto.hdkey_xprv_from_mnemonic", arg)
  }
  /**
   * Derives a key pair for signing from the seed phrase
   *  Validates the seed phrase, generates master key and then derives
   *  the key pair from the master key and the specified path@param phrase  Phrase
   * @param path  Derivation path, for instance "m/44'/396'/0'/0/0"
   * @param dictionary  Dictionary identifier
   * @param word_count  Word count
   */
  def mnemonic_derive_sign_keys(phrase: String, path: Option[String], dictionary: Option[Long], word_count: Option[Long]): Future[Either[Throwable, KeyPair]] = {
    val arg = ParamsOfMnemonicDeriveSignKeys(phrase, path, dictionary, word_count)
    ctx.execAsync("crypto.mnemonic_derive_sign_keys", arg)
  }
  /**
   * Validates a mnemonic phrase
   *  The phrase supplied will be checked for word length and validated according to the checksum
   *  specified in BIP0039.@param phrase  Phrase
   * @param dictionary  Dictionary identifier
   * @param word_count  Word count
   */
  def mnemonic_verify(phrase: String, dictionary: Option[Long], word_count: Option[Long]): Future[Either[Throwable, ResultOfMnemonicVerify]] = {
    val arg = ParamsOfMnemonicVerify(phrase, dictionary, word_count)
    ctx.execAsync("crypto.mnemonic_verify", arg)
  }
  /**
   * Generates mnemonic from the specified entropy
   *  Generates mnemonic from pre-generated entropy@param entropy  Entropy bytes. Hex encoded.
   * @param dictionary  Dictionary identifier
   * @param word_count  Mnemonic word count
   */
  def mnemonic_from_entropy(entropy: String, dictionary: Option[Long], word_count: Option[Long]): Future[Either[Throwable, ResultOfMnemonicFromEntropy]] = {
    val arg = ParamsOfMnemonicFromEntropy(entropy, dictionary, word_count)
    ctx.execAsync("crypto.mnemonic_from_entropy", arg)
  }
  /**
   *  Generates a random mnemonic from the specified dictionary and word count@param dictionary  Dictionary identifier
   * @param word_count  Mnemonic word count
   */
  def mnemonic_from_random(dictionary: Option[Long], word_count: Option[Long]): Future[Either[Throwable, ResultOfMnemonicFromRandom]] = {
    val arg = ParamsOfMnemonicFromRandom(dictionary, word_count)
    ctx.execAsync("crypto.mnemonic_from_random", arg)
  }
  /**  Prints the list of words from the specified dictionary@param dictionary  Dictionary identifier */
  def mnemonic_words(dictionary: Option[Long]): Future[Either[Throwable, ResultOfMnemonicWords]] = {
    val arg = ParamsOfMnemonicWords(dictionary)
    ctx.execAsync("crypto.mnemonic_words", arg)
  }
  /**
   *  Decrypts and verifies cipher text using `nonce` and secret `key`.@param encrypted  Data that must be decrypted. Encoded with `base64`.
   * @param nonce  Nonce in `hex`
   * @param key  Public key - unprefixed 0-padded to 64 symbols hex string
   */
  def nacl_secret_box_open(encrypted: String, nonce: String, key: String): Future[Either[Throwable, ResultOfNaclBoxOpen]] = {
    val arg = ParamsOfNaclSecretBoxOpen(encrypted, nonce, key)
    ctx.execAsync("crypto.nacl_secret_box_open", arg)
  }
  /**
   *  Encrypt and authenticate message using nonce and secret key.@param decrypted  Data that must be encrypted. Encoded with `base64`.
   * @param nonce  Nonce in `hex`
   * @param key  Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def nacl_secret_box(decrypted: String, nonce: String, key: String): Future[Either[Throwable, ResultOfNaclBox]] = {
    val arg = ParamsOfNaclSecretBox(decrypted, nonce, key)
    ctx.execAsync("crypto.nacl_secret_box", arg)
  }
  /**
   *  Decrypt and verify the cipher text using the recievers secret key, the senders public
   *  key, and the nonce.@param encrypted  Data that must be decrypted. Encoded with `base64`.
   * @param nonce
   * @param their_public  Sender's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secret  Receiver's private key - unprefixed 0-padded to 64 symbols hex string
   */
  def nacl_box_open(encrypted: String, nonce: String, their_public: String, secret: String): Future[Either[Throwable, ResultOfNaclBoxOpen]] = {
    val arg = ParamsOfNaclBoxOpen(encrypted, nonce, their_public, secret)
    ctx.execAsync("crypto.nacl_box_open", arg)
  }
  /**
   *  Public key authenticated encryption
   *
   *  Encrypt and authenticate a message using the senders secret key, the recievers public
   *  key, and a nonce. @param decrypted  Data that must be encrypted encoded in `base64`.
   * @param nonce  Nonce, encoded in `hex`
   * @param their_public  Receiver's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secret  Sender's private key - unprefixed 0-padded to 64 symbols hex string
   */
  def nacl_box(decrypted: String, nonce: String, their_public: String, secret: String): Future[Either[Throwable, ResultOfNaclBox]] = {
    val arg = ParamsOfNaclBox(decrypted, nonce, their_public, secret)
    ctx.execAsync("crypto.nacl_box", arg)
  }
  /**  Generates key pair from a secret key@param secret  Secret key - unprefixed 0-padded to 64 symbols hex string  */
  def nacl_box_keypair_from_secret_key(secret: String): Future[Either[Throwable, KeyPair]] = {
    val arg = ParamsOfNaclBoxKeyPairFromSecret(secret)
    ctx.execAsync("crypto.nacl_box_keypair_from_secret_key", arg)
  }
  /**
   */
  def nacl_box_keypair(): Future[Either[Throwable, KeyPair]] = {
    ctx.execAsyncVoid[KeyPair]("crypto.nacl_box_keypair")
  }
  /**
   * @param unsigned  Data that must be signed encoded in `base64`.
   * @param secret  Signer's secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def nacl_sign_detached(unsigned: String, secret: String): Future[Either[Throwable, ResultOfNaclSignDetached]] = {
    val arg = ParamsOfNaclSignDetached(unsigned, secret)
    ctx.execAsync("crypto.nacl_sign_detached", arg)
  }
  /**
   * @param signed  Signed data that must be unsigned. Encoded with `base64`.
   * @param public  Signer's public key - unprefixed 0-padded to 64 symbols hex string
   */
  def nacl_sign_open(signed: String, public: String): Future[Either[Throwable, ResultOfNaclSignOpen]] = {
    val arg = ParamsOfNaclSignOpen(signed, public)
    ctx.execAsync("crypto.nacl_sign_open", arg)
  }
  /**
   *  Signs data using the signer's secret key.@param unsigned  Data that must be signed encoded in `base64`.
   * @param secret  Signer's secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def nacl_sign(unsigned: String, secret: String): Future[Either[Throwable, ResultOfNaclSign]] = {
    val arg = ParamsOfNaclSign(unsigned, secret)
    ctx.execAsync("crypto.nacl_sign", arg)
  }
  /**  Generates a key pair for signing from the secret key@param secret  Secret key - unprefixed 0-padded to 64 symbols hex string  */
  def nacl_sign_keypair_from_secret_key(secret: String): Future[Either[Throwable, KeyPair]] = {
    val arg = ParamsOfNaclSignKeyPairFromSecret(secret)
    ctx.execAsync("crypto.nacl_sign_keypair_from_secret_key", arg)
  }
  /**
   * Perform `scrypt` encryption
   *  Derives key from `password` and `key` using `scrypt` algorithm.
   *  See [https://en.wikipedia.org/wiki/Scrypt].
   *
   *  # Arguments
   *  - `log_n` - The log2 of the Scrypt parameter `N`
   *  - `r` - The Scrypt parameter `r`
   *  - `p` - The Scrypt parameter `p`
   *  # Conditions
   *  - `log_n` must be less than `64`
   *  - `r` must be greater than `0` and less than or equal to `4294967295`
   *  - `p` must be greater than `0` and less than `4294967295`
   *  # Recommended values sufficient for most use-cases
   *  - `log_n = 15` (`n = 32768`)
   *  - `r = 8`
   *  - `p = 1`@param password  The password bytes to be hashed.
   *  Must be encoded with `base64`.
   * @param salt  Salt bytes that modify the hash to protect against Rainbow table attacks.
   *  Must be encoded with `base64`.
   * @param log_n  CPU/memory cost parameter
   * @param r  The block size parameter, which fine-tunes sequential memory read size and performance.
   * @param p  Parallelization parameter.
   * @param dk_len  Intended output length in octets of the derived key.
   */
  def scrypt(password: String, salt: String, log_n: Long, r: Long, p: Long, dk_len: Long): Future[Either[Throwable, ResultOfScrypt]] = {
    val arg = ParamsOfScrypt(password, salt, log_n, r, p, dk_len)
    ctx.execAsync("crypto.scrypt", arg)
  }
  /**  Calculates SHA512 hash of the specified data.@param data  Input data for hash calculation. Encoded with `base64`. */
  def sha512(data: String): Future[Either[Throwable, ResultOfHash]] = {
    val arg = ParamsOfHash512(data)
    ctx.execAsync("crypto.sha512", arg)
  }
  /**  Calculates SHA256 hash of the specified data.@param data  Input data for hash calculation. Encoded with `base64`. */
  def sha256(data: String): Future[Either[Throwable, ResultOfHash]] = {
    val arg = ParamsOfHash256(data)
    ctx.execAsync("crypto.sha512", arg)
  }
  /**
   *  Verifies signed data using the provided public key.
   *  Raises error if verification is failed.@param signed  Signed data that must be verified encoded in `base64`.
   * @param public  Signer's public key - 64 symbols hex string
   */
  def verify_signature(signed: String, public: String): Future[Either[Throwable, ResultOfVerifySignature]] = {
    val arg = ParamsOfVerifySignature(signed, public)
    ctx.execAsync("crypto.verify_signature", arg)
  }
  /**
   *  Signs a data using the provided keys.@param unsigned  Data that must be signed encoded in `base64`.
   * @param keys  Sign keys.
   */
  def sign(unsigned: String, keys: KeyPair): Future[Either[Throwable, ResultOfSign]] = {
    val arg = ParamsOfSign(unsigned, keys)
    ctx.execAsync("crypto.sign", arg)
  }
  /**  Generates random ed25519 key pair. */
  def generate_random_sign_keys(): Future[Either[Throwable, KeyPair]] = {
    ctx.execAsyncVoid[KeyPair]("crypto.generate_random_sign_keys")
  }
  /**  Converts public key to ton safe_format@param public_key  Public key - 64 symbols hex string */
  def convert_public_key_to_ton_safe_format(public_key: String): Future[Either[Throwable, ResultOfConvertPublicKeyToTonSafeFormat]] = {
    val arg = ParamsOfConvertPublicKeyToTonSafeFormat(public_key)
    ctx.execAsync("crypto.convert_public_key_to_ton_safe_format", arg)
  }
  /**  Generates random byte array of the specified length and returns it in `base64` format@param length  Size of random byte array. */
  def generate_random_bytes(length: Long): Future[Either[Throwable, ResultOfGenerateRandomBytes]] = {
    val arg = ParamsOfGenerateRandomBytes(length)
    ctx.execAsync("crypto.generate_random_bytes", arg)
  }
  /**  Calculates CRC16 using TON algorithm.@param data  Input data for CRC calculation. Encoded with `base64`. */
  def ton_crc16(data: String): Future[Either[Throwable, ResultOfTonCrc16]] = {
    val arg = ParamsOfTonCrc16(data)
    ctx.execAsync("crypto.ton_crc16", arg)
  }
  /**
   * Modular exponentiation
   *  Performs modular exponentiation for big integers (`base`^^`exponent` mod `modulus`).
   *  See [https://en.wikipedia.org/wiki/Modular_exponentiation]@param base  `base` argument of calculation.
   * @param exponent  `exponent` argument of calculation.
   * @param modulus  `modulus` argument of calculation.
   */
  def modular_power(base: String, exponent: String, modulus: String): Future[Either[Throwable, ResultOfModularPower]] = {
    val arg = ParamsOfModularPower(base, exponent, modulus)
    ctx.execAsync("crypto.modular_power", arg)
  }
  /**
   * Integer factorization
   *  Performs prime factorization – decomposition of a composite number
   *  into a product of smaller prime integers (factors).
   *  See [https://en.wikipedia.org/wiki/Integer_factorization]@param composite  Hexadecimal representation of u64 composite number.
   */
  def factorize(composite: String): Future[Either[Throwable, ResultOfFactorize]] = {
    val arg = ParamsOfFactorize(composite)
    ctx.execAsync("crypto.factorize", arg)
  }
}
