package com.radiance.scala.tonclient

import com.radiance.scala.tonclient.types._

import scala.concurrent.{ExecutionContext, Future}


class Abi(val context: TONContext)(implicit val ec: ExecutionContext) {
  /**
   * Encodes message body according to ABI function call.
   *
   * @param abi                Contract ABI.
   * @param callSet            Function call parameters.
   * @param isInternal         True if internal message body must be encoded.
   * @param signer             Signing parameters.
   * @param processingTryIndex Processing try index.
   */
  def encodeMessageBody(
                         abi: String,
                         callSet: String,
                         isInternal: Boolean,
                         signer: String,
                         processingTryIndex: Number
                       ): Future[ResultOfEncodeMessageBody] =
    context.requestValue[ResultOfEncodeMessageBody](
      "abi.encode_message_body",
      s"""{
         |"abi":"$abi",
         |"call_set":"$callSet",
         |"is_internal":$isInternal,
         |"signer":"$signer",
         |"processing_try_index":"$processingTryIndex"
         |}""".stripMargin
    )

  /**
   *
   *
   * @param abi       Contract ABI
   * @param publicKey Public key. Must be encoded with `hex`.
   * @param message   Unsigned message BOC. Must be encoded with `base64`.
   * @param signature Signature. Must be encoded with `hex`.
   */
  def attachSignatureToMessageBody(
                                    abi: String,
                                    publicKey: String,
                                    message: String,
                                    signature: String
                                  ): Future[String] =
    context.requestField[String](
      "abi.attach_signature_to_message_body",
      s"""{
          |"abi":"$abi",
          |"public_key":"$publicKey",
          |"message":$message",
          |"signature":"$signature"}"""
    )("body")

  /**
   * Encodes an ABI-compatible message <p> Allows to encode deploy and function call messages, both signed and unsigned.<p> Use cases include messages of any possible type: - deploy with initial function call (i.e. `constructor` or any other function that is used for some kind of initialization); - deploy without initial function call; - signed/unsigned + data for signing. <p> `Signer` defines how the message should or shouldn't be signed:<p> `Signer::None` creates an unsigned message. This may be needed in case of some public methods,  that do not require authorization by pubkey. <p> `Signer::External` takes public key and returns `data_to_sign` for later signing.  Use `attach_signature` method with the result signature to get the signed message.<p> `Signer::Keys` creates a signed message with provided key pair. <p> <a target="_blank" href="SOON">SOON</a> `Signer::SigningBox` Allows using a special interface to imlepement signing  without private key disclosure to SDK. For instance, in case of using a cold wallet or HSM,  when application calls some API to sign data.
   *
   * @param abi                Contract ABI.
   * @param address            Target address the message will be sent to.
   *
   *                           Must be specified in case of non-deploy message.
   * @param deploySet          Deploy parameters.
   *
   *                           Must be specified in case of deploy message.
   * @param callSet            Function call parameters.
   *
   *                           Must be specified in case of non-deploy message.
   *
   *                           In case of deploy message it is optional and contains parameters
   *                           of the functions that will to be called upon deploy transaction.
   * @param signer             Signing parameters.
   * @param processingTryIndex Processing try index.
   *
   *                           Used in message processing with retries (if contract's ABI includes "expire" header).
   *
   *                           Encoder uses the provided try index to calculate message
   *                           expiration time. The 1st message expiration time is specified in
   *                           Client config.
   *
   *                           Expiration timeouts will grow with every retry.
   *                           Retry grow factor is set in Client config:
   *                           <.....add config parameter with default value here>
   *
   *                           Default value is 0.
   */
  def encodeMessage(
                     abi: String,
                     address: String,
                     deploySet: String,
                     callSet: String,
                     signer: String,
                     processingTryIndex: Number
                   ): Future[ResultOfEncodeMessage] =
    context.requestValue[ResultOfEncodeMessage](
      "abi.encode_message",
      s"""{
         |"abi":"$abi",
         |"address":"$address",
         |"deploy_set":"$deploySet",
         |"call_set":"$callSet",
         |"signer":"$signer",
         |"processing_try_index":$processingTryIndex
         |}""".stripMargin
    )

  /**
   * Combines `hex`-encoded `signature` with `base64`-encoded `unsigned_message`. Returns signed message encoded in `base64`.
   *
   * @param abi       Contract ABI
   * @param publicKey Public key encoded in `hex`.
   * @param message   Unsigned message BOC encoded in `base64`.
   * @param signature Signature encoded in `hex`.
   */
  def attachSignature(
                       abi: String,
                       publicKey: String,
                       message: String,
                       signature: String
                     ): Future[ResultOfAttachSignature] =
    context.requestValue[ResultOfAttachSignature](
      "abi.attach_signature",
      s"""{
         |"abi":"$abi",
         |"public_key":"$publicKey",
         |"message":"$message",
         |"signature":"$signature"}""".stripMargin
    )

  /**
   * Decodes message body using provided message BOC and ABI.
   *
   * @param abi     contract ABI
   * @param message Message BOC
   */
  def decodeMessage(
                     abi: String,
                     message: String
                   ): Future[DecodedMessageBody] =
    context.requestValue[DecodedMessageBody](
      "abi.decode_message",
      s"""{
        |"abi":"$abi",
        |"message":"$message"
        |}""".stripMargin
    )

  /**
   * Decodes message body using provided body BOC and ABI.
   *
   * @param abi        Contract ABI used to decode.
   * @param body       Message body BOC encoded in `base64`.
   * @param isInternal True if the body belongs to the internal message.
   */
  def decodeMessageBody(
                         abi: String,
                         body: String,
                         isInternal: Boolean
                       ): Future[DecodedMessageBody] =
    context.requestValue[DecodedMessageBody](
      "abi.decode_message_body",
      s"""{
         |"abi":"$abi",
         |"body":"$body",
         |"is_internal":$isInternal
         |}""".stripMargin
    )

  /**
   * Creates account state BOC<p> Creates account state provided with one of these sets of data : 1. BOC of code, BOC of data, BOC of library 2. TVC (string in `base64`), keys, init params
   *
   * @param stateInit   Source of the account state init.
   * @param balance     Initial balance.
   * @param lastTransLt Initial value for the `last_trans_lt`.
   * @param lastPaid    Initial value for the `last_paid`.
   */
  def encodeAccount(
                     stateInit: String,
                     balance: Long,
                     lastTransLt: Long,
                     lastPaid: Number
                   ): Future[ResultOfEncodeAccount] =
    context.requestValue[ResultOfEncodeAccount](
      "abi.encode_account",
      s"""{
         |"state_init":"$stateInit",
         |"balance":$balance,
         |"last_trans_lt":$lastTransLt,
         |"last_paid":$lastPaid
         |}""".stripMargin
    )
}

