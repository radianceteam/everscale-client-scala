package com.radiance.jvm.crypto

import com.radiance.jvm.Context
import com.radiance.jvm.app.AppObject

import scala.concurrent.Future

class CryptoModule(private val ctx: Context) {

  /**
   * Performs symmetric `chacha20` encryption.
   * @param data
   *   Source data to be encrypted or decrypted. Must be encoded with `base64`.
   * @param key
   *   256-bit key. Must be encoded with `hex`.
   * @param nonce
   *   96-bit nonce. Must be encoded with `hex`.
   */
  def chacha20(
    data: String,
    key: String,
    nonce: String
  ): Either[Throwable, ResultOfChaCha20] =
    ctx
      .execSync("crypto.chacha20", ParamsOfChaCha20(data, key, nonce))

  /**
   * Converts public key to ton safe_format
   * @param public_key
   *   Public key - 64 symbols hex string
   */
  def convertPublicKeyToTonSafeFormat(
    public_key: String
  ): Either[Throwable, ResultOfConvertPublicKeyToTonSafeFormat] =
    ctx.execSync(
      "crypto.convert_public_key_to_ton_safe_format",
      ParamsOfConvertPublicKeyToTonSafeFormat(public_key)
    )

  /**
   * Integer factorization Performs prime factorization – decomposition of a composite number into a product of smaller
   * prime integers (factors). See [https://en.wikipedia.org/wiki/Integer_factorization]
   * @param composite
   *   Hexadecimal representation of u64 composite number.
   */
  def factorize(composite: String): Either[Throwable, ResultOfFactorize] =
    ctx
      .execSync("crypto.factorize", ParamsOfFactorize(composite))

  /**
   * Generates random byte array of the specified length and returns it in `base64` format
   * @param length
   *   Size of random byte array.
   */
  def generateRandomBytes(
    length: Long
  ): Either[Throwable, ResultOfGenerateRandomBytes] =
    ctx
      .execSync(
        "crypto.generate_random_bytes",
        ParamsOfGenerateRandomBytes(length)
      )

  /**
   * Generates random ed25519 key pair.
   */
  def generateRandomSignKeys: Either[Throwable, KeyPair] =
    ctx
      .execSyncParameterless[KeyPair]("crypto.generate_random_sign_keys")

  /**
   * @param public
   *   public
   * @param secret
   *   secret
   */
  def getSigningBox(
    public: String,
    secret: String
  ): Either[Throwable, RegisteredSigningBox] = {
    val arg = KeyPair(public, secret)
    ctx.execSync("crypto.get_signing_box", arg)
  }

  /**
   * Returns extended private key derived from the specified extended private key and child index
   * @param xprv
   *   Serialized extended private key
   * @param child_index
   *   Child index (see BIP-0032)
   * @param hardened
   *   Indicates the derivation of hardened/not-hardened key (see BIP-0032)
   */
  def hdkeyDeriveFromXprv(
    xprv: String,
    child_index: Long,
    hardened: Boolean
  ): Either[Throwable, ResultOfHDKeyDeriveFromXPrv] =
    ctx.execSync(
      "crypto.hdkey_derive_from_xprv",
      ParamsOfHDKeyDeriveFromXPrv(xprv, child_index, hardened)
    )

  /**
   * Derives the exented private key from the specified key and path
   * @param xprv
   *   Serialized extended private key
   * @param path
   *   Derivation path, for instance "m/44'/396'/0'/0/0"
   */
  def hdkeyDeriveFromXprvPath(
    xprv: String,
    path: String
  ): Either[Throwable, ResultOfHDKeyDeriveFromXPrvPath] =
    ctx
      .execSync(
        "crypto.hdkey_derive_from_xprv_path",
        ParamsOfHDKeyDeriveFromXPrvPath(xprv, path)
      )

  /**
   * Extracts the public key from the serialized extended private key
   * @param xprv
   *   Serialized extended private key
   */
  def hdkeyPublicFromXprv(
    xprv: String
  ): Either[Throwable, ResultOfHDKeyPublicFromXPrv] =
    ctx
      .execSync(
        "crypto.hdkey_public_from_xprv",
        ParamsOfHDKeyPublicFromXPrv(xprv: String)
      )

  /**
   * Extracts the private key from the serialized extended private key
   * @param xprv
   *   Serialized extended private key
   */
  def hdkeySecretFromXprv(
    xprv: String
  ): Either[Throwable, ResultOfHDKeySecretFromXPrv] =
    ctx
      .execSync(
        "crypto.hdkey_secret_from_xprv",
        ParamsOfHDKeySecretFromXPrv(xprv)
      )

  /**
   * Generates an extended master private key that will be the root for all the derived keys
   * @param phrase
   *   String with seed phrase
   * @param dictionary
   *   Dictionary identifier
   * @param word_count
   *   Mnemonic word count
   */
  def hdkeyXprvFromMnemonic(
    phrase: String,
    dictionary: Option[Long],
    word_count: Option[Long]
  ): Either[Throwable, ResultOfHDKeyXPrvFromMnemonic] =
    ctx.execSync(
      "crypto.hdkey_xprv_from_mnemonic",
      ParamsOfHDKeyXPrvFromMnemonic(phrase, dictionary, word_count)
    )

  /**
   * Derives a key pair for signing from the seed phrase Validates the seed phrase, generates master key and then
   * derives the key pair from the master key and the specified path
   * @param phrase
   *   Phrase
   * @param path
   *   Derivation path, for instance "m/44'/396'/0'/0/0"
   * @param dictionary
   *   Dictionary identifier
   * @param word_count
   *   Word count
   */
  def mnemonicDeriveSignKeys(
    phrase: String,
    path: Option[String],
    dictionary: Option[Long],
    word_count: Option[Long]
  ): Either[Throwable, KeyPair] =
    ctx.execSync(
      "crypto.mnemonic_derive_sign_keys",
      ParamsOfMnemonicDeriveSignKeys(phrase, path, dictionary, word_count)
    )

  /**
   * Generates mnemonic from the specified entropy Generates mnemonic from pre-generated entropy
   * @param entropy
   *   Entropy bytes. Hex encoded.
   * @param dictionary
   *   Dictionary identifier
   * @param word_count
   *   Mnemonic word count
   */
  def mnemonicFromEntropy(
    entropy: String,
    dictionary: Option[Long],
    word_count: Option[Long]
  ): Either[Throwable, ResultOfMnemonicFromEntropy] =
    ctx.execSync(
      "crypto.mnemonic_from_entropy",
      ParamsOfMnemonicFromEntropy(entropy, dictionary, word_count)
    )

  /**
   * Generates a random mnemonic from the specified dictionary and word count@param dictionary Dictionary identifier
   * @param word_count
   *   Mnemonic word count
   */
  def mnemonicFromRandom(
    dictionary: Option[Long],
    word_count: Option[Long]
  ): Either[Throwable, ResultOfMnemonicFromRandom] =
    ctx.execSync(
      "crypto.mnemonic_from_random",
      ParamsOfMnemonicFromRandom(dictionary, word_count)
    )

  /**
   * Validates a mnemonic phrase The phrase supplied will be checked for word length and validated according to the
   * checksum specified in BIP0039.
   * @param phrase
   *   Phrase
   * @param dictionary
   *   Dictionary identifier
   * @param word_count
   *   Word count
   */
  def mnemonicVerify(
    phrase: String,
    dictionary: Option[Long],
    word_count: Option[Long]
  ): Either[Throwable, ResultOfMnemonicVerify] =
    ctx.execSync(
      "crypto.mnemonic_verify",
      ParamsOfMnemonicVerify(phrase, dictionary, word_count)
    )

  /**
   * Prints the list of words from the specified dictionary
   * @param dictionary
   *   Dictionary identifier
   */
  def mnemonicWords(
    dictionary: Option[Long]
  ): Either[Throwable, ResultOfMnemonicWords] =
    ctx
      .execSync("crypto.mnemonic_words", ParamsOfMnemonicWords(dictionary))

  /**
   * Modular exponentiation Performs modular exponentiation for big integers (`base`^^`exponent` mod `modulus`). See
   * [https://en.wikipedia.org/wiki/Modular_exponentiation]
   * @param base
   *   `base` argument of calculation.
   * @param exponent
   *   `exponent` argument of calculation.
   * @param modulus
   *   `modulus` argument of calculation.
   */
  def modularPower(
    base: String,
    exponent: String,
    modulus: String
  ): Either[Throwable, ResultOfModularPower] =
    ctx.execSync(
      "crypto.modular_power",
      ParamsOfModularPower(base, exponent, modulus)
    )

  /**
   * Public key authenticated encryption
   *
   * Encrypt and authenticate a message using the senders secret key, the recievers public key, and a nonce.
   * @param decrypted
   *   Data that must be encrypted encoded in `base64`.
   * @param nonce
   *   Nonce, encoded in `hex`
   * @param their_public
   *   Receiver's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secret
   *   Sender's private key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclBox(
    decrypted: String,
    nonce: String,
    their_public: String,
    secret: String
  ): Either[Throwable, ResultOfNaclBox] =
    ctx.execSync(
      "crypto.nacl_box",
      ParamsOfNaclBox(decrypted, nonce, their_public, secret)
    )

  def naclBoxKeypair: Either[Throwable, KeyPair] =
    ctx.execSyncParameterless[KeyPair]("crypto.nacl_box_keypair")

  /**
   * Generates key pair from a secret key
   * @param secret
   *   Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclBoxKeypairFromSecretKey(secret: String): Either[Throwable, KeyPair] =
    ctx
      .execSync(
        "crypto.nacl_box_keypair_from_secret_key",
        ParamsOfNaclBoxKeyPairFromSecret(secret)
      )

  /**
   * Decrypt and verify the cipher text using the recievers secret key, the senders public key, and the nonce.@param
   * encrypted Data that must be decrypted. Encoded with `base64`.
   * @param encrypted
   *   text
   * @param nonce
   *   nonce
   * @param their_public
   *   Sender's public key - unprefixed 0-padded to 64 symbols hex string
   * @param secret
   *   Receiver's private key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclBoxOpen(
    encrypted: String,
    nonce: String,
    their_public: String,
    secret: String
  ): Either[Throwable, ResultOfNaclBoxOpen] =
    ctx.execSync(
      "crypto.nacl_box_open",
      ParamsOfNaclBoxOpen(encrypted, nonce, their_public, secret)
    )

  /**
   * Encrypt and authenticate message using nonce and secret key.@param decrypted Data that must be encrypted. Encoded
   * with `base64`.
   * @param nonce
   *   Nonce in `hex`
   * @param key
   *   Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSecretBox(
    decrypted: String,
    nonce: String,
    key: String
  ): Either[Throwable, ResultOfNaclBox] =
    ctx
      .execSync(
        "crypto.nacl_secret_box",
        ParamsOfNaclSecretBox(decrypted, nonce, key)
      )

  /**
   * Decrypts and verifies cipher text using `nonce` and secret `key`.@param encrypted Data that must be decrypted.
   * Encoded with `base64`.
   * @param nonce
   *   Nonce in `hex`
   * @param key
   *   Public key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSecretBoxOpen(
    encrypted: String,
    nonce: String,
    key: String
  ): Either[Throwable, ResultOfNaclBoxOpen] =
    ctx
      .execSync(
        "crypto.nacl_secret_box_open",
        ParamsOfNaclSecretBoxOpen(encrypted, nonce, key)
      )

  /**
   * Signs data using the signer's secret key.@param unsigned Data that must be signed encoded in `base64`.
   * @param secret
   *   Signer's secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSign(
    unsigned: String,
    secret: String
  ): Either[Throwable, ResultOfNaclSign] =
    ctx
      .execSync("crypto.nacl_sign", ParamsOfNaclSign(unsigned, secret))

  /**
   * @param unsigned
   *   Data that must be signed encoded in `base64`.
   * @param secret
   *   Signer's secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSignDetached(
    unsigned: String,
    secret: String
  ): Either[Throwable, ResultOfNaclSignDetached] =
    ctx
      .execSync(
        "crypto.nacl_sign_detached",
        ParamsOfNaclSignDetached(unsigned, secret)
      )

  // TODO add test
  /**
   * Verifies the signature with public key and `unsigned` data.
   * @param unsigned
   *   Encoded with `base64`.
   * @param signature
   *   Encoded with `hex`.
   * @param public
   */
  def naclSignDetachedVerify(
    unsigned: String,
    signature: String,
    public: String
  ): Either[Throwable, ResultOfNaclSignDetachedVerify] = {
    val arg = ParamsOfNaclSignDetachedVerify(unsigned, signature, public)
    ctx
      .execSync(
        "crypto.nacl_sign_detached_verify",
        arg
      )
  }

  /**
   * Generates a key pair for signing from the secret key
   * @param secret
   *   Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSignKeypairFromSecretKey(secret: String): Either[Throwable, KeyPair] =
    ctx
      .execSync(
        "crypto.nacl_sign_keypair_from_secret_key",
        ParamsOfNaclSignKeyPairFromSecret(secret)
      )

  /**
   * @param signed
   *   Signed data that must be unsigned. Encoded with `base64`.
   * @param public
   *   Signer's public key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSignOpen(
    signed: String,
    public: String
  ): Either[Throwable, ResultOfNaclSignOpen] =
    ctx
      .execSync("crypto.nacl_sign_open", ParamsOfNaclSignOpen(signed, public))

  /**
   * @param app_object
   *   app_object
   */
  def registerSigningBox(
    app_object: AppObject[ParamsOfAppSigningBox, ResultOfAppSigningBox]
  ): Future[Either[Throwable, RegisteredSigningBox]] = {
    ctx
      .registerAppObject[RegisteredSigningBox, ParamsOfAppSigningBox, ResultOfAppSigningBox](
        "crypto.register_signing_box",
        "",
        app_object
      )
  }

  /**
   * @param handle
   *   handle
   */
  def removeSigningBox(handle: SigningBoxHandle): Future[Either[Throwable, Unit]] = {
    ctx.unregisterAppObject(handle.value.toInt, "crypto.remove_signing_box")
  }

  /**
   * Perform `scrypt` encryption Derives key from `password` and `key` using `scrypt` algorithm. See
   * [https://en.wikipedia.org/wiki/Scrypt].
   *
   * # Arguments
   *   - `log_n` - The log2 of the Scrypt parameter `N`
   *   - `r` - The Scrypt parameter `r`
   *   - `p` - The Scrypt parameter `p` # Conditions
   *   - `log_n` must be less than `64`
   *   - `r` must be greater than `0` and less than or equal to `4294967295`
   *   - `p` must be greater than `0` and less than `4294967295` # Recommended values sufficient for most use-cases
   *   - `log_n = 15` (`n = 32768`)
   *   - `r = 8`
   *   - `p = 1`@param password The password bytes to be hashed. Must be encoded with `base64`.
   * @param salt
   *   Salt bytes that modify the hash to protect against Rainbow table attacks. Must be encoded with `base64`.
   * @param log_n
   *   CPU/memory cost parameter
   * @param r
   *   The block size parameter, which fine-tunes sequential memory read size and performance.
   * @param p
   *   Parallelization parameter.
   * @param dk_len
   *   Intended output length in octets of the derived key.
   */
  def scrypt(
    password: String,
    salt: String,
    log_n: Long,
    r: Long,
    p: Long,
    dk_len: Long
  ): Either[Throwable, ResultOfScrypt] =
    ctx.execSync(
      "crypto.scrypt",
      ParamsOfScrypt(password, salt, log_n, r, p, dk_len)
    )

  /**
   * Calculates SHA256 hash of the specified data.
   * @param data
   *   Input data for hash calculation. Encoded with `base64`.
   */
  def sha256(data: String): Either[Throwable, ResultOfHash] =
    ctx.execSync("crypto.sha256", ParamsOfHash256(data))

  /**
   * Calculates SHA512 hash of the specified data.
   * @param data
   *   Input data for hash calculation. Encoded with `base64`.
   */
  def sha512(data: String): Either[Throwable, ResultOfHash] =
    ctx.execSync("crypto.sha512", ParamsOfHash512(data))

  /**
   * Signs a data using the provided keys.
   * @param unsigned
   *   Data that must be signed encoded in `base64`.
   * @param keys
   *   Sign keys.
   */
  def sign(unsigned: String, keys: KeyPair): Either[Throwable, ResultOfSign] =
    ctx
      .execSync("crypto.sign", ParamsOfSign(unsigned, keys))

  /**
   * @param handle
   */
  def signingBoxGetPublicKey(
    handle: SigningBoxHandle
  ): Either[Throwable, ResultOfSigningBoxGetPublicKey] = {
    val arg = RegisteredSigningBox1(handle)
    ctx.execSync("crypto.signing_box_get_public_key", arg)
  }

  /**
   * @param signing_box
   *   signing_box
   * @param unsigned
   *   Must be encoded with `base64`.
   */
  def signingBoxSign(
    signing_box: SigningBoxHandle,
    unsigned: String
  ): Either[Throwable, ResultOfSigningBoxSign] = {
    val arg = ParamsOfSigningBoxSign(signing_box, unsigned)
    ctx.execSync("crypto.signing_box_sign", arg)
  }

  /**
   * Calculates CRC16 using TON algorithm.
   * @param data
   *   Input data for CRC calculation. Encoded with `base64`.
   */
  def tonCrc16(data: String): Either[Throwable, ResultOfTonCrc16] =
    ctx
      .execSync("crypto.ton_crc16", ParamsOfTonCrc16(data))

  /**
   * Verifies signed data using the provided public key. Raises error if verification is failed.
   * @param signed
   *   Signed data that must be verified encoded in `base64`.
   * @param public
   *   Signer's public key - 64 symbols hex string
   */
  def verifySignature(
    signed: String,
    public: String
  ): Either[Throwable, ResultOfVerifySignature] =
    ctx
      .execSync(
        "crypto.verify_signature",
        ParamsOfVerifySignature(signed, public)
      )

}
