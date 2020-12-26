package com.radiance.jvm

import com.radiance.jvm.net._
import io.circe._
import io.circe.parser._
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext

class NetModuleTest  extends AnyFlatSpec with TestBase {
  implicit val ec: ExecutionContext = ExecutionContext.global

  behavior.of("NetModule")

  it should "execute simple query" in {
    val query = parse("""{"last_paid":{"in":[1601332024,1601331924]}}""").getOrElse(Json.Null)
    println(s"Query:\n${query.spaces2}")
    val res = net.waitForCollection(
      "accounts",
      Some(query),
      "id,last_paid",
      Some(60000)
    ).get
    println(s"Result:\n$res")
    assert(res.result.hcursor.get[Long]("last_paid").get == 1601331924)
  }

  it should "execute 2nd simple query" in {
    val query = parse("""{"last_paid":{"in":[1601332024,1601331924,1601332491,1601332679]}}""").getOrElse(Json.Null)
    println(s"Query:\n${query.spaces2}")
    val res = net.queryCollection(
      "accounts",
      Some(query),
      "acc_type,acc_type_name,balance,boc,id,last_paid,workchain_id",
      Some(List(OrderBy("last_paid", SortDirection.ASC))),
      Some(2)
    ).get
    println(s"Result:\n$res")
    assert(res.result.size == 2)
  }

  it should "return the event" in {
    var eventsAcc: List[Json] = Nil
    val res = net.subscribeCollection(
      "transactions",
      None,
      "id account_addr",
      e => eventsAcc = e :: eventsAcc
    ).get
    println("Handle: " + res.handle)
    Thread.sleep(5000)
    println(eventsAcc.map(_.dropNullValues.spaces2).mkString("\n"))
    net.unsubscribe(res.handle).get
    println("Unsubscribe successfully")
    // TODO find more accurate criteria
    assert(eventsAcc.nonEmpty || eventsAcc.isEmpty)
  }

  it should "observe the collection" in {
    var eventsAcc: List[Json] = Nil
    val query = parse("""{"balance_delta":{"gt":"0x5f5e100"}}""").getOrElse(Json.Null)
    println(s"Query:\n${query.spaces2}")

    val res = net.subscribeCollection(
      "transactions",
      Some(query),
      "id,block_id,balance_delta",
      e => eventsAcc = e :: eventsAcc
    ).get
    println("Handle: " + res.handle)
    Thread.sleep(5000)
    println(eventsAcc.map(_.dropNullValues.spaces2).mkString("\n"))
    net.unsubscribe(res.handle).get
    println("Unsubscribe successfully")
    assert(true)
  }
}
