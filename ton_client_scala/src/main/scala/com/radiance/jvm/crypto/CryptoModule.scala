package com.radiance.jvm.crypto

import com.radiance.jvm.Context
import com.radiance.jvm.app.AppObject
import com.radiance.jvm.crypto.EncryptionAlgorithmADT.EncryptionAlgorithm

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
      .execSync[ParamsOfChaCha20, ResultOfChaCha20]("crypto.chacha20", ParamsOfChaCha20(data, key, nonce))

  /**
   * Removes cached secrets (overwrites with zeroes) from all signing and encryption boxes, derived from crypto box.
   * @param handle
   *   handle
   */
  def clearCryptoBoxSecretCache(handle: CryptoBoxHandle): Future[Either[Throwable, Unit]] = {
    ctx
      .execAsync[CryptoBoxHandle, Unit]("crypto.clear_crypto_box_secret_cache", handle)
  }

  /**
   * Converts public key to ton safe_format
   * @param public_key
   *   Public key - 64 symbols hex string
   */
  def convertPublicKeyToTonSafeFormat(
    public_key: String
  ): Either[Throwable, ResultOfConvertPublicKeyToTonSafeFormat] =
    ctx.execSync[ParamsOfConvertPublicKeyToTonSafeFormat, ResultOfConvertPublicKeyToTonSafeFormat](
      "crypto.convert_public_key_to_ton_safe_format",
      ParamsOfConvertPublicKeyToTonSafeFormat(public_key)
    )

  /**
   * Creates a Crypto Box instance. Crypto Box is a root crypto object, that encapsulates some secret (seed phrase
   * usually) in encrypted form and acts as a factory for all crypto primitives used in SDK: keys for signing and
   * encryption, derived from this secret.
   *
   * Crypto Box encrypts original Seed Phrase with salt and password that is retrieved from `password_provider`
   * callback, implemented on Application side.
   *
   * When used, decrypted secret shows up in core library's memory for a very short period of time and then is
   * immediately overwritten with zeroes.
   * @param secret_encryption_salt
   *   secret_encryption_salt
   * @param secret
   *   secret
   * @param password_provider
   *   password_provider
   */
  def createCryptoBox(
    secret_encryption_salt: String,
    secret: CryptoBoxSecretADT.CryptoBoxSecret,
    password_provider: AppObject[
      ParamsOfAppPasswordProviderADT.ParamsOfAppPasswordProvider,
      ResultOfAppPasswordProviderADT.ResultOfAppPasswordProvider
    ]
  ): Future[Either[Throwable, RegisteredCryptoBox]] = {
//    ctx.registerAppObject[
//      RegisteredCryptoBox,
//      ParamsOfAppPasswordProviderADT.ParamsOfAppPasswordProvider,
//      ResultOfAppPasswordProviderADT.ResultOfAppPasswordProvider
//    ](
//      "crypto.create_crypto_box",
//      secret_encryption_salt,
//      password_provider
//    )
    ???
  }

  /**
   * Creates encryption box with specified algorithm
   * @param algorithm
   *   algorithm
   */
  def createEncryptionBox(algorithm: EncryptionAlgorithm): Either[Throwable, RegisteredEncryptionBox] = {
    ctx.execSync[ParamsOfCreateEncryptionBox, RegisteredEncryptionBox](
      "crypto.create_encryption_box",
      ParamsOfCreateEncryptionBox(algorithm)
    )
  }

  /**
   * Decrypts data using given encryption box Note. Block cipher algorithms pad data to cipher block size so encrypted
   * data can be longer then original data. Client should store the original data size after encryption and use it after
   * decryption to retrieve the original data from decrypted data.
   * @param encryption_box
   *   encryption_box
   * @param data
   *   data
   */
  def encryptionBoxDecrypt(
    encryption_box: EncryptionBoxHandle,
    data: String
  ): Either[Throwable, ResultOfEncryptionBoxDecrypt] = {
    ctx.execSync[ParamsOfEncryptionBoxDecrypt, ResultOfEncryptionBoxDecrypt](
      "crypto.encryption_box_decrypt",
      ParamsOfEncryptionBoxDecrypt(encryption_box, data)
    )
  }

  /**
   * Encrypts data using given encryption box Note. Block cipher algorithms pad data to cipher block size so encrypted
   * data can be longer then original data. Client should store the original data size after encryption and use it after
   * decryption to retrieve the original data from decrypted data.
   * @param encryption_box
   *   encryption_box
   * @param data
   *   data
   */
  def encryptionBoxEncrypt(
    encryption_box: EncryptionBoxHandle,
    data: String
  ): Either[Throwable, ResultOfEncryptionBoxEncrypt] = {
    ctx.execSync[ParamsOfEncryptionBoxEncrypt, ResultOfEncryptionBoxEncrypt](
      "crypto.encryption_box_encrypt",
      ParamsOfEncryptionBoxEncrypt(encryption_box, data)
    )
  }

  /**
   * Queries info from the given encryption box
   * @param encryption_box
   *   encryption_box
   */
  def encryptionBoxGetInfo(
    encryption_box: EncryptionBoxHandle
  ): Either[Throwable, ResultOfEncryptionBoxGetInfo] = {
    ctx.execSync[ParamsOfEncryptionBoxGetInfo, ResultOfEncryptionBoxGetInfo](
      "crypto.encryption_box_get_info",
      ParamsOfEncryptionBoxGetInfo(encryption_box)
    )
  }

  /**
   * Integer factorization Performs prime factorization – decomposition of a composite number into a product of smaller
   * prime integers (factors). See [https://en.wikipedia.org/wiki/Integer_factorization]
   * @param composite
   *   Hexadecimal representation of u64 composite number.
   */
  def factorize(composite: String): Either[Throwable, ResultOfFactorize] =
    ctx
      .execSync[ParamsOfFactorize, ResultOfFactorize]("crypto.factorize", ParamsOfFactorize(composite))

  /**
   * Generates random byte array of the specified length and returns it in `base64` format
   * @param length
   *   Size of random byte array.
   */
  def generateRandomBytes(
    length: Long
  ): Either[Throwable, ResultOfGenerateRandomBytes] =
    ctx
      .execSync[ParamsOfGenerateRandomBytes, ResultOfGenerateRandomBytes](
        "crypto.generate_random_bytes",
        ParamsOfGenerateRandomBytes(length)
      )

  /**
   * Generates random ed25519 key pair.
   */
  def generateRandomSignKeys: Either[Throwable, KeyPair] =
    ctx
      .execSync[Unit, KeyPair]("crypto.generate_random_sign_keys", ())

  /**
   * Get Crypto Box Info. Used to get `encrypted_secret` that should be used for all the cryptobox initializations
   * except the first one.
   * @param handle
   *   handle
   */
  def getCryptoBoxInfo(handle: CryptoBoxHandle): Future[Either[Throwable, ResultOfGetCryptoBoxInfo]] = {
    ctx
      .execAsync[CryptoBoxHandle, ResultOfGetCryptoBoxInfo]("crypto.get_crypto_box_info", handle)
  }

  /**
   * Get Crypto Box Seed Phrase. Attention! Store this data in your application for a very short period of time and
   * overwrite it with zeroes ASAP.
   * @param handle
   *   handle
   */
  def getCryptoBoxSeedPhrase(handle: CryptoBoxHandle): Future[Either[Throwable, ResultOfGetCryptoBoxSeedPhrase]] = {
    ctx
      .execAsync[CryptoBoxHandle, ResultOfGetCryptoBoxSeedPhrase]("crypto.get_crypto_box_seed_phrase", handle)
  }

  /**
   * Gets Encryption Box from Crypto Box. Derives encryption keypair from cryptobox secret and hdpath and stores it in
   * cache for `secret_lifetime` or until explicitly cleared by `clear_crypto_box_secret_cache` method. If
   * `secret_lifetime` is not specified - overwrites encryption secret with zeroes immediately after encryption
   * operation.
   * @param handle
   *   handle
   * @param hdpath
   *   By default, Everscale HD path is used.
   * @param algorithm
   *   algorithm
   * @param secret_lifetime
   *   secret_lifetime
   */
  def getEncryptionBoxFromCryptoBox(
    handle: Long,
    hdpath: Option[String],
    algorithm: BoxEncryptionAlgorithmADT.BoxEncryptionAlgorithm,
    secret_lifetime: Option[Long]
  ): Future[Either[Throwable, RegisteredEncryptionBox]] = {
    ctx
      .execAsync[ParamsOfGetEncryptionBoxFromCryptoBox, RegisteredEncryptionBox](
        "crypto.get_encryption_box_from_crypto_box",
        ParamsOfGetEncryptionBoxFromCryptoBox(handle, hdpath, algorithm, secret_lifetime)
      )
  }

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
    ctx.execSync[KeyPair, RegisteredSigningBox]("crypto.get_signing_box", KeyPair(public, secret))
  }

  /**
   * Get handle of Signing Box derived from Crypto Box.
   * @param handle
   *   handle
   * @param hdpath
   *   By default, Everscale HD path is used.
   * @param secret_lifetime
   *   secret_lifetime
   */
  def getSigningBoxFromCryptoBox(
    handle: Long,
    hdpath: Option[String],
    secret_lifetime: Option[Long]
  ): Future[Either[Throwable, RegisteredSigningBox]] = {
    ctx
      .execAsync[ParamsOfGetSigningBoxFromCryptoBox, RegisteredSigningBox](
        "crypto.get_encryption_box_from_crypto_box",
        ParamsOfGetSigningBoxFromCryptoBox(handle, hdpath, secret_lifetime)
      )
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
    ctx.execSync[ParamsOfHDKeyDeriveFromXPrv, ResultOfHDKeyDeriveFromXPrv](
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
      .execSync[ParamsOfHDKeyDeriveFromXPrvPath, ResultOfHDKeyDeriveFromXPrvPath](
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
      .execSync[ParamsOfHDKeyPublicFromXPrv, ResultOfHDKeyPublicFromXPrv](
        "crypto.hdkey_public_from_xprv",
        ParamsOfHDKeyPublicFromXPrv(xprv)
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
      .execSync[ParamsOfHDKeySecretFromXPrv, ResultOfHDKeySecretFromXPrv](
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
    ctx.execSync[ParamsOfHDKeyXPrvFromMnemonic, ResultOfHDKeyXPrvFromMnemonic](
      "crypto.hdkey_xprv_from_mnemonic",
      ParamsOfHDKeyXPrvFromMnemonic(phrase, dictionary, word_count)
    )

  /**
   * Derives a key pair for signing from the seed phrase Validates the seed phrase, generates master key and then
   * derives the key pair from the master key and the specified path
   * @param phrase
   *   phrase
   * @param path
   *   path
   * @param dictionary
   *   dictionary
   * @param word_count
   *   word_count
   */
  def mnemonicDeriveSignKeys(
    phrase: String,
    path: Option[String],
    dictionary: Option[Long],
    word_count: Option[Long]
  ): Either[Throwable, KeyPair] =
    ctx.execSync[ParamsOfMnemonicDeriveSignKeys, KeyPair](
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
    ctx.execSync[ParamsOfMnemonicFromEntropy, ResultOfMnemonicFromEntropy](
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
    ctx.execSync[ParamsOfMnemonicFromRandom, ResultOfMnemonicFromRandom](
      "crypto.mnemonic_from_random",
      ParamsOfMnemonicFromRandom(dictionary, word_count)
    )

  /**
   * Validates a mnemonic phrase The phrase supplied will be checked for word length and validated according to the
   * checksum specified in BIP0039.
   * @param phrase
   *   phrase
   * @param dictionary
   *   dictionary
   * @param word_count
   *   word_count
   */
  def mnemonicVerify(
    phrase: String,
    dictionary: Option[Long],
    word_count: Option[Long]
  ): Either[Throwable, ResultOfMnemonicVerify] =
    ctx.execSync[ParamsOfMnemonicVerify, ResultOfMnemonicVerify](
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
      .execSync[ParamsOfMnemonicWords, ResultOfMnemonicWords](
        "crypto.mnemonic_words",
        ParamsOfMnemonicWords(dictionary)
      )

  /**
   * Modular exponentiation Performs modular exponentiation for big integers (`base`^`exponent` mod `modulus`). See
   * [https://en.wikipedia.org/wiki/Modular_exponentiation]
   * @param base
   *   base
   * @param exponent
   *   exponent
   * @param modulus
   *   modulus
   */
  def modularPower(
    base: String,
    exponent: String,
    modulus: String
  ): Either[Throwable, ResultOfModularPower] =
    ctx.execSync[ParamsOfModularPower, ResultOfModularPower](
      "crypto.modular_power",
      ParamsOfModularPower(base, exponent, modulus)
    )

  /**
   * Public key authenticated encryption
   *
   * Encrypt and authenticate a message using the senders secret key, the receivers public key, and a nonce.
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
    ctx.execSync[ParamsOfNaclBox, ResultOfNaclBox](
      "crypto.nacl_box",
      ParamsOfNaclBox(decrypted, nonce, their_public, secret)
    )

  def naclBoxKeypair: Either[Throwable, KeyPair] =
    ctx.execSync[Unit, KeyPair]("crypto.nacl_box_keypair", ())

  /**
   * Generates key pair from a secret key
   * @param secret
   *   Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclBoxKeypairFromSecretKey(secret: String): Either[Throwable, KeyPair] =
    ctx
      .execSync[ParamsOfNaclBoxKeyPairFromSecret, KeyPair](
        "crypto.nacl_box_keypair_from_secret_key",
        ParamsOfNaclBoxKeyPairFromSecret(secret)
      )

  /**
   * Decrypt and verify the cipher text using the receivers secret key, the senders public key, and the nonce.@param
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
    ctx.execSync[ParamsOfNaclBoxOpen, ResultOfNaclBoxOpen](
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
      .execSync[ParamsOfNaclSecretBox, ResultOfNaclBox](
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
      .execSync[ParamsOfNaclSecretBoxOpen, ResultOfNaclBoxOpen](
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
      .execSync[ParamsOfNaclSign, ResultOfNaclSign]("crypto.nacl_sign", ParamsOfNaclSign(unsigned, secret))

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
      .execSync[ParamsOfNaclSign, ResultOfNaclSignDetached](
        "crypto.nacl_sign_detached",
        ParamsOfNaclSign(unsigned, secret)
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
    ctx
      .execSync[ParamsOfNaclSignDetachedVerify, ResultOfNaclSignDetachedVerify](
        "crypto.nacl_sign_detached_verify",
        ParamsOfNaclSignDetachedVerify(unsigned, signature, public)
      )
  }

  /**
   * Generates a key pair for signing from the secret key **NOTE:** In the result the secret key is actually the
   * concatenation of secret and public keys (128 symbols hex string) by design of
   * [NaCL](http://nacl.cr.yp.to/sign.html). See also [the stackexchange
   * question](https://crypto.stackexchange.com/questions/54353/).
   *
   * @param secret
   *   Secret key - unprefixed 0-padded to 64 symbols hex string
   */
  def naclSignKeypairFromSecretKey(secret: String): Either[Throwable, KeyPair] =
    ctx
      .execSync[ParamsOfNaclSignKeyPairFromSecret, KeyPair](
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
      .execSync[ParamsOfNaclSignOpen, ResultOfNaclSignOpen](
        "crypto.nacl_sign_open",
        ParamsOfNaclSignOpen(signed, public)
      )

  /**
   * Register an application implemented encryption box.
   * @param app_object
   *   app_object
   */
  def registerEncryptionBox(
    app_object: AppObject[
      ParamsOfAppEncryptionBoxADT.ParamsOfAppEncryptionBox,
      ResultOfAppEncryptionBoxADT.ResultOfAppEncryptionBox
    ]
  ): Future[Either[Throwable, RegisteredEncryptionBox]] = {
    ctx
      .registerAppObject[
        RegisteredEncryptionBox,
        ParamsOfAppEncryptionBoxADT.ParamsOfAppEncryptionBox,
        ResultOfAppEncryptionBoxADT.ResultOfAppEncryptionBox
      ](
        "crypto.register_encryption_box",
        "",
        app_object
      )
  }

  /**
   * @param app_object
   *   app_object
   */
  def registerSigningBox(
    app_object: AppObject[
      ParamsOfAppSigningBoxADT.ParamsOfAppSigningBox,
      ResultOfAppSigningBoxADT.ResultOfAppSigningBox
    ]
  ): Future[Either[Throwable, RegisteredSigningBox]] = {
    ctx
      .registerAppObject[
        RegisteredSigningBox,
        ParamsOfAppSigningBoxADT.ParamsOfAppSigningBox,
        ResultOfAppSigningBoxADT.ResultOfAppSigningBox
      ](
        "crypto.register_signing_box",
        "",
        app_object
      )
  }

  /**
   * Removes Crypto Box. Clears all secret data.
   * @param handle
   *   handle
   */
  def removeCryptoBox(handle: CryptoBoxHandle): Future[Either[Throwable, Unit]] = {
    ctx.unregisterAppObject(handle.value.toInt, "crypto.remove_crypto_box")
  }

  /**
   * Removes encryption box from SDK
   * @param handle
   *   handle
   */
  def removeEncryptionBox(handle: EncryptionBoxHandle): Future[Either[Throwable, Unit]] = {
    ctx.unregisterAppObject(handle.value.toInt, "crypto.remove_encryption_box")
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
   *   - `p = 1`
   * @param password
   *   password
   * @param salt
   *   salt
   * @param log_n
   *   log_n
   * @param r
   *   r
   * @param p
   *   p
   * @param dk_len
   *   dk_len
   */
  def scrypt(
    password: String,
    salt: String,
    log_n: Long,
    r: Long,
    p: Long,
    dk_len: Long
  ): Either[Throwable, ResultOfScrypt] =
    ctx.execSync[ParamsOfScrypt, ResultOfScrypt](
      "crypto.scrypt",
      ParamsOfScrypt(password, salt, log_n, r, p, dk_len)
    )

  /**
   * Calculates SHA256 hash of the specified data.
   * @param data
   *   Input data for hash calculation. Encoded with `base64`.
   */
  def sha256(data: String): Either[Throwable, ResultOfHash] =
    ctx.execSync[ParamsOfHash, ResultOfHash]("crypto.sha256", ParamsOfHash(data))

  /**
   * Calculates SHA512 hash of the specified data.
   * @param data
   *   Input data for hash calculation. Encoded with `base64`.
   */
  def sha512(data: String): Either[Throwable, ResultOfHash] =
    ctx.execSync[ParamsOfHash, ResultOfHash]("crypto.sha512", ParamsOfHash(data))

  /**
   * Signs a data using the provided keys.
   * @param unsigned
   *   Data that must be signed encoded in `base64`.
   * @param keys
   *   Sign keys.
   */
  def sign(unsigned: String, keys: KeyPair): Either[Throwable, ResultOfSign] =
    ctx
      .execSync[ParamsOfSign, ResultOfSign]("crypto.sign", ParamsOfSign(unsigned, keys))

  /**
   * @param handle
   */
  def signingBoxGetPublicKey(
    handle: SigningBoxHandle
  ): Either[Throwable, ResultOfSigningBoxGetPublicKey] = {
    ctx.execSync[RegisteredSigningBox, ResultOfSigningBoxGetPublicKey](
      "crypto.signing_box_get_public_key",
      RegisteredSigningBox(handle)
    )
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
    ctx.execSync[ParamsOfSigningBoxSign, ResultOfSigningBoxSign](
      "crypto.signing_box_sign",
      ParamsOfSigningBoxSign(signing_box, unsigned)
    )
  }

  /**
   * Calculates CRC16 using TON algorithm.
   * @param data
   *   Input data for CRC calculation. Encoded with `base64`.
   */
  def tonCrc16(data: String): Either[Throwable, ResultOfTonCrc16] =
    ctx
      .execSync[ParamsOfTonCrc16, ResultOfTonCrc16]("crypto.ton_crc16", ParamsOfTonCrc16(data))

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
      .execSync[ParamsOfVerifySignature, ResultOfVerifySignature](
        "crypto.verify_signature",
        ParamsOfVerifySignature(signed, public)
      )

}
