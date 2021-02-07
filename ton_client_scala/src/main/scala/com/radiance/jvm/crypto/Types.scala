package com.radiance.jvm.crypto

import io.circe._
import io.circe.derivation._
import io.circe.Json._
import io.circe.syntax._
import cats.implicits._
import com.radiance.jvm.Utils._
import io.circe.generic.extras

sealed trait CryptoErrorCode {
  val code: String
}

object CryptoErrorCode {

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

  case object InvalidBigInt extends CryptoErrorCode {
    override val code: String = "107"
  }

  case object InvalidFactorizeChallenge extends CryptoErrorCode {
    override val code: String = "106"
  }

  case object InvalidKey extends CryptoErrorCode {
    override val code: String = "102"
  }

  case object InvalidKeySize extends CryptoErrorCode {
    override val code: String = "109"
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

}

case class KeyPair(public: String, secret: String)

object KeyPair {
  implicit val codec: Codec[KeyPair] = deriveCodec[KeyPair]
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
  dictionary: Option[Long],
  word_count: Option[Long]
)

object ParamsOfHDKeyXPrvFromMnemonic {
  implicit val encoder: Encoder[ParamsOfHDKeyXPrvFromMnemonic] =
    deriveEncoder[ParamsOfHDKeyXPrvFromMnemonic]
}

case class ParamsOfMnemonicDeriveSignKeys(
  phrase: String,
  path: Option[String],
  dictionary: Option[Long],
  word_count: Option[Long]
)

object ParamsOfMnemonicDeriveSignKeys {
  implicit val encoder: Encoder[ParamsOfMnemonicDeriveSignKeys] =
    deriveEncoder[ParamsOfMnemonicDeriveSignKeys]
}

case class ParamsOfMnemonicFromEntropy(
  entropy: String,
  dictionary: Option[Long],
  word_count: Option[Long]
)

object ParamsOfMnemonicFromEntropy {
  implicit val encoder: Encoder[ParamsOfMnemonicFromEntropy] =
    deriveEncoder[ParamsOfMnemonicFromEntropy]
}

case class ParamsOfMnemonicFromRandom(
  dictionary: Option[Long],
  word_count: Option[Long]
)

object ParamsOfMnemonicFromRandom {
  implicit val encoder: Encoder[ParamsOfMnemonicFromRandom] =
    deriveEncoder[ParamsOfMnemonicFromRandom]
}

case class ParamsOfMnemonicVerify(
  phrase: String,
  dictionary: Option[Long],
  word_count: Option[Long]
)

object ParamsOfMnemonicVerify {
  implicit val encoder: Encoder[ParamsOfMnemonicVerify] =
    deriveEncoder[ParamsOfMnemonicVerify]
}

case class ParamsOfMnemonicWords(dictionary: Option[Long])

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

case class RegisteredSigningBox(handle: SigningBoxHandle)

object RegisteredSigningBox {
  implicit val codec: Codec[RegisteredSigningBox] =
    deriveCodec[RegisteredSigningBox]
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

case class SigningBoxHandle(value: BigInt)

object SigningBoxHandle {
  // TODO unusual behavior
  implicit val decoder: Decoder[SigningBoxHandle] =
    (c: HCursor) => c.value.as[BigInt].map(SigningBoxHandle(_))

  implicit val encoder: Encoder[SigningBoxHandle] = (a: SigningBoxHandle) => fromBigInt(a.value)

}
