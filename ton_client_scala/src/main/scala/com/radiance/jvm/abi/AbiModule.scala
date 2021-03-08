package com.radiance.jvm.abi

import com.radiance.jvm.Context
import com.radiance.jvm.boc._

import scala.concurrent.Future

class AbiModule(ctx: Context) {

  /**
   * Combines `hex`-encoded `signature` with `base64`-encoded `unsigned_message`. Returns signed message encoded in
   * `base64`.
   * @param abi
   *   Contract ABI
   *
   * @param public_key
   *   Public key encoded in `hex`.
   * @param message
   *   Unsigned message BOC encoded in `base64`.
   * @param signature
   *   Signature encoded in `hex`.
   */
  def attachSignature(
    abi: AbiADT.Abi,
    public_key: String,
    message: String,
    signature: String
  ): Future[Either[Throwable, ResultOfAttachSignature]] = {
    ctx.execAsync[ParamsOfAttachSignature, ResultOfAttachSignature](
      "abi.attach_signature",
      ParamsOfAttachSignature(abi, public_key, message, signature)
    )
  }

  /**
   * @param abi
   *   Contract ABI
   * @param public_key
   *   Public key. Must be encoded with `hex`.
   * @param message
   *   Unsigned message BOC. Must be encoded with `base64`.
   * @param signature
   *   Signature. Must be encoded with `hex`.
   */
  def attachSignatureToMessageBody(
    abi: AbiADT.Abi,
    public_key: String,
    message: String,
    signature: String
  ): Future[Either[Throwable, ResultOfAttachSignatureToMessageBody]] = {
    ctx.execAsync[ParamsOfAttachSignatureToMessageBody, ResultOfAttachSignatureToMessageBody](
      "abi.attach_signature_to_message_body",
      ParamsOfAttachSignatureToMessageBody(abi, public_key, message, signature)
    )
  }

  /**
   * Decodes message body using provided message BOC and ABI.
   * @param abi
   *   contract ABI
   *
   * @param message
   *   Message BOC
   */
  def decodeMessage(
    abi: AbiADT.Abi,
    message: String
  ): Future[Either[Throwable, DecodedMessageBody]] = {
    ctx.execAsync[ParamsOfDecodeMessage, DecodedMessageBody]("abi.decode_message", ParamsOfDecodeMessage(abi, message))
  }

  /**
   * Decodes message body using provided body BOC and ABI.
   * @param abi
   *   Contract ABI used to decode.
   *
   * @param body
   *   Message body BOC encoded in `base64`.
   * @param is_internal
   *   True if the body belongs to the internal message.
   */
  def decodeMessageBody(
    abi: AbiADT.Abi,
    body: String,
    is_internal: Boolean
  ): Future[Either[Throwable, DecodedMessageBody]] = {
    ctx.execAsync[ParamsOfDecodeMessageBody, DecodedMessageBody](
      "abi.decode_message_body",
      ParamsOfDecodeMessageBody(abi, body, is_internal)
    )
  }

  /**
   * Creates account state BOC
   *
   * Creates account state provided with one of these sets of data :
   *   1. BOC of code, BOC of data, BOC of library 2. TVC (string in `base64`), keys, init params
   * @param state_init
   *   Source of the account state init.
   *
   * @param balance
   *   Initial balance.
   * @param last_trans_lt
   *   Initial value for the `last_trans_lt`.
   * @param last_paid
   *   Initial value for the `last_paid`.
   * @param boc_cache
   *   The BOC itself returned if no cache type provided
   */
  def encodeAccount(
    state_init: StateInitSourceADT.StateInitSource,
    balance: Option[BigInt],
    last_trans_lt: Option[BigInt],
    last_paid: Option[Long],
    boc_cache: Option[BocCacheTypeADT.BocCacheType]
  ): Future[Either[Throwable, ResultOfEncodeAccount]] = {
    ctx.execAsync[ParamsOfEncodeAccount, ResultOfEncodeAccount](
      "abi.encode_account",
      ParamsOfEncodeAccount(state_init, balance, last_trans_lt, last_paid, boc_cache)
    )
  }

  /**
   * Encodes an internal ABI-compatible message Allows to encode deploy and function call messages.
   *
   * Use cases include messages of any possible type:
   *   - deploy with initial function call (i.e. `constructor` or any other function that is used for some kind of
   *     initialization);
   *   - deploy without initial function call;
   *   - simple function call
   *
   * There is an optional public key can be provided in deploy set in order to substitute one in TVM file.
   *
   * Public key resolving priority:
   *   1. Public key from deploy set. 2. Public key, specified in TVM file.
   * @param abi
   *   Can be None if both deploy_set and call_set are None.
   * @param address
   *   Must be specified in case of non-deploy message.
   * @param src_address
   *   src_address
   * @param deploy_set
   *   Must be specified in case of deploy message.
   * @param call_set
   *   Must be specified in case of non-deploy message.
   *
   * In case of deploy message it is optional and contains parameters of the functions that will to be called upon
   * deploy transaction.
   * @param value
   *   value
   * @param bounce
   *   Default is true.
   * @param enable_ihr
   *   Default is false.
   */
  def encodeInternalMessage(
    abi: Option[AbiADT.Abi],
    address: Option[String],
    src_address: Option[String],
    deploy_set: Option[DeploySet],
    call_set: Option[CallSet],
    value: String,
    bounce: Option[Boolean],
    enable_ihr: Option[Boolean]
  ): Future[Either[Throwable, ResultOfEncodeInternalMessage]] =
    ctx.execAsync[ParamsOfEncodeInternalMessage, ResultOfEncodeInternalMessage](
      "abi.encode_internal_message",
      ParamsOfEncodeInternalMessage(abi, address, src_address, deploy_set, call_set, value, bounce, enable_ihr)
    )

  /**
   * Encodes an ABI-compatible message. Allows to encode deploy and function call messages, both signed and unsigned.
   *
   * Use cases include messages of any possible type:
   *   - deploy with initial function call (i.e. `constructor` or any other function that is used for some kind of
   *     initialization);
   *   - deploy without initial function call;
   *   - signed/unsigned + data for signing.
   *
   * `Signer` defines how the message should or shouldn't be signed:
   *
   * `Signer::None` creates an unsigned message. This may be needed in case of some public methods, that do not require
   * authorization by pubkey.
   *
   * `Signer::External` takes public key and returns `data_to_sign` for later signing. Use `attach_signature` method
   * with the result signature to get the signed message.
   *
   * `Signer::Keys` creates a signed message with provided key pair.
   *
   * [SOON] `Signer::SigningBox` Allows using a special interface to implement signing without private key disclosure to
   * SDK. For instance, in case of using a cold wallet or HSM, when application calls some API to sign data.
   *
   * There is an optional public key can be provided in deploy set in order to substitute one in TVM file.
   *
   * Public key resolving priority:
   *   1. Public key from deploy set. 2. Public key, specified in TVM file. 3. Public key, provided by signer.
   * @param abi
   *   Contract ABI
   * @param address
   *   Must be specified in case of non-deploy message.
   * @param deploy_set
   *   Must be specified in case of deploy message.
   * @param call_set
   *   Must be specified in case of non-deploy message.
   *
   * In case of deploy message it is optional and contains parameters of the functions that will to be called upon
   * deploy transaction.
   * @param signer
   *   Signer parameter
   * @param processing_try_index
   *   .Used in message processing with retries (if contract's ABI includes "expire" header).
   *
   * Encoder uses the provided try index to calculate message expiration time. The 1st message expiration time is
   * specified in Client config.
   *
   * Expiration timeouts will grow with every retry. Retry grow factor is set in Client config: <.....add config
   * parameter with default value here>
   *
   * Default value is 0.
   */
  def encodeMessage(
    abi: AbiADT.Abi,
    address: Option[String],
    deploy_set: Option[DeploySet],
    call_set: Option[CallSet],
    signer: SignerADT.Signer,
    processing_try_index: Option[Long]
  ): Future[Either[Throwable, ResultOfEncodeMessage]] = {
    ctx.execAsync[ParamsOfEncodeMessage, ResultOfEncodeMessage](
      "abi.encode_message",
      ParamsOfEncodeMessage(
        abi,
        address,
        deploy_set,
        call_set,
        signer,
        processing_try_index
      )
    )
  }

  /**
   * Encodes message body according to ABI function call.
   * @param abi
   *   Contract ABI.
   *
   * @param call_set
   *   Function call parameters.
   *
   * Must be specified in non deploy message.
   *
   * In case of deploy message contains parameters of constructor.
   * @param is_internal
   *   True if internal message body must be encoded.
   * @param signer
   *   Signing parameters.
   * @param processing_try_index
   *   Processing try index.
   *
   * Used in message processing with retries.
   *
   * Encoder uses the provided try index to calculate message expiration time.
   *
   * Expiration timeouts will grow with every retry.
   *
   * Default value is 0.
   */
  def encodeMessageBody(
    abi: AbiADT.Abi,
    call_set: CallSet,
    is_internal: Boolean,
    signer: SignerADT.Signer,
    processing_try_index: Option[Long]
  ): Future[Either[Throwable, ResultOfEncodeMessageBody]] = {
    ctx.execAsync[ParamsOfEncodeMessageBody, ResultOfEncodeMessageBody](
      "abi.encode_message_body",
      ParamsOfEncodeMessageBody(
        abi,
        call_set,
        is_internal,
        signer,
        processing_try_index
      )
    )
  }
}
