package com.radiance.jvm.debot

import com.radiance.jvm.crypto._
import io.circe._
import io.circe.generic.extras

case class DebotAction(
  description: String,
  name: String,
  action_type: Long,
  to: Long,
  attributes: String,
  misc: String
)

object DebotAction {
  implicit val encoder: Encoder[DebotAction] = derivation.deriveEncoder[DebotAction]
}

object DebotActivityADT {

  /**
   * [UNSTABLE](UNSTABLE.md) Describes the operation that the DeBot wants to perform.
   */
  sealed trait DebotActivity

  /**
   * [UNSTABLE](UNSTABLE.md) Describes the operation that the DeBot wants to perform.
   */
  case class Transaction(msg: String, dst: String, out: List[Spending], fee: BigInt, setcode: Boolean, signkey: String, signing_box_handle: Long) extends DebotActivity

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[DebotActivity] =
    extras.semiauto.deriveConfiguredEncoder[DebotActivity]
}

object DebotErrorCodeEnum {
  sealed trait DebotErrorCode {
    val code: String
  }

  case object DebotBrowserCallbackFailed extends DebotErrorCode {
    override val code: String = "811"
  }

  case object DebotExecutionFailed extends DebotErrorCode {
    override val code: String = "803"
  }

  case object DebotExternalCallFailed extends DebotErrorCode {
    override val code: String = "810"
  }

  case object DebotFetchFailed extends DebotErrorCode {
    override val code: String = "802"
  }

  case object DebotGetMethodFailed extends DebotErrorCode {
    override val code: String = "808"
  }

  case object DebotInvalidAbi extends DebotErrorCode {
    override val code: String = "807"
  }

  case object DebotInvalidFunctionId extends DebotErrorCode {
    override val code: String = "806"
  }

  case object DebotInvalidHandle extends DebotErrorCode {
    override val code: String = "804"
  }

  case object DebotInvalidJsonParams extends DebotErrorCode {
    override val code: String = "805"
  }

  case object DebotInvalidMsg extends DebotErrorCode {
    override val code: String = "809"
  }

  case object DebotOperationRejected extends DebotErrorCode {
    override val code: String = "812"
  }

  case object DebotStartFailed extends DebotErrorCode {
    override val code: String = "801"
  }
}

case class DebotHandle(value: BigInt) extends AnyVal

object DebotHandle {
  implicit val decoder: Decoder[DebotHandle] = Decoder.instance(c => c.value.as[BigInt].map(DebotHandle(_)))
  implicit val encoder: Encoder[DebotHandle] = Encoder.instance(a => Json.fromBigInt(a.value))
}

/**
 * [UNSTABLE](UNSTABLE.md) Describes DeBot metadata.
 */
case class DebotInfo(
  name: Option[String],
  version: Option[String],
  publisher: Option[String],
  caption: Option[String],
  author: Option[String],
  support: Option[String],
  hello: Option[String],
  language: Option[String],
  dabi: Option[String],
  icon: Option[String],
  interfaces: List[String]
)

object DebotInfo {
  implicit val encoder: Encoder[DebotInfo] = derivation.deriveEncoder[DebotInfo]
}

object ParamsOfAppDebotBrowserADT {

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  sealed trait ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Approve(activity: DebotActivityADT.DebotActivity) extends ParamsOfAppDebotBrowser

  /**
   * Get signing box to sign data. Signing box returned is owned and disposed by debot engine
   */
  case object GetSigningBox extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Input(prompt: String) extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class InvokeDebot(debot_addr: String, action: DebotAction) extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Log(msg: String) extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Send(message: String) extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class ShowAction(action: DebotAction) extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Switch(context_id: Long) extends ParamsOfAppDebotBrowser

  /**
   * Notify browser that all context actions are shown.
   */
  case object SwitchCompleted extends ParamsOfAppDebotBrowser

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[ParamsOfAppDebotBrowser] =
    extras.semiauto.deriveConfiguredEncoder[ParamsOfAppDebotBrowser]
}

/**
 * [UNSTABLE](UNSTABLE.md) Parameters for executing debot action.
 */
case class ParamsOfExecute(debot_handle: DebotHandle, action: DebotAction)

/**
 * [UNSTABLE](UNSTABLE.md) Parameters to fetch DeBot metadata.
 */
case class ParamsOfFetch(address: String)

/**
 * [UNSTABLE](UNSTABLE.md) Parameters to init DeBot.
 */
case class ParamsOfInit(address: String)

/**
 * [UNSTABLE](UNSTABLE.md)
 */
case class ParamsOfRemove(debot_handle: DebotHandle)

/**
 * [UNSTABLE](UNSTABLE.md) Parameters of `send` function.
 */
case class ParamsOfSend(debot_handle: DebotHandle, message: String)

/**
 * [UNSTABLE](UNSTABLE.md) Parameters to start DeBot. DeBot must be already initialized with init() function.
 */
case class ParamsOfStart(debot_handle: DebotHandle)

/**
 * [UNSTABLE](UNSTABLE.md) Structure for storing debot handle returned from `init` function.
 */
case class RegisteredDebot(debot_handle: DebotHandle, debot_abi: String, info: DebotInfo)
object ResultOfAppDebotBrowserADT {

  /**
   * [UNSTABLE](UNSTABLE.md) Returning values from Debot Browser callbacks.
   */
  sealed trait ResultOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Returning values from Debot Browser callbacks.
   */
  case class Approve(approved: Boolean) extends ResultOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Returning values from Debot Browser callbacks.
   */
  case class GetSigningBox(signing_box: SigningBoxHandle) extends ResultOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Returning values from Debot Browser callbacks.
   */
  case class Input(value: String) extends ResultOfAppDebotBrowser

  /**
   * Result of debot invoking.
   */
  case object InvokeDebot extends ResultOfAppDebotBrowser

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val decoder: Decoder[ResultOfAppDebotBrowser] =
    extras.semiauto.deriveConfiguredDecoder[ResultOfAppDebotBrowser]
}

/**
 * [UNSTABLE](UNSTABLE.md)
 */
case class ResultOfFetch(info: DebotInfo)

/**
 * [UNSTABLE](UNSTABLE.md) Describes how much funds will be debited from the target contract balance as a result of the
 * transaction.
 */
case class Spending(amount: BigInt, dst: String)

object Spending {
  implicit val encoder: Encoder[Spending] = derivation.deriveEncoder[Spending]
}
