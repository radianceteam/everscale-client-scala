package com.radiance.jvm.debot

import com.radiance.jvm._
import com.radiance.jvm.crypto._
import io.circe.derivation._
import io.circe._

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

  case object DebotStartFailed extends DebotErrorCode {
    override val code: String = "801"
  }

  case object DebotFetchFailed extends DebotErrorCode {
    override val code: String = "802"
  }

  case object DebotExecutionFailed extends DebotErrorCode {
    override val code: String = "803"
  }

  case object DebotInvalidHandle extends DebotErrorCode {
    override val code: String = "804"
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

  // TODO add encoder
}

case class ParamsOfStart(address: String) extends Bind {
  override type Out = RegisteredDebot
  override val decoder: Decoder[RegisteredDebot] =
    implicitly[Decoder[RegisteredDebot]]
}

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

  case class ParamsOfFetch(address: String) extends Bind {
    override type Out = RegisteredDebot
    override val decoder: Decoder[RegisteredDebot] =
      implicitly[Decoder[RegisteredDebot]]
  }

  object ParamsOfFetch {
    implicit val encoder: Encoder[ParamsOfFetch] = deriveEncoder[ParamsOfFetch]
  }

  case class ParamsOfExecute(debot_handle: DebotHandle, action: DebotAction) extends Bind {
    override type Out = Unit
    override val decoder: Decoder[Unit] = implicitly[Decoder[Unit]]
  }

  object ParamsOfExecute {
    implicit val encoder: Encoder[ParamsOfExecute] =
      deriveEncoder[ParamsOfExecute]
  }

  // TODO add decoder
}
