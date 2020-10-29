package com.radiance.scala.tonclient

import java.io._


import io.circe.Json
import io.circe._
import io.circe.parser._

import scala.collection.mutable
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.Success

object TONContext {
  @native
  private def createContext(config: String): String
  @native
  private def destroyContext(context: Int): Unit
  @native
  private def request(context: Int, functionName: String, params: String, requestId: Int): Unit
  @native
  private def requestSync(context: Int, functionName: String, params: String)

  private var requestCount = 0
  private val responses =  mutable.Map[Int, Promise[String]]()

  try {
    System.load(createTempDll("/tonclient.dll"))
  } catch {
    case e: IOException =>
      throw new RuntimeException(e)
  }

  @throws[IOException]
  private def createTempDll(fileName: String) = {
    val inputStream = classOf[TONContext].getResourceAsStream(fileName)
    if (inputStream == null) throw new IOException("Cannot find resource '" + fileName + "'")
    val tempDll = File.createTempFile("TONLibrary", ".dll")
    val outputStream = new FileOutputStream(tempDll)
    val array = new Array[Byte](8192)
    var i = inputStream.read(array)
    while ( { i != -1 }) {
      outputStream.write(array, 0, i)
      i = inputStream.read(array)
    }
    inputStream.close()
    outputStream.close()
    tempDll.deleteOnExit()
    tempDll.getAbsolutePath
  }


  def toNumber(value: String): Number = if (value.indexOf('.') < 0) if (value.length > 6) java.lang.Long.valueOf(value)
  else Integer.valueOf(value)
  else if (value.length > 7) java.lang.Double.valueOf(value)
  else java.lang.Float.valueOf(value)

  private def responseHandler(id: Int, params: String, `type`: Int, finished: Boolean): Unit = {
    this.synchronized({
        responses.remove(id).map(promise => {
          if (`type` == 1) {
            val cursor = parse(params).getOrElse(Json.Null).hcursor
            val code: Int = cursor.downField("code").as[Int].getOrElse(-1)
            val message = cursor.downField("message").as[String].getOrElse("")
            promise.failure(new RuntimeException(s"Code: $code; Message: $message"))
          } else {
            promise.complete(Success(params))
          }
        })
      }
    )
  }

  @throws[Exception]
  def create(config: String): TONContext = {
    val result = createContext(config)
    val cursor = parse(result).getOrElse(Json.Null).hcursor
    cursor.downField("error").success.map(e => {
      val code = e.downField("code").as[Int]
      val message = e.downField("message").as[String]
      throw new RuntimeException(s"Code: $code; Message: $message")
    }).getOrElse({
      cursor.downField("result").as[Int].map(i => new TONContext(i)).fold(
        t => throw new RuntimeException(t),
        r => r
      )
    })
  }

  def main(args: String*): Unit = {
    val ctx = TONContext.create("{}")
    val crypto = new Crypto(ctx)(ExecutionContext.global)
    //println(util.Arrays.asList(crypto.factorize("EE").get))
    println(Await.result(ctx.request("client.version", ""), 1.second))
  }
}

class TONContext private(var contextId: Int) {
  implicit val ec: ExecutionContext = ExecutionContext.global

  def destroy(): Unit = {
    if (contextId >= 0) {
      TONContext.destroyContext(contextId)
      contextId = -1
    }
  }

  def request(functionName: String, params: String): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    this.synchronized({
      TONContext.requestCount += 1
      val id = TONContext.requestCount
      TONContext.responses.put(id, promise)
      TONContext.request(contextId, functionName, params, id)
    })
    promise.future
  }

//  def requestJSON(functionName: String, params: String): Future[Json] =
//    request(functionName, params).map(r => parse(r).fold(
//      t => throw new RuntimeException(t),
//      a => a
//    ))

  def requestField[T : Decoder](functionName: String, params: String)(fieldName: String): Future[T] =
    request(functionName, params).map(r => parse(r).fold(
      t => throw new RuntimeException(t),
      a => a.hcursor.get[T](fieldName).fold(
        t => throw new RuntimeException(t),
        r => r
      )
    ))

  def requestValue[T : Codec](functionName: String, params: String): Future[T] =
    request(functionName, params).map(r => parse(r).map(_.as[T]).fold(
      t => throw new RuntimeException(t),
      r => r.fold(
        t => throw new RuntimeException(t),
        a => a
      )
    ))

}
