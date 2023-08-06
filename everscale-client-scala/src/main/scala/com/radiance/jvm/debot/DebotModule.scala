package com.radiance.jvm.debot

import com.radiance.jvm._
import com.radiance.jvm.app.AppDebotBrowser

import scala.concurrent.Future

class DebotModule(private val ctx: Context) {

  /**
   * [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Executes debot action. Calls debot engine referenced by debot
   * handle to execute input action. Calls Debot Browser Callbacks if needed.
   *
   * # Remarks Chain of actions can be executed if input action generates a list of subactions.
   *
   * @param debot_handle
   *   debot_handle
   * @param action
   *   action
   */
  def execute(debot_handle: DebotHandle, action: DebotAction): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[ParamsOfExecute, Unit](
      "debot.execute",
      ParamsOfExecute(debot_handle, action)
    )
  }

  /**
   * [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Fetches DeBot metadata from blockchain. Downloads DeBot from
   * blockchain and creates and fetches its metadata.
   *
   * @param address
   *   address
   */
  def fetch(address: String): Future[Either[Throwable, ResultOfFetch]] = {
    ctx.execAsync[ParamsOfFetch, ResultOfFetch](
      "debot.fetch",
      ParamsOfFetch(address)
    )
  }

  /**
   * [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Creates and instance of DeBot. Downloads debot smart contract
   * (code and data) from blockchain and creates an instance of Debot Engine for it.
   *
   * # Remarks It does not switch debot to context 0. Browser Callbacks are not called.
   *
   * @param address
   *   address
   * @param app_object
   *   app_object
   */
  def init(
    address: String,
    app_object: AppDebotBrowser
  ): Future[Either[Throwable, RegisteredDebot]] = {
    ctx.registerAppObject[
      RegisteredDebot,
      ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser,
      ResultOfAppDebotBrowserADT.ResultOfAppDebotBrowser
    ](
      "debot.init",
      address,
      app_object
    )
  }

  /**
   * [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Destroys debot handle. Removes handle from Client Context and
   * drops debot engine referenced by that handle.
   *
   * @param debot_handle
   *   debot_handle
   */
  def remove(debot_handle: DebotHandle): Future[Either[Throwable, Unit]] = {
    ctx.unregisterAppObject(debot_handle.value.toInt, "debot.remove")
  }

  /**
   * [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Sends message to Debot. Used by Debot Browser to send response
   * on Dinterface call or from other Debots.
   *
   * @param debot_handle
   *   debot_handle
   * @param message
   *   message
   */
  def send(debot_handle: DebotHandle, message: String): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[ParamsOfSend, Unit](
      "debot.send",
      ParamsOfSend(debot_handle, message)
    )
  }

  /**
   * [UNSTABLE](UNSTABLE.md) [DEPRECATED](DEPRECATED.md) Starts the DeBot. Downloads debot smart contract from
   * blockchain and switches it to context zero.
   *
   * This function must be used by Debot Browser to start a dialog with debot. While the function is executing, several
   * Browser Callbacks can be called, since the debot tries to display all actions from the context 0 to the user.
   *
   * When the debot starts SDK registers `BrowserCallbacks` AppObject. Therefore when `debote.remove` is called the
   * debot is being deleted and the callback is called with `finish`=`true` which indicates that it will never be used
   * again.
   *
   * @param debot_handle
   *   debot_handle
   */
  def start(debot_handle: DebotHandle): Future[Either[Throwable, Unit]] = {
    ctx.execAsync[ParamsOfStart, Unit](
      "debot.start",
      ParamsOfStart(debot_handle)
    )
  }
}
