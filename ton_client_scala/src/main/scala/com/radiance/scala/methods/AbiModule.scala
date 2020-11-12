package com.radiance.scala.methods

import com.radiance.scala.tonclient.TonContextScala
import com.radiance.scala.types.AbiTypes._

import scala.concurrent.Future

class AbiModule(ctx: TonContextScala) {
  /**
   *  Creates account state BOC
   *
   *  Creates account state provided with one of these sets of data :
   *  1. BOC of code, BOC of data, BOC of library
   *  2. TVC (string in `base64`), keys, init params@param state_init  Source of the account state init.
   * @param balance  Initial balance.
   * @param last_trans_lt  Initial value for the `last_trans_lt`.
   * @param last_paid  Initial value for the `last_paid`.
   */
  def encode_account(state_init: StateInitSource, balance: Option[BigInt], last_trans_lt: Option[BigInt], last_paid: Option[Long]): Future[Either[Throwable, ResultOfEncodeAccount]] = {
    val arg = ParamsOfEncodeAccount(state_init, balance, last_trans_lt, last_paid)
    ctx.execAsync("abi.encode_account", arg)
  }
  /**
   *  Decodes message body using provided body BOC and ABI.@param abi  Contract ABI used to decode.
   * @param body  Message body BOC encoded in `base64`.
   * @param is_internal  True if the body belongs to the internal message.
   */
  def decode_message_body(abi: Abi, body: String, is_internal: Boolean): Future[Either[Throwable, DecodedMessageBody]] = {
    val arg = ParamsOfDecodeMessageBody(abi, body, is_internal)
    ctx.execAsync("abi.decode_message_body", arg)
  }

  /**
   *  Decodes message body using provided message BOC and ABI.@param abi  contract ABI
   * @param message  Message BOC
   */
  def decode_message(abi: Abi, message: String): Future[Either[Throwable, DecodedMessageBody]] = {
    // TODO sdvornik check
    val arg = ParamsOfDecodeMessageBody(abi, message, false)
    ctx.execAsync("abi.decode_message", arg)
  }
  /**
   *  Combines `hex`-encoded `signature` with `base64`-encoded `unsigned_message`.
   *  Returns signed message encoded in `base64`.@param abi  Contract ABI
   * @param public_key  Public key encoded in `hex`.
   * @param message  Unsigned message BOC encoded in `base64`.
   * @param signature  Signature encoded in `hex`.
   */
  def attach_signature(abi: Abi, public_key: String, message: String, signature: String): Future[Either[Throwable, ResultOfAttachSignature]] = {
    val arg = ParamsOfAttachSignature(abi, public_key, message, signature)
    ctx.execAsync("abi.attach_signature", arg)
  }
  /**
   *  Encodes an ABI-compatible message
   *
   *  Allows to encode deploy and function call messages,
   *  both signed and unsigned.
   *
   *  Use cases include messages of any possible type:
   *  - deploy with initial function call (i.e. `constructor` or any other function that is used for some kind
   *  of initialization);
   *  - deploy without initial function call;
   *  - signed/unsigned + data for signing.
   *
   *  `Signer` defines how the message should or shouldn't be signed:
   *
   *  `Signer::None` creates an unsigned message. This may be needed in case of some public methods,
   *  that do not require authorization by pubkey.
   *
   *  `Signer::External` takes public key and returns `data_to_sign` for later signing.
   *  Use `attach_signature` method with the result signature to get the signed message.
   *
   *  `Signer::Keys` creates a signed message with provided key pair.
   *
   *  [SOON] `Signer::SigningBox` Allows using a special interface to imlepement signing
   *  without private key disclosure to SDK. For instance, in case of using a cold wallet or HSM,
   *  when application calls some API to sign data.@param abi  Contract ABI.
   * @param address  Target address the message will be sent to.
   *
   *  Must be specified in case of non-deploy message.
   * @param deploy_set  Deploy parameters.
   *
   *  Must be specified in case of deploy message.
   * @param call_set  Function call parameters.
   *
   *  Must be specified in case of non-deploy message.
   *
   *  In case of deploy message it is optional and contains parameters
   *  of the functions that will to be called upon deploy transaction.
   * @param signer  Signing parameters.
   * @param processing_try_index  Processing try index.
   *
   *  Used in message processing with retries (if contract's ABI includes "expire" header).
   *
   *  Encoder uses the provided try index to calculate message
   *  expiration time. The 1st message expiration time is specified in
   *  Client config.
   *
   *  Expiration timeouts will grow with every retry.
   *  Retry grow factor is set in Client config:
   *  <.....add config parameter with default value here>
   *
   *  Default value is 0.
   */
  def encode_message(abi: Abi, address: Option[String], deploy_set: Option[DeploySet], call_set: Option[CallSet], signer: Signer, processing_try_index: Option[Long]): Future[Either[Throwable, ResultOfEncodeMessage]] = {
    val arg = ParamsOfEncodeMessage(abi, address, deploy_set, call_set, signer, processing_try_index)
    ctx.execAsync("abi.encode_message", arg)
  }
  /**
   * @param abi  Contract ABI
   * @param public_key  Public key. Must be encoded with `hex`.
   * @param message  Unsigned message BOC. Must be encoded with `base64`.
   * @param signature  Signature. Must be encoded with `hex`.
   */
  def attach_signature_to_message_body(abi: Abi, public_key: String, message: String, signature: String): Future[Either[Throwable, ResultOfAttachSignatureToMessageBody]] = {
    val arg = ParamsOfAttachSignatureToMessageBody(abi, public_key, message, signature)
    ctx.execAsync("abi.attach_signature_to_message_body", arg)
  }
  /**
   *  Encodes message body according to ABI function call.@param abi  Contract ABI.
   * @param call_set  Function call parameters.
   *
   *  Must be specified in non deploy message.
   *
   *  In case of deploy message contains parameters of constructor.
   * @param is_internal  True if internal message body must be encoded.
   * @param signer  Signing parameters.
   * @param processing_try_index  Processing try index.
   *
   *  Used in message processing with retries.
   *
   *  Encoder uses the provided try index to calculate message
   *  expiration time.
   *
   *  Expiration timeouts will grow with every retry.
   *
   *  Default value is 0.
   */
  def encode_message_body(abi: Abi, call_set: CallSet, is_internal: Boolean, signer: Signer, processing_try_index: Option[Long]): Future[Either[Throwable, ResultOfEncodeMessageBody]] = {
    val arg = ParamsOfEncodeMessageBody(abi, call_set, is_internal, signer, processing_try_index)
    ctx.execAsync("abi.encode_message_body", arg)
  }
}
