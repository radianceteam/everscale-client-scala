package com.radiance.scala.types

import io.circe.derivation.{deriveCodec, deriveDecoder, deriveEncoder}
import io.circe.{Codec, Decoder, Encoder}

object CryptoTypes {

  case class SigningBoxHandle(value: BigInt)

  case class ParamsOfFactorize(composite: String) extends Bind {
    override type Out = ResultOfFactorize
    override val decoder: Decoder[ResultOfFactorize] = implicitly[Decoder[ResultOfFactorize]]
  }

  case class ResultOfFactorize(factors: List[String])

  case class ParamsOfModularPower(base: String, exponent: String, modulus: String) extends Bind {
    override type Out = ResultOfModularPower
    override val decoder: Decoder[ResultOfModularPower] = implicitly[Decoder[ResultOfModularPower]]
  }

  case class ResultOfModularPower(modular_power: String)

  case class ParamsOfTonCrc16(data: String) extends Bind {
    override type Out = ResultOfTonCrc16
    override val decoder: Decoder[ResultOfTonCrc16] = implicitly[Decoder[ResultOfTonCrc16]]
  }

  case class ResultOfTonCrc16(crc: Long)

  case class ParamsOfGenerateRandomBytes(length: Long) extends Bind {
    override type Out = ResultOfGenerateRandomBytes
    override val decoder: Decoder[ResultOfGenerateRandomBytes] = implicitly[Decoder[ResultOfGenerateRandomBytes]]
  }

  case class ResultOfGenerateRandomBytes(bytes: String)

  case class ParamsOfConvertPublicKeyToTonSafeFormat(public_key: String) extends Bind {
    override type Out = ResultOfConvertPublicKeyToTonSafeFormat
    override val decoder: Decoder[ResultOfConvertPublicKeyToTonSafeFormat] = implicitly[Decoder[ResultOfConvertPublicKeyToTonSafeFormat]]
  }

  case class ResultOfConvertPublicKeyToTonSafeFormat(ton_public_key: String)

  case class KeyPair(public: String, secret: String)

  case class ParamsOfSign(unsigned: String, keys: KeyPair) extends Bind {
    override type Out = ResultOfSign
    override val decoder: Decoder[ResultOfSign] = implicitly[Decoder[ResultOfSign]]
  }

  case class ResultOfSign(signed: String, signature: String)

  case class ParamsOfVerifySignature(signed: String, public: String) extends Bind {
    override type Out = ResultOfVerifySignature
    override val decoder: Decoder[ResultOfVerifySignature] = implicitly[Decoder[ResultOfVerifySignature]]
  }

  case class ResultOfVerifySignature(unsigned: String)

  // TODO was renamed
  case class ParamsOfHash512(data: String) extends Bind {
    override type Out = ResultOfHash
    override val decoder: Decoder[ResultOfHash] = implicitly[Decoder[ResultOfHash]]
  }

  case class ResultOfHash(hash: String)

  case class ParamsOfScrypt(password: String, salt: String, log_n: Long, r: Long, p: Long, dk_len: Long) extends Bind {
    override type Out = ResultOfScrypt
    override val decoder: Decoder[ResultOfScrypt] = implicitly[Decoder[ResultOfScrypt]]
  }

  case class ResultOfScrypt(key: String)

  case class ParamsOfNaclSignKeyPairFromSecret(secret: String) extends Bind {
    override type Out = KeyPair
    override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
  }

  case class ParamsOfNaclSign(unsigned: String, secret: String) extends Bind {
    override type Out = ResultOfNaclSign
    override val decoder: Decoder[ResultOfNaclSign] = implicitly[Decoder[ResultOfNaclSign]]
  }

  case class ResultOfNaclSign(signed: String)

  case class ParamsOfNaclSignOpen(signed: String, public: String) extends Bind {
    override type Out = ResultOfNaclSignOpen
    override val decoder: Decoder[ResultOfNaclSignOpen] = implicitly[Decoder[ResultOfNaclSignOpen]]
  }

  case class ResultOfNaclSignOpen(unsigned: String)

  case class ResultOfNaclSignDetached(signature: String)

  case class ParamsOfNaclBoxKeyPairFromSecret(secret: String) extends Bind {
    override type Out = KeyPair
    override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
  }

  case class ParamsOfNaclBox(decrypted: String, nonce: String, their_public: String, secret: String) extends Bind {
    override type Out = ResultOfNaclBox
    override val decoder: Decoder[ResultOfNaclBox] = implicitly[Decoder[ResultOfNaclBox]]
  }

  case class ResultOfNaclBox(encrypted: String)

  case class ParamsOfNaclBoxOpen(encrypted: String, nonce: String, their_public: String, secret: String) extends Bind {
    override type Out = ResultOfNaclBoxOpen
    override val decoder: Decoder[ResultOfNaclBoxOpen] = implicitly[Decoder[ResultOfNaclBoxOpen]]
  }

  case class ResultOfNaclBoxOpen(decrypted: String)

  case class ParamsOfNaclSecretBox(decrypted: String, nonce: String, key: String) extends Bind {
    override type Out = ResultOfNaclBox
    override val decoder: Decoder[ResultOfNaclBox] = implicitly[Decoder[ResultOfNaclBox]]
  }

  case class ParamsOfNaclSecretBoxOpen(encrypted: String, nonce: String, key: String) extends Bind {
    override type Out = ResultOfNaclBoxOpen
    override val decoder: Decoder[ResultOfNaclBoxOpen] = implicitly[Decoder[ResultOfNaclBoxOpen]]
  }

  case class ParamsOfMnemonicWords(dictionary: Option[Long]) extends Bind {
    override type Out = ResultOfMnemonicWords
    override val decoder: Decoder[ResultOfMnemonicWords] = implicitly[Decoder[ResultOfMnemonicWords]]
  }

  case class ResultOfMnemonicWords(words: String)

  case class ParamsOfMnemonicFromRandom(dictionary: Option[Long], word_count: Option[Long]) extends Bind {
    override type Out = ResultOfMnemonicFromRandom
    override val decoder: Decoder[ResultOfMnemonicFromRandom] = implicitly[Decoder[ResultOfMnemonicFromRandom]]
  }

  case class ResultOfMnemonicFromRandom(phrase: String)

  case class ParamsOfMnemonicFromEntropy(entropy: String, dictionary: Option[Long], word_count: Option[Long]) extends Bind {
    override type Out = ResultOfMnemonicFromEntropy
    override val decoder: Decoder[ResultOfMnemonicFromEntropy] = implicitly[Decoder[ResultOfMnemonicFromEntropy]]
  }

  case class ResultOfMnemonicFromEntropy(phrase: String)

  case class ParamsOfMnemonicVerify(phrase: String, dictionary: Option[Long], word_count: Option[Long]) extends Bind {
    override type Out = ResultOfMnemonicVerify
    override val decoder: Decoder[ResultOfMnemonicVerify] = implicitly[Decoder[ResultOfMnemonicVerify]]
  }

  case class ResultOfMnemonicVerify(valid: Boolean)

  case class ParamsOfMnemonicDeriveSignKeys(phrase: String, path: Option[String], dictionary: Option[Long], word_count: Option[Long]) extends Bind {
    override type Out = KeyPair
    override val decoder: Decoder[KeyPair] = implicitly[Decoder[KeyPair]]
  }

  case class ParamsOfHDKeyXPrvFromMnemonic(phrase: String, dictionary: Option[Long], word_count: Option[Long]) extends Bind {
    override type Out = ResultOfHDKeyXPrvFromMnemonic
    override val decoder: Decoder[ResultOfHDKeyXPrvFromMnemonic] = implicitly[Decoder[ResultOfHDKeyXPrvFromMnemonic]]
  }

  case class ResultOfHDKeyXPrvFromMnemonic(xprv: String)

  case class ParamsOfHDKeyDeriveFromXPrv(xprv: String, child_index: Long, hardened: Boolean) extends Bind {
    override type Out = ResultOfHDKeyDeriveFromXPrv
    override val decoder: Decoder[ResultOfHDKeyDeriveFromXPrv] = implicitly[Decoder[ResultOfHDKeyDeriveFromXPrv]]
  }

  case class ResultOfHDKeyDeriveFromXPrv(xprv: String)

  case class ParamsOfHDKeyDeriveFromXPrvPath(xprv: String, path: String) extends Bind {
    override type Out = ResultOfHDKeyDeriveFromXPrvPath
    override val decoder: Decoder[ResultOfHDKeyDeriveFromXPrvPath] = implicitly[Decoder[ResultOfHDKeyDeriveFromXPrvPath]]
  }

  case class ResultOfHDKeyDeriveFromXPrvPath(xprv: String)

  case class ParamsOfHDKeySecretFromXPrv(xprv: String) extends Bind {
    override type Out = ResultOfHDKeySecretFromXPrv
    override val decoder: Decoder[ResultOfHDKeySecretFromXPrv] = implicitly[Decoder[ResultOfHDKeySecretFromXPrv]]
  }

  case class ResultOfHDKeySecretFromXPrv(secret: String)

  case class ParamsOfHDKeyPublicFromXPrv(xprv: String) extends Bind {
    override type Out = ResultOfHDKeyPublicFromXPrv
    override val decoder: Decoder[ResultOfHDKeyPublicFromXPrv] = implicitly[Decoder[ResultOfHDKeyPublicFromXPrv]]
  }

  case class ResultOfHDKeyPublicFromXPrv(public: String)

  case class ParamsOfChaCha20(data: String, key: String, nonce: String) extends Bind {
    override type Out = ResultOfChaCha20
    override val decoder: Decoder[ResultOfChaCha20] = implicitly[Decoder[ResultOfChaCha20]]
  }

  case class ResultOfChaCha20(data: String)


  object SigningBoxHandle {
    implicit val SigningBoxHandleEncoder: Encoder[SigningBoxHandle] = deriveEncoder[SigningBoxHandle]
  }

  object ParamsOfFactorize {
    implicit val ParamsOfFactorizeEncoder: Encoder[ParamsOfFactorize] = deriveEncoder[ParamsOfFactorize]
  }

  object ResultOfFactorize {
    implicit val ResultOfFactorizeDecoder: Decoder[ResultOfFactorize] = deriveDecoder[ResultOfFactorize]
  }

  object ParamsOfModularPower {
    implicit val ParamsOfModularPowerEncoder: Encoder[ParamsOfModularPower] = deriveEncoder[ParamsOfModularPower]
  }

  object ResultOfModularPower {
    implicit val ResultOfModularPowerDecoder: Decoder[ResultOfModularPower] = deriveDecoder[ResultOfModularPower]
  }

  object ParamsOfTonCrc16 {
    implicit val ParamsOfTonCrc16Encoder: Encoder[ParamsOfTonCrc16] = deriveEncoder[ParamsOfTonCrc16]
  }

  object ResultOfTonCrc16 {
    implicit val ResultOfTonCrc16Decoder: Decoder[ResultOfTonCrc16] = deriveDecoder[ResultOfTonCrc16]
  }

  object ParamsOfGenerateRandomBytes {
    implicit val ParamsOfGenerateRandomBytesEncoder: Encoder[ParamsOfGenerateRandomBytes] = deriveEncoder[ParamsOfGenerateRandomBytes]
  }

  object ResultOfGenerateRandomBytes {
    implicit val ResultOfGenerateRandomBytesDecoder: Decoder[ResultOfGenerateRandomBytes] = deriveDecoder[ResultOfGenerateRandomBytes]
  }

  object ParamsOfConvertPublicKeyToTonSafeFormat {
    implicit val ParamsOfConvertPublicKeyToTonSafeFormatEncoder: Encoder[ParamsOfConvertPublicKeyToTonSafeFormat] = deriveEncoder[ParamsOfConvertPublicKeyToTonSafeFormat]
  }

  object ResultOfConvertPublicKeyToTonSafeFormat {
    implicit val ResultOfConvertPublicKeyToTonSafeFormatDecoder: Decoder[ResultOfConvertPublicKeyToTonSafeFormat] = deriveDecoder[ResultOfConvertPublicKeyToTonSafeFormat]
  }

  object KeyPair {
    implicit val KeyPairCodec: Codec[KeyPair] = deriveCodec[KeyPair]
  }

  object ParamsOfSign {
    implicit val ParamsOfSignEncoder: Encoder[ParamsOfSign] = deriveEncoder[ParamsOfSign]
  }

  object ResultOfSign {
    implicit val ResultOfSignDecoder: Decoder[ResultOfSign] = deriveDecoder[ResultOfSign]
  }

  object ParamsOfVerifySignature {
    implicit val ParamsOfVerifySignatureEncoder: Encoder[ParamsOfVerifySignature] = deriveEncoder[ParamsOfVerifySignature]
  }

  object ResultOfVerifySignature {
    implicit val ResultOfVerifySignatureDecoder: Decoder[ResultOfVerifySignature] = deriveDecoder[ResultOfVerifySignature]
  }

  object ParamsOfHash512 {
    implicit val ParamsOfHash512Encoder: Encoder[ParamsOfHash512] = deriveEncoder[ParamsOfHash512]
  }

  object ResultOfHash {
    implicit val ResultOfHashDecoder: Decoder[ResultOfHash] = deriveDecoder[ResultOfHash]
  }

  object ParamsOfScrypt {
    implicit val ParamsOfScryptEncoder: Encoder[ParamsOfScrypt] = deriveEncoder[ParamsOfScrypt]
  }

  object ResultOfScrypt {
    implicit val ResultOfScryptDecoder: Decoder[ResultOfScrypt] = deriveDecoder[ResultOfScrypt]
  }

  object ParamsOfNaclSignKeyPairFromSecret {
    implicit val ParamsOfNaclSignKeyPairFromSecretEncoder: Encoder[ParamsOfNaclSignKeyPairFromSecret] = deriveEncoder[ParamsOfNaclSignKeyPairFromSecret]
  }

  object ParamsOfNaclSign {
    implicit val ParamsOfNaclSignEncoder: Encoder[ParamsOfNaclSign] = deriveEncoder[ParamsOfNaclSign]
  }

  object ResultOfNaclSign {
    implicit val ResultOfNaclSignDecoder: Decoder[ResultOfNaclSign] = deriveDecoder[ResultOfNaclSign]
  }

  object ParamsOfNaclSignOpen {
    implicit val ParamsOfNaclSignOpenEncoder: Encoder[ParamsOfNaclSignOpen] = deriveEncoder[ParamsOfNaclSignOpen]
  }

  object ResultOfNaclSignOpen {
    implicit val ResultOfNaclSignOpenDecoder: Decoder[ResultOfNaclSignOpen] = deriveDecoder[ResultOfNaclSignOpen]
  }

  object ResultOfNaclSignDetached {
    implicit val ResultOfNaclSignDetachedDecoder: Decoder[ResultOfNaclSignDetached] = deriveDecoder[ResultOfNaclSignDetached]
  }

  object ParamsOfNaclBoxKeyPairFromSecret {
    implicit val ParamsOfNaclBoxKeyPairFromSecretEncoder: Encoder[ParamsOfNaclBoxKeyPairFromSecret] = deriveEncoder[ParamsOfNaclBoxKeyPairFromSecret]
  }

  object ParamsOfNaclBox {
    implicit val ParamsOfNaclBoxEncoder: Encoder[ParamsOfNaclBox] = deriveEncoder[ParamsOfNaclBox]
  }

  object ResultOfNaclBox {
    implicit val ResultOfNaclBoxDecoder: Decoder[ResultOfNaclBox] = deriveDecoder[ResultOfNaclBox]
  }

  object ParamsOfNaclBoxOpen {
    implicit val ParamsOfNaclBoxOpenEncoder: Encoder[ParamsOfNaclBoxOpen] = deriveEncoder[ParamsOfNaclBoxOpen]
  }

  object ResultOfNaclBoxOpen {
    implicit val ResultOfNaclBoxOpenDecoder: Decoder[ResultOfNaclBoxOpen] = deriveDecoder[ResultOfNaclBoxOpen]
  }

  object ParamsOfNaclSecretBox {
    implicit val ParamsOfNaclSecretBoxEncoder: Encoder[ParamsOfNaclSecretBox] = deriveEncoder[ParamsOfNaclSecretBox]
  }

  object ParamsOfNaclSecretBoxOpen {
    implicit val ParamsOfNaclSecretBoxOpenEncoder: Encoder[ParamsOfNaclSecretBoxOpen] = deriveEncoder[ParamsOfNaclSecretBoxOpen]
  }

  object ParamsOfMnemonicWords {
    implicit val ParamsOfMnemonicWordsEncoder: Encoder[ParamsOfMnemonicWords] = deriveEncoder[ParamsOfMnemonicWords]
  }

  object ResultOfMnemonicWords {
    implicit val ResultOfMnemonicWordsDecoder: Decoder[ResultOfMnemonicWords] = deriveDecoder[ResultOfMnemonicWords]
  }

  object ParamsOfMnemonicFromRandom {
    implicit val ParamsOfMnemonicFromRandomEncoder: Encoder[ParamsOfMnemonicFromRandom] = deriveEncoder[ParamsOfMnemonicFromRandom]
  }

  object ResultOfMnemonicFromRandom {
    implicit val ResultOfMnemonicFromRandomDecoder: Decoder[ResultOfMnemonicFromRandom] = deriveDecoder[ResultOfMnemonicFromRandom]
  }

  object ParamsOfMnemonicFromEntropy {
    implicit val ParamsOfMnemonicFromEntropyEncoder: Encoder[ParamsOfMnemonicFromEntropy] = deriveEncoder[ParamsOfMnemonicFromEntropy]
  }

  object ResultOfMnemonicFromEntropy {
    implicit val ResultOfMnemonicFromEntropyDecoder: Decoder[ResultOfMnemonicFromEntropy] = deriveDecoder[ResultOfMnemonicFromEntropy]
  }

  object ParamsOfMnemonicVerify {
    implicit val ParamsOfMnemonicVerifyEncoder: Encoder[ParamsOfMnemonicVerify] = deriveEncoder[ParamsOfMnemonicVerify]
  }

  object ResultOfMnemonicVerify {
    implicit val ResultOfMnemonicVerifyDecoder: Decoder[ResultOfMnemonicVerify] = deriveDecoder[ResultOfMnemonicVerify]
  }

  object ParamsOfMnemonicDeriveSignKeys {
    implicit val ParamsOfMnemonicDeriveSignKeysEncoder: Encoder[ParamsOfMnemonicDeriveSignKeys] = deriveEncoder[ParamsOfMnemonicDeriveSignKeys]
  }

  object ParamsOfHDKeyXPrvFromMnemonic {
    implicit val ParamsOfHDKeyXPrvFromMnemonicEncoder: Encoder[ParamsOfHDKeyXPrvFromMnemonic] = deriveEncoder[ParamsOfHDKeyXPrvFromMnemonic]
  }

  object ResultOfHDKeyXPrvFromMnemonic {
    implicit val ResultOfHDKeyXPrvFromMnemonicDecoder: Decoder[ResultOfHDKeyXPrvFromMnemonic] = deriveDecoder[ResultOfHDKeyXPrvFromMnemonic]
  }

  object ParamsOfHDKeyDeriveFromXPrv {
    implicit val ParamsOfHDKeyDeriveFromXPrvEncoder: Encoder[ParamsOfHDKeyDeriveFromXPrv] = deriveEncoder[ParamsOfHDKeyDeriveFromXPrv]
  }

  object ResultOfHDKeyDeriveFromXPrv {
    implicit val ResultOfHDKeyDeriveFromXPrvDecoder: Decoder[ResultOfHDKeyDeriveFromXPrv] = deriveDecoder[ResultOfHDKeyDeriveFromXPrv]
  }

  object ParamsOfHDKeyDeriveFromXPrvPath {
    implicit val ParamsOfHDKeyDeriveFromXPrvPathEncoder: Encoder[ParamsOfHDKeyDeriveFromXPrvPath] = deriveEncoder[ParamsOfHDKeyDeriveFromXPrvPath]
  }

  object ResultOfHDKeyDeriveFromXPrvPath {
    implicit val ResultOfHDKeyDeriveFromXPrvPathDecoder: Decoder[ResultOfHDKeyDeriveFromXPrvPath] = deriveDecoder[ResultOfHDKeyDeriveFromXPrvPath]
  }

  object ParamsOfHDKeySecretFromXPrv {
    implicit val ParamsOfHDKeySecretFromXPrvEncoder: Encoder[ParamsOfHDKeySecretFromXPrv] = deriveEncoder[ParamsOfHDKeySecretFromXPrv]
  }

  object ResultOfHDKeySecretFromXPrv {
    implicit val ResultOfHDKeySecretFromXPrvDecoder: Decoder[ResultOfHDKeySecretFromXPrv] = deriveDecoder[ResultOfHDKeySecretFromXPrv]
  }

  object ParamsOfHDKeyPublicFromXPrv {
    implicit val ParamsOfHDKeyPublicFromXPrvEncoder: Encoder[ParamsOfHDKeyPublicFromXPrv] = deriveEncoder[ParamsOfHDKeyPublicFromXPrv]
  }

  object ResultOfHDKeyPublicFromXPrv {
    implicit val ResultOfHDKeyPublicFromXPrvDecoder: Decoder[ResultOfHDKeyPublicFromXPrv] = deriveDecoder[ResultOfHDKeyPublicFromXPrv]
  }

  object ParamsOfChaCha20 {
    implicit val ParamsOfChaCha20Encoder: Encoder[ParamsOfChaCha20] = deriveEncoder[ParamsOfChaCha20]
  }

  object ResultOfChaCha20 {
    implicit val ResultOfChaCha20Decoder: Decoder[ResultOfChaCha20] = deriveDecoder[ResultOfChaCha20]
  }

  // TODO were added
  case class ParamsOfNaclSignDetached(unsigned: String, secret: String) extends Bind {
    override type Out = ResultOfNaclSignDetached
    override val decoder: Decoder[ResultOfNaclSignDetached] = implicitly[Decoder[ResultOfNaclSignDetached]]
  }

  object ParamsOfNaclSignDetached {
    implicit val ParamsOfNaclSignEncoder: Encoder[ParamsOfNaclSignDetached] = deriveEncoder[ParamsOfNaclSignDetached]
  }

  case class ParamsOfHash256(data: String) extends Bind {
    override type Out = ResultOfHash
    override val decoder: Decoder[ResultOfHash] = implicitly[Decoder[ResultOfHash]]
  }

  object ParamsOfHash256 {
    implicit val ParamsOfHash256Encoder: Encoder[ParamsOfHash256] = deriveEncoder[ParamsOfHash256]
  }

}
