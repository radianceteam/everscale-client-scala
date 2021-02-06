package com.radiance.jvm.debot

import com.radiance.jvm.Utils.generateType
import com.radiance.jvm.crypto._
import io.circe.derivation._
import io.circe._
import io.circe.syntax._
import io.circe.Json._

case class DebotAction(
  description: String,
  name: String,
  action_type: Long,
  to: Long,
  attributes: String,
  misc: String
)

object DebotAction {
  implicit val encoder: Encoder[DebotAction] = deriveEncoder[DebotAction]
}

sealed trait DebotErrorCode {
  val code: String
}

object DebotErrorCode {

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
  implicit val codec: Codec[DebotHandle] = deriveCodec[DebotHandle]
}

/**
 * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
 */
sealed trait ParamsOfAppDebotBrowser

object ParamsOfAppDebotBrowser {

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Log(msg: String) extends ParamsOfAppDebotBrowser

  object Log {
    implicit val encoder: Encoder[Log] = deriveEncoder[Log]
  }

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Send(message: String) extends ParamsOfAppDebotBrowser

  object Send {
    implicit val encoder: Encoder[Send] = deriveEncoder[Send]
  }

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Switch(context_id: Long) extends ParamsOfAppDebotBrowser

  object Switch {
    implicit val encoder: Encoder[Switch] = deriveEncoder[Switch]
  }

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class ShowAction(action: DebotAction) extends ParamsOfAppDebotBrowser

  object ShowAction {
    implicit val encoder: Encoder[ShowAction] = deriveEncoder[ShowAction]
  }

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class Input(prompt: String) extends ParamsOfAppDebotBrowser

  object Input {
    implicit val encoder: Encoder[Input] = deriveEncoder[Input]
  }

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case object GetSigningBox extends ParamsOfAppDebotBrowser

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case class InvokeDebot(debot_addr: String, action: DebotAction) extends ParamsOfAppDebotBrowser

  object InvokeDebot {
    implicit val encoder: Encoder[InvokeDebot] = deriveEncoder[InvokeDebot]
  }

  /**
   * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks Called by debot engine to communicate with debot browser.
   */
  case object SwitchCompleted extends ParamsOfAppDebotBrowser

  implicit val encoder: Encoder[ParamsOfAppDebotBrowser] = {
    case a: Log          => a.asJson.deepMerge(generateType(a))
    case a: Send         => a.asJson.deepMerge(generateType(a))
    case a: Switch       => a.asJson.deepMerge(generateType(a))
    case a: ShowAction   => a.asJson.deepMerge(generateType(a))
    case a: Input        => a.asJson.deepMerge(generateType(a))
    case GetSigningBox   => fromFields(Seq("type" -> fromString("GetSigningBox")))
    case a: InvokeDebot  => a.asJson.deepMerge(generateType(a))
    case SwitchCompleted => fromFields(Seq("type" -> fromString("SwitchCompleted")))
  }
}

case class ParamsOfStart(address: String)

object ParamsOfStart {
  implicit val encoder: Encoder[ParamsOfStart] = deriveEncoder[ParamsOfStart]
}

case class RegisteredDebot(debot_handle: DebotHandle)

object RegisteredDebot {
  implicit val codec: Codec[RegisteredDebot] = deriveCodec[RegisteredDebot]
}

sealed trait ResultOfAppDebotBrowser

object ResultOfAppDebotBrowser {
  case class Input(value: String) extends ResultOfAppDebotBrowser
  case class GetSigningBox(signing_box: SigningBoxHandle) extends ResultOfAppDebotBrowser
  case object InvokeDebot extends ResultOfAppDebotBrowser

  case class ParamsOfFetch(address: String)

  object ParamsOfFetch {
    implicit val encoder: Encoder[ParamsOfFetch] = deriveEncoder[ParamsOfFetch]
  }

  /**
   * [UNSTABLE](UNSTABLE.md) Parameters of `send` function.
   */
  case class ParamsOfSend(debot_handle: DebotHandle, source: String, func_id: Long, params: String)

  object ParamsOfSend {
    implicit val encoder: Encoder[ParamsOfSend] = deriveEncoder[ParamsOfSend]
  }

  case class ParamsOfExecute(debot_handle: DebotHandle, action: DebotAction)

  object ParamsOfExecute {
    implicit val encoder: Encoder[ParamsOfExecute] =
      deriveEncoder[ParamsOfExecute]
  }

}
