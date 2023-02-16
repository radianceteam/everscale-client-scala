package com.radiance.jvm.crypto

import com.radiance.jvm.Value
import com.radiance.jvm.crypto.EncryptionAlgorithmADT.EncryptionAlgorithm
import io.circe._
import io.circe.derivation._
import io.circe.generic.extras

case class AesInfo(mode: CipherModeEnum.CipherMode, iv: Option[String])

case class AesParamsEB(mode: CipherModeEnum.CipherMode, key: String, iv: Option[String])

object AesParamsEB {
  implicit val encoder: Encoder[AesParamsEB] =
    deriveEncoder[AesParamsEB]
}

object BoxEncryptionAlgorithmADT {
  sealed trait BoxEncryptionAlgorithm
  case class ChaCha20(value: ChaCha20ParamsCB) extends BoxEncryptionAlgorithm
  case class NaclBox(value: NaclBoxParamsCB) extends BoxEncryptionAlgorithm
  case class NaclSecretBox(value: NaclSecretBoxParamsCB) extends BoxEncryptionAlgorithm

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[BoxEncryptionAlgorithm] =
    extras.semiauto.deriveConfiguredEncoder[BoxEncryptionAlgorithm]
}

case class ChaCha20ParamsCB(nonce: String)

object ChaCha20ParamsCB {
  implicit val encoder: Encoder[ChaCha20ParamsCB] =
    deriveEncoder[ChaCha20ParamsCB]
}

case class ChaCha20ParamsEB(key: String, nonce: String)

object ChaCha20ParamsEB {
  implicit val encoder: Encoder[ChaCha20ParamsEB] =
    deriveEncoder[ChaCha20ParamsEB]
}

object CipherModeEnum {
  sealed trait CipherMode
  case object CBC extends CipherMode
  case object CFB extends CipherMode
  case object CTR extends CipherMode
  case object ECB extends CipherMode
  case object OFB extends CipherMode

  implicit val encoder: Encoder[CipherMode] =
    extras.semiauto.deriveEnumerationEncoder[CipherMode]
}

case class CryptoBoxHandle(value: BigInt) extends AnyVal

object CryptoBoxHandle {
  implicit val decoder: Decoder[CryptoBoxHandle] =
    Decoder.instance(c => c.value.as[BigInt].map(CryptoBoxHandle(_)))
  implicit val encoder: Encoder[CryptoBoxHandle] = Encoder.instance(a => Json.fromBigInt(a.value))
}

object CryptoBoxSecretADT {

  /**
   * Crypto Box Secret.
   */
  sealed trait CryptoBoxSecret

  /**
   * Crypto Box Secret.
   */
  case class EncryptedSecret(encrypted_secret: String) extends CryptoBoxSecret

  /**
   * Crypto Box Secret.
   */
  case class PredefinedSeedPhrase(
    phrase: String,
    dictionary: MnemonicDictionaryEnum.MnemonicDictionary,
    wordcount: Long
  ) extends CryptoBoxSecret

  /**
   * Crypto Box Secret.
   */
  case class RandomSeedPhrase(dictionary: MnemonicDictionaryEnum.MnemonicDictionary, wordcount: Long)
      extends CryptoBoxSecret

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[CryptoBoxSecret] =
    extras.semiauto.deriveConfiguredEncoder[CryptoBoxSecret]
}

object CryptoErrorCodeEnum {

  sealed trait CryptoErrorCode {
    val code: String
  }

  case object Bip32InvalidDerivePath extends CryptoErrorCode {
    override val code: String = "116"
  }

  case object Bip32InvalidKey extends CryptoErrorCode {
    override val code: String = "115"
  }

  case object Bip39InvalidDictionary extends CryptoErrorCode {
    override val code: String = "117"
  }

  case object Bip39InvalidEntropy extends CryptoErrorCode {
    override val code: String = "113"
  }

  case object Bip39InvalidPhrase extends CryptoErrorCode {
    override val code: String = "114"
  }

  case object Bip39InvalidWordCount extends CryptoErrorCode {
    override val code: String = "118"
  }

  case object CannotCreateCipher extends CryptoErrorCode {
    override val code: String = "126"
  }
  case object CryptoBoxNotRegistered extends CryptoErrorCode {
    override val code: String = "130"
  }

  case object CryptoBoxSecretDeserializationError extends CryptoErrorCode {
    override val code: String = "133"
  }

  case object CryptoBoxSecretSerializationError extends CryptoErrorCode {
    override val code: String = "132"
  }

  case object DecryptDataError extends CryptoErrorCode {
    override val code: String = "128"
  }

  case object EncryptDataError extends CryptoErrorCode {
    override val code: String = "127"
  }

  case object EncryptionBoxNotRegistered extends CryptoErrorCode {
    override val code: String = "123"
  }

  case object InvalidBigInt extends CryptoErrorCode {
    override val code: String = "107"
  }

  case object InvalidCryptoBoxType extends CryptoErrorCode {
    override val code: String = "131"
  }

  case object InvalidFactorizeChallenge extends CryptoErrorCode {
    override val code: String = "106"
  }

  case object InvalidIvSize extends CryptoErrorCode {
    override val code: String = "124"
  }

  case object InvalidKey extends CryptoErrorCode {
    override val code: String = "102"
  }

  case object InvalidKeySize extends CryptoErrorCode {
    override val code: String = "109"
  }

  case object InvalidNonceSize extends CryptoErrorCode {
    override val code: String = "134"
  }

  case object InvalidPublicKey extends CryptoErrorCode {
    override val code: String = "100"
  }

  case object InvalidSecretKey extends CryptoErrorCode {
    override val code: String = "101"
  }

  case object InvalidSignature extends CryptoErrorCode {
    override val code: String = "122"
  }

  case object IvRequired extends CryptoErrorCode {
    override val code: String = "129"
  }

  case object MnemonicFromEntropyFailed extends CryptoErrorCode {
    override val code: String = "120"
  }

  case object MnemonicGenerationFailed extends CryptoErrorCode {
    override val code: String = "119"
  }

  case object NaclBoxFailed extends CryptoErrorCode {
    override val code: String = "111"
  }

  case object NaclSecretBoxFailed extends CryptoErrorCode {
    override val code: String = "110"
  }

  case object NaclSignFailed extends CryptoErrorCode {
    override val code: String = "112"
  }

  case object ScryptFailed extends CryptoErrorCode {
    override val code: String = "108"
  }

  case object SigningBoxNotRegistered extends CryptoErrorCode {
    override val code: String = "121"
  }

  case object UnsupportedCipherMode extends CryptoErrorCode {
    override val code: String = "125"
  }
}

object EncryptionAlgorithmADT {
  sealed trait EncryptionAlgorithm
  case class AES(value: AesParamsEB) extends EncryptionAlgorithm
  case class ChaCha20(value: ChaCha20ParamsEB) extends EncryptionAlgorithm
  case class NaclBox(value: NaclBoxParamsEB) extends EncryptionAlgorithm
  case class NaclSecretBox(value: NaclSecretBoxParamsEB) extends EncryptionAlgorithm

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[EncryptionAlgorithm] =
    extras.semiauto.deriveConfiguredEncoder[EncryptionAlgorithm]
}

case class EncryptionBoxHandle(value: BigInt) extends AnyVal

object EncryptionBoxHandle {
  implicit val decoder: Decoder[EncryptionBoxHandle] =
    Decoder.instance(c => c.value.as[BigInt].map(EncryptionBoxHandle(_)))
  implicit val encoder: Encoder[EncryptionBoxHandle] = Encoder.instance(a => Json.fromBigInt(a.value))
}

/**
 * Encryption box information.
 */
case class EncryptionBoxInfo(
  hdpath: Option[String],
  algorithm: Option[String],
  options: Option[Value],
  public: Option[Value]
)

object EncryptionBoxInfo {
  implicit val decoder: Decoder[EncryptionBoxInfo] =
    deriveDecoder[EncryptionBoxInfo]

  implicit val encoder: Encoder[EncryptionBoxInfo] =
    deriveEncoder[EncryptionBoxInfo]
}

case class KeyPair(public: String, secret: String)

object KeyPair {
  implicit val codec: Codec[KeyPair] = deriveCodec[KeyPair]
}

object MnemonicDictionaryEnum {
  sealed trait MnemonicDictionary {
    val code: String
  }

  /**
   * Chinese simplified BIP-39 dictionary
   */
  case object ChineseSimplified extends MnemonicDictionary {
    override val code: String = "2"
  }

  /**
   * Chinese traditional BIP-39 dictionary
   */
  case object ChineseTraditional extends MnemonicDictionary {
    override val code: String = "3"
  }

  /**
   * English BIP-39 dictionary
   */
  case object English extends MnemonicDictionary {
    override val code: String = "1"
  }

  /**
   * French BIP-39 dictionary
   */
  case object French extends MnemonicDictionary {
    override val code: String = "4"
  }

  /**
   * Italian BIP-39 dictionary
   */
  case object Italian extends MnemonicDictionary {
    override val code: String = "5"
  }

  /**
   * Japanese BIP-39 dictionary
   */
  case object Japanese extends MnemonicDictionary {
    override val code: String = "6"
  }

  /**
   * Korean BIP-39 dictionary
   */
  case object Korean extends MnemonicDictionary {
    override val code: String = "7"
  }

  /**
   * Spanish BIP-39 dictionary
   */
  case object Spanish extends MnemonicDictionary {
    override val code: String = "8"
  }

  /**
   * TON compatible dictionary
   */
  case object Ton extends MnemonicDictionary {
    override val code: String = "0"
  }

  implicit val codec: Codec[MnemonicDictionary] =
    extras.semiauto.deriveEnumerationCodec[MnemonicDictionary]
}

case class NaclBoxParamsCB(their_public: String, nonce: String)

object NaclBoxParamsCB {
  implicit val encoder: Encoder[NaclBoxParamsCB] =
    deriveEncoder[NaclBoxParamsCB]
}

case class NaclBoxParamsEB(their_public: String, secret: String, nonce: String)

object NaclBoxParamsEB {
  implicit val encoder: Encoder[NaclBoxParamsEB] =
    deriveEncoder[NaclBoxParamsEB]
}

case class NaclSecretBoxParamsCB(nonce: String)

object NaclSecretBoxParamsCB {
  implicit val encoder: Encoder[NaclSecretBoxParamsCB] =
    deriveEncoder[NaclSecretBoxParamsCB]
}

case class NaclSecretBoxParamsEB(key: String, nonce: String)

object NaclSecretBoxParamsEB {
  implicit val encoder: Encoder[NaclSecretBoxParamsEB] =
    deriveEncoder[NaclSecretBoxParamsEB]
}

object ParamsOfAppEncryptionBoxADT {

  /**
   * Interface for data encryption/decryption
   */
  sealed trait ParamsOfAppEncryptionBox

  /**
   * Interface for data encryption/decryption
   */
  case class Decrypt(data: String) extends ParamsOfAppEncryptionBox

  /**
   * Interface for data encryption/decryption
   */
  case class Encrypt(data: String) extends ParamsOfAppEncryptionBox

  /**
   * Get encryption box info
   */
  case object GetInfo extends ParamsOfAppEncryptionBox

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val decoder: Decoder[ParamsOfAppEncryptionBox] =
    extras.semiauto.deriveConfiguredDecoder[ParamsOfAppEncryptionBox]
}

object ParamsOfAppPasswordProviderADT {

  /**
   * Interface that provides a callback that returns an encrypted password, used for cryptobox secret encryption To
   * secure the password while passing it from application to the library, the library generates a temporary key pair,
   * passes the pubkey to the passwordProvider, decrypts the received password with private key, and deletes the key
   * pair right away.
   *
   * Application should generate a temporary nacl_box_keypair and encrypt the password with naclbox function using
   * nacl_box_keypair.secret and encryption_public_key keys + nonce = 24-byte prefix of encryption_public_key.
   */
  sealed trait ParamsOfAppPasswordProvider

  /**
   * Interface that provides a callback that returns an encrypted password, used for cryptobox secret encryption To
   * secure the password while passing it from application to the library, the library generates a temporary key pair,
   * passes the pubkey to the passwordProvider, decrypts the received password with private key, and deletes the key
   * pair right away.
   *
   * Application should generate a temporary nacl_box_keypair and encrypt the password with naclbox function using
   * nacl_box_keypair.secret and encryption_public_key keys + nonce = 24-byte prefix of encryption_public_key.
   */
  case class GetPassword(encryption_public_key: String) extends ParamsOfAppPasswordProvider

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val decoder: Decoder[ParamsOfAppPasswordProvider] =
    extras.semiauto.deriveConfiguredDecoder[ParamsOfAppPasswordProvider]
}

object ParamsOfAppSigningBoxADT {

  sealed trait ParamsOfAppSigningBox

  case object GetPublicKey extends ParamsOfAppSigningBox

  case class Sign(unsigned: String) extends ParamsOfAppSigningBox

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val decoder: Decoder[ParamsOfAppSigningBox] =
    extras.semiauto.deriveConfiguredDecoder[ParamsOfAppSigningBox]
}

case class ParamsOfChaCha20(data: String, key: String, nonce: String)

object ParamsOfChaCha20 {
  implicit val encoder: Encoder[ParamsOfChaCha20] =
    deriveEncoder[ParamsOfChaCha20]
}

case class ParamsOfConvertPublicKeyToTonSafeFormat(public_key: String)

object ParamsOfConvertPublicKeyToTonSafeFormat {
  implicit val encoder: Encoder[ParamsOfConvertPublicKeyToTonSafeFormat] =
    deriveEncoder[ParamsOfConvertPublicKeyToTonSafeFormat]
}

case class ParamsOfCreateCryptoBox(secret_encryption_salt: String, secret: CryptoBoxSecretADT.CryptoBoxSecret)

object ParamsOfCreateCryptoBox {
  implicit val encoder: Encoder[ParamsOfCreateCryptoBox] =
    deriveEncoder[ParamsOfCreateCryptoBox]
}

case class ParamsOfEncryptionBoxDecrypt(encryption_box: EncryptionBoxHandle, data: String)

object ParamsOfEncryptionBoxDecrypt {
  implicit val encoder: Encoder[ParamsOfEncryptionBoxDecrypt] =
    deriveEncoder[ParamsOfEncryptionBoxDecrypt]
}

case class ParamsOfCreateEncryptionBox(algorithm: EncryptionAlgorithm)

object ParamsOfCreateEncryptionBox {
  implicit val encoder: Encoder[ParamsOfCreateEncryptionBox] =
    deriveEncoder[ParamsOfCreateEncryptionBox]
}

case class ParamsOfEncryptionBoxEncrypt(encryption_box: EncryptionBoxHandle, data: String)

object ParamsOfEncryptionBoxEncrypt {
  implicit val encoder: Encoder[ParamsOfEncryptionBoxEncrypt] =
    deriveEncoder[ParamsOfEncryptionBoxEncrypt]
}

case class ParamsOfEncryptionBoxGetInfo(encryption_box: EncryptionBoxHandle)

object ParamsOfEncryptionBoxGetInfo {
  implicit val encoder: Encoder[ParamsOfEncryptionBoxGetInfo] =
    deriveEncoder[ParamsOfEncryptionBoxGetInfo]
}

case class ParamsOfFactorize(composite: String)

object ParamsOfFactorize {
  implicit val encoder: Encoder[ParamsOfFactorize] =
    deriveEncoder[ParamsOfFactorize]
}

case class ParamsOfGenerateRandomBytes(length: Long)

object ParamsOfGenerateRandomBytes {
  implicit val encoder: Encoder[ParamsOfGenerateRandomBytes] =
    deriveEncoder[ParamsOfGenerateRandomBytes]
}

case class ParamsOfGetEncryptionBoxFromCryptoBox(
  handle: Long,
  hdpath: Option[String],
  algorithm: BoxEncryptionAlgorithmADT.BoxEncryptionAlgorithm,
  secret_lifetime: Option[Long]
)

object ParamsOfGetEncryptionBoxFromCryptoBox {
  implicit val encoder: Encoder[ParamsOfGetEncryptionBoxFromCryptoBox] =
    deriveEncoder[ParamsOfGetEncryptionBoxFromCryptoBox]
}

case class ParamsOfGetSigningBoxFromCryptoBox(handle: Long, hdpath: Option[String], secret_lifetime: Option[Long])

object ParamsOfGetSigningBoxFromCryptoBox {
  implicit val encoder: Encoder[ParamsOfGetSigningBoxFromCryptoBox] =
    deriveEncoder[ParamsOfGetSigningBoxFromCryptoBox]
}

case class ParamsOfHash(data: String)

object ParamsOfHash {
  implicit val encoder: Encoder[ParamsOfHash] =
    deriveEncoder[ParamsOfHash]
}

case class ParamsOfHDKeyDeriveFromXPrv(
  xprv: String,
  child_index: Long,
  hardened: Boolean
)

object ParamsOfHDKeyDeriveFromXPrv {
  implicit val encoder: Encoder[ParamsOfHDKeyDeriveFromXPrv] =
    deriveEncoder[ParamsOfHDKeyDeriveFromXPrv]
}

case class ParamsOfHDKeyDeriveFromXPrvPath(xprv: String, path: String)

object ParamsOfHDKeyDeriveFromXPrvPath {
  implicit val encoder: Encoder[ParamsOfHDKeyDeriveFromXPrvPath] =
    deriveEncoder[ParamsOfHDKeyDeriveFromXPrvPath]
}

case class ParamsOfHDKeyPublicFromXPrv(xprv: String)

object ParamsOfHDKeyPublicFromXPrv {
  implicit val encoder: Encoder[ParamsOfHDKeyPublicFromXPrv] =
    deriveEncoder[ParamsOfHDKeyPublicFromXPrv]
}

case class ParamsOfHDKeySecretFromXPrv(xprv: String)

object ParamsOfHDKeySecretFromXPrv {
  implicit val encoder: Encoder[ParamsOfHDKeySecretFromXPrv] =
    deriveEncoder[ParamsOfHDKeySecretFromXPrv]
}

case class ParamsOfHDKeyXPrvFromMnemonic(
  phrase: String,
  dictionary: Option[MnemonicDictionaryEnum.MnemonicDictionary],
  word_count: Option[Long]
)

object ParamsOfHDKeyXPrvFromMnemonic {
  implicit val encoder: Encoder[ParamsOfHDKeyXPrvFromMnemonic] =
    deriveEncoder[ParamsOfHDKeyXPrvFromMnemonic]
}

case class ParamsOfMnemonicDeriveSignKeys(
  phrase: String,
  path: Option[String],
  dictionary: Option[MnemonicDictionaryEnum.MnemonicDictionary],
  word_count: Option[Long]
)

object ParamsOfMnemonicDeriveSignKeys {
  implicit val encoder: Encoder[ParamsOfMnemonicDeriveSignKeys] =
    deriveEncoder[ParamsOfMnemonicDeriveSignKeys]
}

case class ParamsOfMnemonicFromEntropy(
  entropy: String,
  dictionary: Option[MnemonicDictionaryEnum.MnemonicDictionary],
  word_count: Option[Long]
)

object ParamsOfMnemonicFromEntropy {
  implicit val encoder: Encoder[ParamsOfMnemonicFromEntropy] =
    deriveEncoder[ParamsOfMnemonicFromEntropy]
}

case class ParamsOfMnemonicFromRandom(
  dictionary: Option[MnemonicDictionaryEnum.MnemonicDictionary],
  word_count: Option[Long]
)

object ParamsOfMnemonicFromRandom {
  implicit val encoder: Encoder[ParamsOfMnemonicFromRandom] =
    deriveEncoder[ParamsOfMnemonicFromRandom]
}

case class ParamsOfMnemonicVerify(
  phrase: String,
  dictionary: Option[MnemonicDictionaryEnum.MnemonicDictionary],
  word_count: Option[Long]
)

object ParamsOfMnemonicVerify {
  implicit val encoder: Encoder[ParamsOfMnemonicVerify] =
    deriveEncoder[ParamsOfMnemonicVerify]
}

case class ParamsOfMnemonicWords(dictionary: Option[MnemonicDictionaryEnum.MnemonicDictionary])

object ParamsOfMnemonicWords {
  implicit val encoder: Encoder[ParamsOfMnemonicWords] =
    deriveEncoder[ParamsOfMnemonicWords]
}

case class ParamsOfModularPower(
  base: String,
  exponent: String,
  modulus: String
)

object ParamsOfModularPower {
  implicit val encoder: Encoder[ParamsOfModularPower] =
    deriveEncoder[ParamsOfModularPower]
}

case class ParamsOfNaclBox(
  decrypted: String,
  nonce: String,
  their_public: String,
  secret: String
)

object ParamsOfNaclBox {
  implicit val encoder: Encoder[ParamsOfNaclBox] =
    deriveEncoder[ParamsOfNaclBox]
}

case class ParamsOfNaclBoxKeyPairFromSecret(secret: String)

object ParamsOfNaclBoxKeyPairFromSecret {
  implicit val encoder: Encoder[ParamsOfNaclBoxKeyPairFromSecret] =
    deriveEncoder[ParamsOfNaclBoxKeyPairFromSecret]
}

case class ParamsOfNaclBoxOpen(
  encrypted: String,
  nonce: String,
  their_public: String,
  secret: String
)

object ParamsOfNaclBoxOpen {
  implicit val encoder: Encoder[ParamsOfNaclBoxOpen] =
    deriveEncoder[ParamsOfNaclBoxOpen]
}

case class ParamsOfNaclSecretBox(
  decrypted: String,
  nonce: String,
  key: String
)

object ParamsOfNaclSecretBox {
  implicit val encoder: Encoder[ParamsOfNaclSecretBox] =
    deriveEncoder[ParamsOfNaclSecretBox]
}

case class ParamsOfNaclSecretBoxOpen(
  encrypted: String,
  nonce: String,
  key: String
)

object ParamsOfNaclSecretBoxOpen {
  implicit val encoder: Encoder[ParamsOfNaclSecretBoxOpen] =
    deriveEncoder[ParamsOfNaclSecretBoxOpen]
}

case class ParamsOfNaclSign(unsigned: String, secret: String)

object ParamsOfNaclSign {
  implicit val encoder: Encoder[ParamsOfNaclSign] =
    deriveEncoder[ParamsOfNaclSign]
}

case class ParamsOfNaclSignDetachedVerify(unsigned: String, signature: String, public: String)

object ParamsOfNaclSignDetachedVerify {
  implicit val encoder: Encoder[ParamsOfNaclSignDetachedVerify] =
    deriveEncoder[ParamsOfNaclSignDetachedVerify]
}

case class ParamsOfNaclSignKeyPairFromSecret(secret: String)

object ParamsOfNaclSignKeyPairFromSecret {
  implicit val encoder: Encoder[ParamsOfNaclSignKeyPairFromSecret] =
    deriveEncoder[ParamsOfNaclSignKeyPairFromSecret]
}

case class ParamsOfNaclSignOpen(signed: String, public: String)

object ParamsOfNaclSignOpen {
  implicit val encoder: Encoder[ParamsOfNaclSignOpen] =
    deriveEncoder[ParamsOfNaclSignOpen]
}

case class ParamsOfScrypt(
  password: String,
  salt: String,
  log_n: Long,
  r: Long,
  p: Long,
  dk_len: Long
)

object ParamsOfScrypt {
  implicit val encoder: Encoder[ParamsOfScrypt] =
    deriveEncoder[ParamsOfScrypt]
}

case class ParamsOfSign(unsigned: String, keys: KeyPair)

object ParamsOfSign {
  implicit val encoder: Encoder[ParamsOfSign] = deriveEncoder[ParamsOfSign]
}

case class ParamsOfSigningBoxSign(
  signing_box: SigningBoxHandle,
  unsigned: String
)

object ParamsOfSigningBoxSign {
  implicit val encoder: Encoder[ParamsOfSigningBoxSign] =
    deriveEncoder[ParamsOfSigningBoxSign]
}

case class ParamsOfTonCrc16(data: String)

object ParamsOfTonCrc16 {
  implicit val encoder: Encoder[ParamsOfTonCrc16] =
    deriveEncoder[ParamsOfTonCrc16]
}

case class ParamsOfVerifySignature(signed: String, public: String)

object ParamsOfVerifySignature {
  implicit val encoder: Encoder[ParamsOfVerifySignature] =
    deriveEncoder[ParamsOfVerifySignature]
}

case class RegisteredCryptoBox(handle: CryptoBoxHandle)

object RegisteredCryptoBox {
  implicit val codec: Codec[RegisteredCryptoBox] =
    deriveCodec[RegisteredCryptoBox]
}

case class RegisteredEncryptionBox(handle: EncryptionBoxHandle)

object RegisteredEncryptionBox {
  implicit val codec: Codec[RegisteredEncryptionBox] =
    deriveCodec[RegisteredEncryptionBox]
}

case class RegisteredSigningBox(handle: SigningBoxHandle)

object RegisteredSigningBox {
  implicit val codec: Codec[RegisteredSigningBox] =
    deriveCodec[RegisteredSigningBox]
}

object ResultOfAppEncryptionBoxADT {

  /**
   * Returning values from signing box callbacks.
   */
  sealed trait ResultOfAppEncryptionBox

  /**
   * Returning values from signing box callbacks.
   */
  case class Decrypt(data: String) extends ResultOfAppEncryptionBox

  /**
   * Returning values from signing box callbacks.
   */
  case class Encrypt(data: String) extends ResultOfAppEncryptionBox

  /**
   * Returning values from signing box callbacks.
   */
  case class GetInfo(info: EncryptionBoxInfo) extends ResultOfAppEncryptionBox

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[ResultOfAppEncryptionBox] =
    extras.semiauto.deriveConfiguredEncoder[ResultOfAppEncryptionBox]
}

object ResultOfAppPasswordProviderADT {
  sealed trait ResultOfAppPasswordProvider
  case class GetPassword(encrypted_password: String, app_encryption_pubkey: String) extends ResultOfAppPasswordProvider

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[ResultOfAppPasswordProvider] =
    extras.semiauto.deriveConfiguredEncoder[ResultOfAppPasswordProvider]
}

object ResultOfAppSigningBoxADT {

  sealed trait ResultOfAppSigningBox

  case class GetPublicKey(public_key: String) extends ResultOfAppSigningBox

  case class Sign(signature: String) extends ResultOfAppSigningBox

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[ResultOfAppSigningBox] =
    extras.semiauto.deriveConfiguredEncoder[ResultOfAppSigningBox]

}

case class ResultOfChaCha20(data: String)

object ResultOfChaCha20 {
  implicit val ResultOfChaCha20Decoder: Decoder[ResultOfChaCha20] =
    deriveDecoder[ResultOfChaCha20]
}

case class ResultOfConvertPublicKeyToTonSafeFormat(ton_public_key: String)

object ResultOfConvertPublicKeyToTonSafeFormat {
  implicit val decoder: Decoder[ResultOfConvertPublicKeyToTonSafeFormat] =
    deriveDecoder[ResultOfConvertPublicKeyToTonSafeFormat]
}

case class ResultOfEncryptionBoxDecrypt(data: String)

object ResultOfEncryptionBoxDecrypt {
  implicit val decoder: Decoder[ResultOfEncryptionBoxDecrypt] =
    deriveDecoder[ResultOfEncryptionBoxDecrypt]
}

case class ResultOfEncryptionBoxEncrypt(data: String)

object ResultOfEncryptionBoxEncrypt {
  implicit val decoder: Decoder[ResultOfEncryptionBoxEncrypt] =
    deriveDecoder[ResultOfEncryptionBoxEncrypt]
}

case class ResultOfEncryptionBoxGetInfo(info: EncryptionBoxInfo)

object ResultOfEncryptionBoxGetInfo {
  implicit val decoder: Decoder[ResultOfEncryptionBoxGetInfo] =
    deriveDecoder[ResultOfEncryptionBoxGetInfo]
}

case class ResultOfFactorize(factors: List[String])

object ResultOfFactorize {
  implicit val decoder: Decoder[ResultOfFactorize] =
    deriveDecoder[ResultOfFactorize]
}

case class ResultOfGenerateRandomBytes(bytes: String)

object ResultOfGenerateRandomBytes {
  implicit val decoder: Decoder[ResultOfGenerateRandomBytes] =
    deriveDecoder[ResultOfGenerateRandomBytes]
}

case class ResultOfGetCryptoBoxInfo(encrypted_secret: String)

object ResultOfGetCryptoBoxInfo {
  implicit val decoder: Decoder[ResultOfGetCryptoBoxInfo] =
    deriveDecoder[ResultOfGetCryptoBoxInfo]
}

case class ResultOfGetCryptoBoxSeedPhrase(
  phrase: String,
  dictionary: MnemonicDictionaryEnum.MnemonicDictionary,
  wordcount: Long
)

object ResultOfGetCryptoBoxSeedPhrase {
  implicit val decoder: Decoder[ResultOfGetCryptoBoxSeedPhrase] =
    deriveDecoder[ResultOfGetCryptoBoxSeedPhrase]
}

case class ResultOfHash(hash: String)

object ResultOfHash {
  implicit val decoder: Decoder[ResultOfHash] = deriveDecoder[ResultOfHash]
}

case class ResultOfHDKeyDeriveFromXPrv(xprv: String)

object ResultOfHDKeyDeriveFromXPrv {
  implicit val decoder: Decoder[ResultOfHDKeyDeriveFromXPrv] =
    deriveDecoder[ResultOfHDKeyDeriveFromXPrv]
}

case class ResultOfHDKeyDeriveFromXPrvPath(xprv: String)

object ResultOfHDKeyDeriveFromXPrvPath {
  implicit val decoder: Decoder[ResultOfHDKeyDeriveFromXPrvPath] =
    deriveDecoder[ResultOfHDKeyDeriveFromXPrvPath]
}

case class ResultOfHDKeyPublicFromXPrv(public: String)

object ResultOfHDKeyPublicFromXPrv {
  implicit val decoder: Decoder[ResultOfHDKeyPublicFromXPrv] =
    deriveDecoder[ResultOfHDKeyPublicFromXPrv]
}

case class ResultOfHDKeySecretFromXPrv(secret: String)

object ResultOfHDKeySecretFromXPrv {
  implicit val decoder: Decoder[ResultOfHDKeySecretFromXPrv] =
    deriveDecoder[ResultOfHDKeySecretFromXPrv]
}

case class ResultOfHDKeyXPrvFromMnemonic(xprv: String)

object ResultOfHDKeyXPrvFromMnemonic {
  implicit val decoder: Decoder[ResultOfHDKeyXPrvFromMnemonic] =
    deriveDecoder[ResultOfHDKeyXPrvFromMnemonic]
}

case class ResultOfMnemonicFromEntropy(phrase: String)

object ResultOfMnemonicFromEntropy {
  implicit val decoder: Decoder[ResultOfMnemonicFromEntropy] =
    deriveDecoder[ResultOfMnemonicFromEntropy]
}

case class ResultOfMnemonicFromRandom(phrase: String)

object ResultOfMnemonicFromRandom {
  implicit val decoder: Decoder[ResultOfMnemonicFromRandom] =
    deriveDecoder[ResultOfMnemonicFromRandom]
}

case class ResultOfMnemonicVerify(valid: Boolean)

object ResultOfMnemonicVerify {
  implicit val decoder: Decoder[ResultOfMnemonicVerify] =
    deriveDecoder[ResultOfMnemonicVerify]
}

case class ResultOfMnemonicWords(words: String)

object ResultOfMnemonicWords {
  implicit val decoder: Decoder[ResultOfMnemonicWords] =
    deriveDecoder[ResultOfMnemonicWords]
}

case class ResultOfModularPower(modular_power: String)

object ResultOfModularPower {
  implicit val decoder: Decoder[ResultOfModularPower] =
    deriveDecoder[ResultOfModularPower]
}

case class ResultOfNaclBox(encrypted: String)

object ResultOfNaclBox {
  implicit val decoder: Decoder[ResultOfNaclBox] =
    deriveDecoder[ResultOfNaclBox]
}

case class ResultOfNaclBoxOpen(decrypted: String)

object ResultOfNaclBoxOpen {
  implicit val decoder: Decoder[ResultOfNaclBoxOpen] =
    deriveDecoder[ResultOfNaclBoxOpen]
}

case class ResultOfNaclSign(signed: String)

object ResultOfNaclSign {
  implicit val decoder: Decoder[ResultOfNaclSign] =
    deriveDecoder[ResultOfNaclSign]
}

case class ResultOfNaclSignDetached(signature: String)

object ResultOfNaclSignDetached {
  implicit val decoder: Decoder[ResultOfNaclSignDetached] =
    deriveDecoder[ResultOfNaclSignDetached]
}

case class ResultOfNaclSignDetachedVerify(succeeded: Boolean)

object ResultOfNaclSignDetachedVerify {
  implicit val decoder: Decoder[ResultOfNaclSignDetachedVerify] =
    deriveDecoder[ResultOfNaclSignDetachedVerify]
}

case class ResultOfNaclSignOpen(unsigned: String)

object ResultOfNaclSignOpen {
  implicit val decoder: Decoder[ResultOfNaclSignOpen] =
    deriveDecoder[ResultOfNaclSignOpen]
}

case class ResultOfScrypt(key: String)

object ResultOfScrypt {
  implicit val decoder: Decoder[ResultOfScrypt] =
    deriveDecoder[ResultOfScrypt]
}

case class ResultOfSign(signed: String, signature: String)

object ResultOfSign {
  implicit val decoder: Decoder[ResultOfSign] = deriveDecoder[ResultOfSign]
}

case class ResultOfSigningBoxGetPublicKey(pubkey: String)

object ResultOfSigningBoxGetPublicKey {
  implicit val decoder: Decoder[ResultOfSigningBoxGetPublicKey] =
    deriveDecoder[ResultOfSigningBoxGetPublicKey]
}

case class ResultOfSigningBoxSign(signature: String)

object ResultOfSigningBoxSign {
  implicit val decoder: Decoder[ResultOfSigningBoxSign] =
    deriveDecoder[ResultOfSigningBoxSign]
}

case class ResultOfTonCrc16(crc: Long)
object ResultOfTonCrc16 {
  implicit val decoder: Decoder[ResultOfTonCrc16] =
    deriveDecoder[ResultOfTonCrc16]
}

case class ResultOfVerifySignature(unsigned: String)

object ResultOfVerifySignature {
  implicit val decoder: Decoder[ResultOfVerifySignature] =
    deriveDecoder[ResultOfVerifySignature]
}

case class SigningBoxHandle(value: BigInt) extends AnyVal

object SigningBoxHandle {
  implicit val decoder: Decoder[SigningBoxHandle] = Decoder.instance(c => c.value.as[BigInt].map(SigningBoxHandle(_)))
  implicit val encoder: Encoder[SigningBoxHandle] = Encoder.instance(a => Json.fromBigInt(a.value))
}
