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

object DebotErrorCodeEnum {

  sealed trait DebotErrorCode {
    val code: String
  }

  case object DebotExecutionFailed extends DebotErrorCode {
    override val code: String = "803"
  }
  case object DebotFetchFailed extends DebotErrorCode {
    override val code: String = "802"
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
  case object DebotStartFailed extends DebotErrorCode {
    override val code: String = "801"
  }
}

case class DebotHandle(value: BigInt)

object DebotHandle {
  implicit val codec: Codec[DebotHandle] = derivation.deriveCodec[DebotHandle]
}

object ParamsOfAppDebotBrowserADT {

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  sealed trait ParamsOfAppDebotBrowser

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
  case class Switch(context_id: Long) extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class ShowAction(action: DebotAction) extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Input(prompt: String) extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case object GetSigningBox extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class InvokeDebot(debot_addr: String, action: DebotAction) extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case object SwitchCompleted extends ParamsOfAppDebotBrowser

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Encoder[ParamsOfAppDebotBrowser] =
    extras.semiauto.deriveConfiguredEncoder[ParamsOfAppDebotBrowser]
}

case class ParamsOfStart(address: String)

object ParamsOfStart {
  implicit val encoder: Encoder[ParamsOfStart] = derivation.deriveEncoder[ParamsOfStart]
}

case class RegisteredDebot(debot_handle: DebotHandle)

object RegisteredDebot {
  implicit val codec: Codec[RegisteredDebot] = derivation.deriveCodec[RegisteredDebot]
}

object ResultOfAppDebotBrowserADT {

  sealed trait ResultOfAppDebotBrowser

  case class Input(value: String) extends ResultOfAppDebotBrowser

  case class GetSigningBox(signing_box: SigningBoxHandle) extends ResultOfAppDebotBrowser

  case object InvokeDebot extends ResultOfAppDebotBrowser

  case class ParamsOfFetch(address: String)

  /**
   * [UNSTABLE](UNSTABLE.md) Parameters of `send` function.
   */
  case class ParamsOfSend(debot_handle: DebotHandle, source: String, func_id: Long, params: String)

  case class ParamsOfExecute(debot_handle: DebotHandle, action: DebotAction)

  import com.radiance.jvm.DiscriminatorConfig._
  implicit val encoder: Decoder[ResultOfAppDebotBrowser] =
    extras.semiauto.deriveConfiguredDecoder[ResultOfAppDebotBrowser]

}
