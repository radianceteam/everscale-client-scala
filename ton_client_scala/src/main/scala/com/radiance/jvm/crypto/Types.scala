package com.radiance.jvm.crypto

import com.radiance.jvm.Utils._
import com.radiance.jvm._
import io.circe._
import io.circe.derivation._

case class KeyPair(public: String, secret: String) extends Bind {
  override type Out = RegisteredSigningBox
  override val decoder: Decoder[RegisteredSigningBox] = implicitly[Decoder[RegisteredSigningBox]]
}

object KeyPair {
  implicit val codec: Codec[KeyPair] = deriveCodec[KeyPair]
}

sealed trait ParamsOfAppSigningBox extends Bind {
  override type Out = RegisteredSigningBox
  override val decoder: Decoder[RegisteredSigningBox] =
    implicitly[Decoder[RegisteredSigningBox]]
}

object ParamsOfAppSigningBox {
  import io.circe.Json._
  import io.circe.syntax._

  case object GetPublicKey extends ParamsOfAppSigningBox

  case class Sign(unsigned: String) extends ParamsOfAppSigningBox

  object Sign {
    implicit val encoder: Encoder[Sign] = deriveEncoder[Sign]
  }

  implicit val encoder: Encoder[ParamsOfAppSigningBox] = {
    case GetPublicKey => fromFields(Seq("type" -> fromString("GetPublicKey")))
    case a: Sign => a.asJson.deepMerge(generateType(a))
  }
}

case class ParamsOfChaCha20(data: String, key: String, nonce: String)
    extends Bind {
  override type Out = ResultOfChaCha20
  override val decoder: Decoder[ResultOfChaCha20] =
    implicitly[Decoder[ResultOfChaCha20]]
}

object ParamsOfChaCha20 {
  implicit val encoder: Encoder[ParamsOfChaCha20] =
    deriveEncoder[ParamsOfChaCha20]
}

case class ParamsOfConvertPublicKeyToTonSafeFormat(public_key: String)
    extends Bind {
  override type Out = ResultOfConvertPublicKeyToTonSafeFormat
  override val decoder: Decoder[ResultOfConvertPublicKeyToTonSafeFormat] =
    implicitly[Decoder[ResultOfConvertPublicKeyToTonSafeFormat]]
}

object ParamsOfConvertPublicKeyToTonSafeFormat {
  implicit val encoder: Encoder[ParamsOfConvertPublicKeyToTonSafeFormat] =
    deriveEncoder[ParamsOfConvertPublicKeyToTonSafeFormat]
}

case class ParamsOfFactorize(composite: String) extends Bind {
  override type Out = ResultOfFactorize
  override val decoder: Decoder[ResultOfFactorize] =
    implicitly[Decoder[ResultOfFactorize]]
}

object ParamsOfFactorize {
  implicit val encoder: Encoder[ParamsOfFactorize] =
    deriveEncoder[ParamsOfFactorize]
}

case class ParamsOfGenerateRandomBytes(length: Long) extends Bind {
  override type Out = ResultOfGenerateRandomBytes
  override val decoder: Decoder[ResultOfGenerateRandomBytes] =
    implicitly[Decoder[ResultOfGenerateRandomBytes]]
}

object ParamsOfGenerateRandomBytes {
  implicit val encoder: Encoder[ParamsOfGenerateRandomBytes] =
    deriveEncoder[ParamsOfGenerateRandomBytes]
}

// TODO was renamed
case class ParamsOfHash256(data: String) extends Bind {
  override type Out = ResultOfHash
  override val decoder: Decoder[ResultOfHash] =
    implicitly[Decoder[ResultOfHash]]
}

object ParamsOfHash256 {
  implicit val encoder: Encoder[ParamsOfHash256] =
    deriveEncoder[ParamsOfHash256]
}

// TODO was renamed
case class ParamsOfHash512(data: String) extends Bind {
  override type Out = ResultOfHash
  override val decoder: Decoder[ResultOfHash] =
    implicitly[Decoder[ResultOfHash]]
}

object ParamsOfHash512 {
  implicit val encoder: Encoder[ParamsOfHash512] =
    deriveEncoder[ParamsOfHash512]
}

case class ParamsOfHDKeyDeriveFromXPrv(
    xprv: String,
    child_index: Long,
    hardened: Boolean
) extends Bind {
  override type Out = ResultOfHDKeyDeriveFromXPrv
  override val decoder: Decoder[ResultOfHDKeyDeriveFromXPrv] =
    implicitly[Decoder[ResultOfHDKeyDeriveFromXPrv]]
}

object ParamsOfHDKeyDeriveFromXPrv {
  implicit val encoder: Encoder[ParamsOfHDKeyDeriveFromXPrv] =
    deriveEncoder[ParamsOfHDKeyDeriveFromXPrv]
}

case class ParamsOfHDKeyDeriveFromXPrvPath(xprv: String, path: String)
    extends Bind {
  override type Out = ResultOfHDKeyDeriveFromXPrvPath
  override val decoder: Decoder[ResultOfHDKeyDeriveFromXPrvPath] =
    implicitly[Decoder[ResultOfHDKeyDeriveFromXPrvPath]]
}

object ParamsOfHDKeyDeriveFromXPrvPath {
  implicit val encoder: Encoder[ParamsOfHDKeyDeriveFromXPrvPath] =
    deriveEncoder[ParamsOfHDKeyDeriveFromXPrvPath]
}

case class ParamsOfHDKeyPublicFromXPrv(xprv: String) extends Bind {
  override type Out = ResultOfHDKeyPublicFromXPrv
  override val decoder: Decoder[ResultOfHDKeyPublicFromXPrv] =
    implicitly[Decoder[ResultOfHDKeyPublicFromXPrv]]
}

object ParamsOfHDKeyPublicFromXPrv {
  implicit val encoder: Encoder[ParamsOfHDKeyPublicFromXPrv] =
    deriveEncoder[ParamsOfHDKeyPublicFromXPrv]
}

case class ParamsOfHDKeySecretFromXPrv(xprv: String) extends Bind {
  override type Out = ResultOfHDKeySecretFromXPrv
  override val decoder: Decoder[ResultOfHDKeySecretFromXPrv] =
    implicitly[Decoder[ResultOfHDKeySecretFromXPrv]]
}

object ParamsOfHDKeySecretFromXPrv {
  implicit val encoder: Encoder[ParamsOfHDKeySecretFromXPrv] =
    deriveEncoder[ParamsOfHDKeySecretFromXPrv]
}

case class ParamsOfHDKeyXPrvFromMnemonic(
    phrase: String,
    dictionary: Option[Long],
    word_count: Option[Long]
) extends Bind {
  override type Out = ResultOfHDKeyXPrvFromMnemonic
  override val decoder: Decoder[ResultOfHDKeyXPrvFromMnemonic] =
    implicitly[Decoder[ResultOfHDKeyXPrvFromMnemonic]]
}

object ParamsOfHDKeyXPrvFromMnemonic {
  implicit val encoder: Encoder[ParamsOfHDKeyXPrvFromMnemonic] =
    deriveEncoder[ParamsOfHDKeyXPrvFromMnemonic]
}

case class ParamsOfMnemonicDeriveSignKeys(
    phrase: String,
    path: Option[String],
    dictionary: Option[Long],
    word_count: Option[Long]
) extends Bind {
  override type Out = KeyPair
  override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
}

object ParamsOfMnemonicDeriveSignKeys {
  implicit val encoder: Encoder[ParamsOfMnemonicDeriveSignKeys] =
    deriveEncoder[ParamsOfMnemonicDeriveSignKeys]
}

case class ParamsOfMnemonicFromEntropy(
    entropy: String,
    dictionary: Option[Long],
    word_count: Option[Long]
) extends Bind {
  override type Out = ResultOfMnemonicFromEntropy
  override val decoder: Decoder[ResultOfMnemonicFromEntropy] =
    implicitly[Decoder[ResultOfMnemonicFromEntropy]]
}

object ParamsOfMnemonicFromEntropy {
  implicit val encoder: Encoder[ParamsOfMnemonicFromEntropy] =
    deriveEncoder[ParamsOfMnemonicFromEntropy]
}

case class ParamsOfMnemonicFromRandom(
    dictionary: Option[Long],
    word_count: Option[Long]
) extends Bind {
  override type Out = ResultOfMnemonicFromRandom
  override val decoder: Decoder[ResultOfMnemonicFromRandom] =
    implicitly[Decoder[ResultOfMnemonicFromRandom]]
}

object ParamsOfMnemonicFromRandom {
  implicit val encoder: Encoder[ParamsOfMnemonicFromRandom] =
    deriveEncoder[ParamsOfMnemonicFromRandom]
}

case class ParamsOfMnemonicVerify(
    phrase: String,
    dictionary: Option[Long],
    word_count: Option[Long]
) extends Bind {
  override type Out = ResultOfMnemonicVerify
  override val decoder: Decoder[ResultOfMnemonicVerify] =
    implicitly[Decoder[ResultOfMnemonicVerify]]
}

object ParamsOfMnemonicVerify {
  implicit val encoder: Encoder[ParamsOfMnemonicVerify] =
    deriveEncoder[ParamsOfMnemonicVerify]
}

case class ParamsOfMnemonicWords(dictionary: Option[Long]) extends Bind {
  override type Out = ResultOfMnemonicWords
  override val decoder: Decoder[ResultOfMnemonicWords] =
    implicitly[Decoder[ResultOfMnemonicWords]]
}

object ParamsOfMnemonicWords {
  implicit val encoder: Encoder[ParamsOfMnemonicWords] =
    deriveEncoder[ParamsOfMnemonicWords]
}

case class ParamsOfModularPower(
    base: String,
    exponent: String,
    modulus: String
) extends Bind {
  override type Out = ResultOfModularPower
  override val decoder: Decoder[ResultOfModularPower] =
    implicitly[Decoder[ResultOfModularPower]]
}

object ParamsOfModularPower {
  implicit val encoder: Encoder[ParamsOfModularPower] =
    deriveEncoder[ParamsOfModularPower]
}

case class ParamsOfNaclBox(
    decrypted: String,
    nonce: String,
    their_public: String,
    secret: String
) extends Bind {
  override type Out = ResultOfNaclBox
  override val decoder: Decoder[ResultOfNaclBox] =
    implicitly[Decoder[ResultOfNaclBox]]
}

object ParamsOfNaclBox {
  implicit val encoder: Encoder[ParamsOfNaclBox] =
    deriveEncoder[ParamsOfNaclBox]
}

case class ParamsOfNaclBoxKeyPairFromSecret(secret: String) extends Bind {
  override type Out = KeyPair
  override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
}

object ParamsOfNaclBoxKeyPairFromSecret {
  implicit val encoder: Encoder[ParamsOfNaclBoxKeyPairFromSecret] =
    deriveEncoder[ParamsOfNaclBoxKeyPairFromSecret]
}

case class ParamsOfNaclBoxOpen(
    encrypted: String,
    nonce: String,
    their_public: String,
    secret: String
) extends Bind {
  override type Out = ResultOfNaclBoxOpen
  override val decoder: Decoder[ResultOfNaclBoxOpen] =
    implicitly[Decoder[ResultOfNaclBoxOpen]]
}

object ParamsOfNaclBoxOpen {
  implicit val encoder: Encoder[ParamsOfNaclBoxOpen] =
    deriveEncoder[ParamsOfNaclBoxOpen]
}

case class ParamsOfNaclSecretBox(
    decrypted: String,
    nonce: String,
    key: String
) extends Bind {
  override type Out = ResultOfNaclBox
  override val decoder: Decoder[ResultOfNaclBox] =
    implicitly[Decoder[ResultOfNaclBox]]
}

object ParamsOfNaclSecretBox {
  implicit val encoder: Encoder[ParamsOfNaclSecretBox] =
    deriveEncoder[ParamsOfNaclSecretBox]
}

case class ParamsOfNaclSecretBoxOpen(
    encrypted: String,
    nonce: String,
    key: String
) extends Bind {
  override type Out = ResultOfNaclBoxOpen
  override val decoder: Decoder[ResultOfNaclBoxOpen] =
    implicitly[Decoder[ResultOfNaclBoxOpen]]
}

object ParamsOfNaclSecretBoxOpen {
  implicit val encoder: Encoder[ParamsOfNaclSecretBoxOpen] =
    deriveEncoder[ParamsOfNaclSecretBoxOpen]
}

case class ParamsOfNaclSign(unsigned: String, secret: String) extends Bind {
  override type Out = ResultOfNaclSign
  override val decoder: Decoder[ResultOfNaclSign] =
    implicitly[Decoder[ResultOfNaclSign]]
}

object ParamsOfNaclSign {
  implicit val encoder: Encoder[ParamsOfNaclSign] =
    deriveEncoder[ParamsOfNaclSign]
}

// TODO were added
case class ParamsOfNaclSignDetached(unsigned: String, secret: String)
    extends Bind {
  override type Out = ResultOfNaclSignDetached
  override val decoder: Decoder[ResultOfNaclSignDetached] =
    implicitly[Decoder[ResultOfNaclSignDetached]]
}

object ParamsOfNaclSignDetached {
  implicit val ParamsOfNaclSignEncoder: Encoder[ParamsOfNaclSignDetached] =
    deriveEncoder[ParamsOfNaclSignDetached]
}

case class ParamsOfNaclSignKeyPairFromSecret(secret: String) extends Bind {
  override type Out = KeyPair
  override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
}

object ParamsOfNaclSignKeyPairFromSecret {
  implicit val encoder: Encoder[ParamsOfNaclSignKeyPairFromSecret] =
    deriveEncoder[ParamsOfNaclSignKeyPairFromSecret]
}

case class ParamsOfNaclSignOpen(signed: String, public: String) extends Bind {
  override type Out = ResultOfNaclSignOpen
  override val decoder: Decoder[ResultOfNaclSignOpen] =
    implicitly[Decoder[ResultOfNaclSignOpen]]
}

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
) extends Bind {
  override type Out = ResultOfScrypt
  override val decoder: Decoder[ResultOfScrypt] =
    implicitly[Decoder[ResultOfScrypt]]
}

object ParamsOfScrypt {
  implicit val encoder: Encoder[ParamsOfScrypt] =
    deriveEncoder[ParamsOfScrypt]
}

case class ParamsOfSign(unsigned: String, keys: KeyPair) extends Bind {
  override type Out = ResultOfSign
  override val decoder: Decoder[ResultOfSign] =
    implicitly[Decoder[ResultOfSign]]
}

object ParamsOfSign {
  implicit val encoder: Encoder[ParamsOfSign] = deriveEncoder[ParamsOfSign]
}

case class ParamsOfSigningBoxSign(
    signing_box: SigningBoxHandle,
    unsigned: String
) extends Bind {
  override type Out = ResultOfSigningBoxSign
  override val decoder: Decoder[ResultOfSigningBoxSign] =
    implicitly[Decoder[ResultOfSigningBoxSign]]
}

object ParamsOfSigningBoxSign {
  implicit val encoder: Encoder[ParamsOfSigningBoxSign] =
    deriveEncoder[ParamsOfSigningBoxSign]
}

case class ParamsOfTonCrc16(data: String) extends Bind {
  override type Out = ResultOfTonCrc16
  override val decoder: Decoder[ResultOfTonCrc16] =
    implicitly[Decoder[ResultOfTonCrc16]]
}

object ParamsOfTonCrc16 {
  implicit val encoder: Encoder[ParamsOfTonCrc16] =
    deriveEncoder[ParamsOfTonCrc16]
}

case class ParamsOfVerifySignature(signed: String, public: String)
    extends Bind {
  override type Out = ResultOfVerifySignature
  override val decoder: Decoder[ResultOfVerifySignature] =
    implicitly[Decoder[ResultOfVerifySignature]]
}

object ParamsOfVerifySignature {
  implicit val encoder: Encoder[ParamsOfVerifySignature] =
    deriveEncoder[ParamsOfVerifySignature]
}

case class RegisteredSigningBox(handle: SigningBoxHandle) extends Bind {
  override type Out = Unit
  override val decoder: Decoder[Unit] = implicitly[Decoder[Unit]]
}

object RegisteredSigningBox {
  implicit val codec: Codec[RegisteredSigningBox] = deriveCodec[RegisteredSigningBox]
}

// TODO were added
case class RegisteredSigningBox1(handle: SigningBoxHandle) extends Bind {
  override type Out = ResultOfSigningBoxGetPublicKey
  override val decoder: Decoder[ResultOfSigningBoxGetPublicKey] =
    implicitly[Decoder[ResultOfSigningBoxGetPublicKey]]
}

object RegisteredSigningBox1 {
  implicit val encoder: Encoder[RegisteredSigningBox1] = deriveEncoder[RegisteredSigningBox1]
}

// TODO Not used
sealed trait ResultOfAppSigningBox

object ResultOfAppSigningBox {
  case class GetPublicKey(public_key: String) extends ResultOfAppSigningBox
  case class Sign(signature: String) extends ResultOfAppSigningBox
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
  implicit val codec: Codec[SigningBoxHandle] =
    deriveCodec[SigningBoxHandle]
}
