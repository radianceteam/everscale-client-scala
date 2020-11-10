/*
package client {
  case class ClientError(code: Long, message: String, data: com.radiance.Value)
  case class ClientConfig(network: Option[client.NetworkConfig], crypto: Option[client.CryptoConfig], abi: Option[client.AbiConfig])
  case class NetworkConfig(server_address: String, network_retries_count: Option[Int], message_retries_count: Option[Int], message_processing_timeout: Option[Long], wait_for_timeout: Option[Long], out_of_sync_threshold: Option[Long], access_key: Option[String])
  case class CryptoConfig(mnemonic_dictionary: Option[Long], mnemonic_word_count: Option[Long], hdkey_derivation_path: Option[String], hdkey_compliant: Option[Boolean])
  case class AbiConfig(workchain: Option[Int], message_expiration_timeout: Option[Long], message_expiration_timeout_grow_factor: Option[Float])
  case class BuildInfoDependency(name: String, git_commit: String)
  case class ResultOfGetApiReference(api: com.radiance.API)
  case class ResultOfVersion(version: String)
  case class ResultOfBuildInfo(build_number: Long, dependencies: List[client.BuildInfoDependency])
}

package crypto {
  case class SigningBoxHandle(value: BigInt)
  case class ParamsOfFactorize(composite: String)
  case class ResultOfFactorize(factors: List[String])
  case class ParamsOfModularPower(base: String, exponent: String, modulus: String)
  case class ResultOfModularPower(modular_power: String)
  case class ParamsOfTonCrc16(data: String)
  case class ResultOfTonCrc16(crc: Long)
  case class ParamsOfGenerateRandomBytes(length: Long)
  case class ResultOfGenerateRandomBytes(bytes: String)
  case class ParamsOfConvertPublicKeyToTonSafeFormat(public_key: String)
  case class ResultOfConvertPublicKeyToTonSafeFormat(ton_public_key: String)
  case class KeyPair(public: String, secret: String)
  case class ParamsOfSign(unsigned: String, keys: crypto.KeyPair)
  case class ResultOfSign(signed: String, signature: String)
  case class ParamsOfVerifySignature(signed: String, public: String)
  case class ResultOfVerifySignature(unsigned: String)
  case class ParamsOfHash(data: String)
  case class ResultOfHash(hash: String)
  case class ParamsOfScrypt(password: String, salt: String, log_n: Long, r: Long, p: Long, dk_len: Long)
  case class ResultOfScrypt(key: String)
  case class ParamsOfNaclSignKeyPairFromSecret(secret: String)
  case class ParamsOfNaclSign(unsigned: String, secret: String)
  case class ResultOfNaclSign(signed: String)
  case class ParamsOfNaclSignOpen(signed: String, public: String)
  case class ResultOfNaclSignOpen(unsigned: String)
  case class ResultOfNaclSignDetached(signature: String)
  case class ParamsOfNaclBoxKeyPairFromSecret(secret: String)
  case class ParamsOfNaclBox(decrypted: String, nonce: String, their_public: String, secret: String)
  case class ResultOfNaclBox(encrypted: String)
  case class ParamsOfNaclBoxOpen(encrypted: String, nonce: String, their_public: String, secret: String)
  case class ResultOfNaclBoxOpen(decrypted: String)
  case class ParamsOfNaclSecretBox(decrypted: String, nonce: String, key: String)
  case class ParamsOfNaclSecretBoxOpen(encrypted: String, nonce: String, key: String)
  case class ParamsOfMnemonicWords(dictionary: Option[Long])
  case class ResultOfMnemonicWords(words: String)
  case class ParamsOfMnemonicFromRandom(dictionary: Option[Long], word_count: Option[Long])
  case class ResultOfMnemonicFromRandom(phrase: String)
  case class ParamsOfMnemonicFromEntropy(entropy: String, dictionary: Option[Long], word_count: Option[Long])
  case class ResultOfMnemonicFromEntropy(phrase: String)
  case class ParamsOfMnemonicVerify(phrase: String, dictionary: Option[Long], word_count: Option[Long])
  case class ResultOfMnemonicVerify(valid: Boolean)
  case class ParamsOfMnemonicDeriveSignKeys(phrase: String, path: Option[String], dictionary: Option[Long], word_count: Option[Long])
  case class ParamsOfHDKeyXPrvFromMnemonic(phrase: String, dictionary: Option[Long], word_count: Option[Long])
  case class ResultOfHDKeyXPrvFromMnemonic(xprv: String)
  case class ParamsOfHDKeyDeriveFromXPrv(xprv: String, child_index: Long, hardened: Boolean)
  case class ResultOfHDKeyDeriveFromXPrv(xprv: String)
  case class ParamsOfHDKeyDeriveFromXPrvPath(xprv: String, path: String)
  case class ResultOfHDKeyDeriveFromXPrvPath(xprv: String)
  case class ParamsOfHDKeySecretFromXPrv(xprv: String)
  case class ResultOfHDKeySecretFromXPrv(secret: String)
  case class ParamsOfHDKeyPublicFromXPrv(xprv: String)
  case class ResultOfHDKeyPublicFromXPrv(public: String)
  case class ParamsOfChaCha20(data: String, key: String, nonce: String)
  case class ResultOfChaCha20(data: String)
}

package abi {
  sealed trait Abi
  case class Contract(value: abi.AbiContract) extends Abi
  case class Json(value: String) extends Abi
  case class Handle(value: abi.AbiHandle) extends Abi
  case class Serialized(value: abi.AbiContract) extends Abi
  case class AbiHandle(value: BigInt)
  /**
   *  The ABI function header.
   *
   *  Includes several hidden function parameters that contract
   *  uses for security, message delivery monitoring and replay protection reasons.
   *
   *  The actual set of header fields depends on the contract's ABI.
   *  If a contract's ABI does not include some headers, then they are not filled.
   */
  case class FunctionHeader(expire: Option[Long], time: Option[BigInt], pubkey: Option[String])
  case class CallSet(function_name: String, header: Option[abi.FunctionHeader], input: Option[com.radiance.Value])
  case class DeploySet(tvc: String, workchain_id: Option[Int], initial_data: Option[com.radiance.Value])
  sealed trait Signer
  case class None() extends Signer
  case class External(public_key: String) extends Signer
  case class Keys(keys: crypto.KeyPair) extends Signer
  case class SigningBox(handle: crypto.SigningBoxHandle) extends Signer
  sealed trait MessageBodyType
  /**  Message contains the input of the ABI function. */
  object Input extends MessageBodyType
  /**  Message contains the output of the ABI function. */
  object Output extends MessageBodyType
  /**
   *  Message contains the input of the imported ABI function.
   *
   *  Occurs when contract sends an internal message to other
   *  contract.
   */
  object InternalOutput extends MessageBodyType
  /**  Message contains the input of the ABI event. */
  object Event extends MessageBodyType
  sealed trait StateInitSource
  case class Message(source: abi.MessageSource) extends StateInitSource
  case class StateInit(code: String, data: String, library: Option[String]) extends StateInitSource
  case class Tvc(tvc: String, public_key: Option[String], init_params: Option[abi.StateInitParams]) extends StateInitSource
  case class StateInitParams(abi: abi.Abi, value: com.radiance.Value)
  sealed trait MessageSource
  case class Encoded(message: String, abi: Option[abi.Abi]) extends MessageSource
  case class EncodingParams(value: abi.ParamsOfEncodeMessage) extends MessageSource
  case class AbiParam(name: String, `type`: String, components: Option[List[abi.AbiParam]])
  case class AbiEvent(name: String, inputs: List[abi.AbiParam], id: Option[Option[Long]])
  case class AbiData(key: BigInt, name: String, `type`: String, components: Option[List[abi.AbiParam]])
  case class AbiFunction(name: String, inputs: List[abi.AbiParam], outputs: List[abi.AbiParam], id: Option[Option[Long]])
  case class AbiContract(`ABI version`: Option[Long], abi_version: Option[Long], header: Option[List[String]], functions: Option[List[abi.AbiFunction]], events: Option[List[abi.AbiEvent]], data: Option[List[abi.AbiData]])
  case class ParamsOfEncodeMessageBody(abi: abi.Abi, call_set: abi.CallSet, is_internal: Boolean, signer: abi.Signer, processing_try_index: Option[Long])
  case class ResultOfEncodeMessageBody(body: String, data_to_sign: Option[String])
  case class ParamsOfAttachSignatureToMessageBody(abi: abi.Abi, public_key: String, message: String, signature: String)
  case class ResultOfAttachSignatureToMessageBody(body: String)
  case class ParamsOfEncodeMessage(abi: abi.Abi, address: Option[String], deploy_set: Option[abi.DeploySet], call_set: Option[abi.CallSet], signer: abi.Signer, processing_try_index: Option[Long])
  case class ResultOfEncodeMessage(message: String, data_to_sign: Option[String], address: String, message_id: String)
  case class ParamsOfAttachSignature(abi: abi.Abi, public_key: String, message: String, signature: String)
  case class ResultOfAttachSignature(message: String, message_id: String)
  case class ParamsOfDecodeMessage(abi: abi.Abi, message: String)
  case class DecodedMessageBody(body_type: abi.MessageBodyType, name: String, value: Option[com.radiance.Value], header: Option[abi.FunctionHeader])
  case class ParamsOfDecodeMessageBody(abi: abi.Abi, body: String, is_internal: Boolean)
  case class ParamsOfEncodeAccount(state_init: abi.StateInitSource, balance: Option[BigInt], last_trans_lt: Option[BigInt], last_paid: Option[Long])
  case class ResultOfEncodeAccount(account: String, id: String)
}

package boc {
  case class ParamsOfParse(boc: String)
  case class ResultOfParse(parsed: com.radiance.Value)
  case class ParamsOfParseShardstate(boc: String, id: String, workchain_id: Int)
  case class ParamsOfGetBlockchainConfig(block_boc: String)
  case class ResultOfGetBlockchainConfig(config_boc: String)
}

package processing {
  sealed trait ProcessingEvent
  case class WillFetchFirstBlock() extends ProcessingEvent
  case class FetchFirstBlockFailed(error: client.ClientError) extends ProcessingEvent
  case class WillSend(shard_block_id: String, message_id: String, message: String) extends ProcessingEvent
  case class DidSend(shard_block_id: String, message_id: String, message: String) extends ProcessingEvent
  case class SendFailed(shard_block_id: String, message_id: String, message: String, error: client.ClientError) extends ProcessingEvent
  case class WillFetchNextBlock(shard_block_id: String, message_id: String, message: String) extends ProcessingEvent
  case class FetchNextBlockFailed(shard_block_id: String, message_id: String, message: String, error: client.ClientError) extends ProcessingEvent
  case class MessageExpired(message_id: String, message: String, error: client.ClientError) extends ProcessingEvent
  case class ResultOfProcessMessage(transaction: com.radiance.Value, out_messages: List[String], decoded: Option[processing.DecodedOutput], fees: tvm.TransactionFees)
  case class DecodedOutput(out_messages: List[Option[abi.DecodedMessageBody]], output: Option[com.radiance.Value])
  case class ParamsOfSendMessage(message: String, abi: Option[abi.Abi], send_events: Boolean)
  case class ResultOfSendMessage(shard_block_id: String)
  case class ParamsOfWaitForTransaction(abi: Option[abi.Abi], message: String, shard_block_id: String, send_events: Boolean)
  case class ParamsOfProcessMessage(message_encode_params: abi.ParamsOfEncodeMessage, send_events: Boolean)
}

package utils {
  sealed trait AddressStringFormat
  case class AccountId() extends AddressStringFormat
  case class Hex() extends AddressStringFormat
  case class Base64(url: Boolean, test: Boolean, bounce: Boolean) extends AddressStringFormat
  case class ParamsOfConvertAddress(address: String, output_format: utils.AddressStringFormat)
  case class ResultOfConvertAddress(address: String)
}
package tvm {
  case class ExecutionOptions(blockchain_config: Option[String], block_time: Option[Long], block_lt: Option[BigInt], transaction_lt: Option[BigInt])
  sealed trait AccountForExecutor
  case class None() extends AccountForExecutor
  case class Uninit() extends AccountForExecutor
  case class Account(boc: String, unlimited_balance: Option[Boolean]) extends AccountForExecutor
  case class TransactionFees(in_msg_fwd_fee: BigInt, storage_fee: BigInt, gas_fee: BigInt, out_msgs_fwd_fee: BigInt, total_account_fees: BigInt, total_output: BigInt)
  case class ParamsOfRunExecutor(message: String, account: tvm.AccountForExecutor, execution_options: Option[tvm.ExecutionOptions], abi: Option[abi.Abi], skip_transaction_check: Option[Boolean])
  case class ResultOfRunExecutor(transaction: com.radiance.Value, out_messages: List[String], decoded: Option[processing.DecodedOutput], account: String, fees: tvm.TransactionFees)
  case class ParamsOfRunTvm(message: String, account: String, execution_options: Option[tvm.ExecutionOptions], abi: Option[abi.Abi])
  case class ResultOfRunTvm(out_messages: List[String], decoded: Option[processing.DecodedOutput], account: String)
  case class ParamsOfRunGet(account: String, function_name: String, input: Option[com.radiance.Value], execution_options: Option[tvm.ExecutionOptions])
  case class ResultOfRunGet(output: com.radiance.Value)
}

package net {
  case class OrderBy(path: String, direction: net.SortDirection)
  sealed trait SortDirection
  object ASC extends SortDirection
  object DESC extends SortDirection
  case class ParamsOfQueryCollection(collection: String, filter: Option[com.radiance.Value], result: String, order: Option[List[net.OrderBy]], limit: Option[Long])
  case class ResultOfQueryCollection(result: List[com.radiance.Value])
  case class ParamsOfWaitForCollection(collection: String, filter: Option[com.radiance.Value], result: String, timeout: Option[Long])
  case class ResultOfWaitForCollection(result: com.radiance.Value)
  case class ResultOfSubscribeCollection(handle: Long)
  object unit
  case class ParamsOfSubscribeCollection(collection: String, filter: Option[com.radiance.Value], result: String)
}
*/
