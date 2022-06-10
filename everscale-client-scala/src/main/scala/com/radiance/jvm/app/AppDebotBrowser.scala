package com.radiance.jvm.app

import com.radiance.jvm.crypto.SigningBoxHandle
import com.radiance.jvm.debot.{DebotAction, DebotActivityADT, ParamsOfAppDebotBrowserADT, ResultOfAppDebotBrowserADT}

import scala.concurrent.ExecutionContext

abstract class AppDebotBrowser()(implicit
  ec: ExecutionContext
) extends AppObject[
      ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser,
      ResultOfAppDebotBrowserADT.ResultOfAppDebotBrowser
    ] {

  override protected val f
    : ParamsOfAppDebotBrowserADT.ParamsOfAppDebotBrowser => ResultOfAppDebotBrowserADT.ResultOfAppDebotBrowser = {

    case ParamsOfAppDebotBrowserADT.Approve(activity: DebotActivityADT.DebotActivity) =>
      ResultOfAppDebotBrowserADT.Approve(approve(activity))

    case ParamsOfAppDebotBrowserADT.GetSigningBox =>
      ResultOfAppDebotBrowserADT.GetSigningBox(getSigningBox)

    case ParamsOfAppDebotBrowserADT.Input(prompt) =>
      ResultOfAppDebotBrowserADT.Input(input(prompt))

    case ParamsOfAppDebotBrowserADT.InvokeDebot(debot_addr: String, action: DebotAction) =>
      invokeDebot(debot_addr: String, action: DebotAction)
      ResultOfAppDebotBrowserADT.InvokeDebot

    case ParamsOfAppDebotBrowserADT.Log(msg) =>
      log(msg)
      ResultOfAppDebotBrowserADT.InvokeDebot

    case ParamsOfAppDebotBrowserADT.Send(message: String) =>
      send(message)
      ResultOfAppDebotBrowserADT.InvokeDebot

    case ParamsOfAppDebotBrowserADT.ShowAction(action: DebotAction) =>
      showAction(action)
      ResultOfAppDebotBrowserADT.InvokeDebot

    case ParamsOfAppDebotBrowserADT.Switch(context_id: Long) =>
      switchTo(context_id)
      ResultOfAppDebotBrowserADT.InvokeDebot

    case ParamsOfAppDebotBrowserADT.SwitchCompleted =>
      switchCompleted()
      ResultOfAppDebotBrowserADT.InvokeDebot
  }

  def log(msg: String): Unit

  def switchTo(contextId: Long): Unit

  def switchCompleted(): Unit

  def showAction(action: DebotAction): Unit

  def input(prompt: String): String

  def getSigningBox: SigningBoxHandle

  def invokeDebot(debotAddr: String, action: DebotAction): Unit

  def send(message: String): Unit

  def approve(activity: DebotActivityADT.DebotActivity): Boolean
}
