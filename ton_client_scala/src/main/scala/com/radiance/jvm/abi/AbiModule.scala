package com.radiance.jvm.abi

import com.radiance.jvm.{Context, Value}
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
   * Decodes account data using provided data BOC and ABI. Note: this feature requires ABI 2.1 or higher.
   * @param abi
   *   abi
   * @param data
   *   data
   */
  def decodeAccountData(abi: AbiADT.Abi, data: String): Future[Either[Throwable, ResultOfDecodeAccountData]] = {
    ctx.execAsync[ParamsOfDecodeAccountData, ResultOfDecodeAccountData](
      "abi.decode_account_data",
      ParamsOfDecodeAccountData(abi, data)
    )
  }

  /**
   * Decodes BOC into JSON as a set of provided parameters. Solidity functions use ABI types for [builder
   * encoding](https://github.com/tonlabs/TON-Solidity-Compiler/blob/master/API.md#tvmbuilderstore). The simplest way to
   * decode such a BOC is to use ABI decoding. ABI has it own rules for fields layout in cells so manually encoded BOC
   * can not be described in terms of ABI rules.
   *
   * To solve this problem we introduce a new ABI type `Ref(<ParamType>)` which allows to store `ParamType` ABI
   * parameter in cell reference and, thus, decode manually encoded BOCs. This type is available only in `decode_boc`
   * function and will not be available in ABI messages encoding until it is included into some ABI revision.
   *
   * Such BOC descriptions covers most users needs. If someone wants to decode some BOC which can not be described by
   * these rules (i.e. BOC with TLB containing constructors of flags defining some parsing conditions) then they can
   * decode the fields up to fork condition, check the parsed data manually, expand the parsing schema and then decode
   * the whole BOC with the full schema.
   * @param params
   *   params
   * @param boc
   *   boc
   * @param allow_partial
   *   allow_partial
   */
  def decodeBoc(
    params: List[AbiParam],
    boc: String,
    allow_partial: Boolean
  ): Future[Either[Throwable, ResultOfDecodeBoc]] = {
    ctx.execAsync[ParamsOfDecodeBoc, ResultOfDecodeBoc](
      "abi.decode_boc",
      ParamsOfDecodeBoc(params, boc, allow_partial)
    )
  }

  /**
   * Decodes initial values of a contract's static variables and owner's public key from account initial data This
   * operation is applicable only for initial account data (before deploy). If the contract is already deployed, its
   * data doesn't contain this data section any more.
   * @param abi
   *   Initial data is decoded if this parameter is provided
   * @param data
   *   data
   */
  def decodeInitialData(abi: Option[AbiADT.Abi], data: String): Future[Either[Throwable, ResultOfDecodeInitialData]] = {
    ctx.execAsync[ParamsOfDecodeInitialData, ResultOfDecodeInitialData](
      "abi.decode_initial_data",
      ParamsOfDecodeInitialData(abi, data)
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
   * Encodes initial account data with initial values for the contract's static variables and owner's public key into a
   * data BOC that can be passed to `encode_tvc` function afterwards. This function is analogue of `tvm.buildDataInit`
   * function in Solidity.
   * @param abi
   *   abi
   * @param initial_data
   *   `abi` parameter should be provided to set initial data
   * @param initial_pubkey
   *   initial_pubkey
   * @param boc_cache
   *   boc_cache
   */
  def encodeInitialData(
    abi: Option[AbiADT.Abi],
    initial_data: Option[Value],
    initial_pubkey: Option[String],
    boc_cache: Option[BocCacheTypeADT.BocCacheType]
  ): Future[Either[Throwable, ResultOfEncodeInitialData]] = {
    ctx.execAsync[ParamsOfEncodeInitialData, ResultOfEncodeInitialData](
      "abi.encode_initial_data",
      ParamsOfEncodeInitialData(abi, initial_data, initial_pubkey, boc_cache)
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

  /**
   * Updates initial account data with initial values for the contract's static variables and owner's public key. This
   * operation is applicable only for initial account data (before deploy). If the contract is already deployed, its
   * data doesn't contain this data section any more.
   * @param abi
   *   abi
   * @param data
   *   data
   * @param initial_data
   *   `abi` parameter should be provided to set initial data
   * @param initial_pubkey
   *   initial_pubkey
   * @param boc_cache
   *   boc_cache
   */
  def updateInitialData(
    abi: Option[AbiADT.Abi],
    data: String,
    initial_data: Option[Value],
    initial_pubkey: Option[String],
    boc_cache: Option[BocCacheTypeADT.BocCacheType]
  ): Future[Either[Throwable, ResultOfUpdateInitialData]] = {
    ctx.execAsync[ParamsOfUpdateInitialData, ResultOfUpdateInitialData](
      "abi.update_initial_data",
      ParamsOfUpdateInitialData(
        abi,
        data,
        initial_data,
        initial_pubkey,
        boc_cache
      )
    )
  }

}
