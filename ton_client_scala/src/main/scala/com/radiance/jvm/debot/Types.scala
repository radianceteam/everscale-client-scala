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
  // TODO implement it
}

case class DebotHandle(value: BigInt)

object DebotHandle {
  // TODO implement it
}

/**
  * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks
  * Called by debot engine to communicate with debot browser.
  */
sealed trait ParamsOfAppDebotBrowser

object ParamsOfAppDebotBrowser {

  /**
    * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks
    * Called by debot engine to communicate with debot browser.
    */
  case class Log(msg: String) extends ParamsOfAppDebotBrowser

  /**
    * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks
    * Called by debot engine to communicate with debot browser.
    */
  case class Switch(context_id: Long) extends ParamsOfAppDebotBrowser

  /**
    * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks
    * Called by debot engine to communicate with debot browser.
    */
  case class ShowAction(action: DebotAction) extends ParamsOfAppDebotBrowser

  /**
    * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks
    * Called by debot engine to communicate with debot browser.
    */
  case class Input(prompt: String) extends ParamsOfAppDebotBrowser

  /**
    * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks
    * Called by debot engine to communicate with debot browser.
    */
  case object GetSigningBox extends ParamsOfAppDebotBrowser

  /**
    * [UNSTABLE](UNSTABLE.md) Debot Browser callbacks
    * Called by debot engine to communicate with debot browser.
    */
  case class InvokeDebot(debot_addr: String, action: DebotAction)
      extends ParamsOfAppDebotBrowser

  // TODO add encoder
}

case class ParamsOfStart(address: String)

object ParamsOfStart {
  // TODO implement it
}

case class RegisteredDebot(debot_handle: DebotHandle)

object RegisteredDebot {
  // TODO implement it
}

sealed trait ResultOfAppDebotBrowser
object ResultOfAppDebotBrowser {
  case class Input(value: String) extends ResultOfAppDebotBrowser
  case class GetSigningBox(signing_box: SigningBoxHandle)
      extends ResultOfAppDebotBrowser
  case object InvokeDebot extends ResultOfAppDebotBrowser
  case class ParamsOfFetch(address: String)

  case class ParamsOfExecute(debot_handle: DebotHandle, action: DebotAction) extends Bind {
    override type Out = Unit
    override val decoder: Decoder[Unit] = implicitly[Decoder[Unit]]
  }

  object ParamsOfExecute {
    implicit val encoder: Encoder[ParamsOfExecute] = deriveEncoder[ParamsOfExecute]
  }

  // TODO add decoder
}
