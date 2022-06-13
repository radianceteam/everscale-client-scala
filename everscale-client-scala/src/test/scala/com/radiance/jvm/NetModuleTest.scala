package com.radiance.jvm

import com.radiance.jvm.net._
import io.circe._
import io.circe.parser._
import io.circe.Json._
import org.scalatest.flatspec.AnyFlatSpec
import cats.implicits._
import com.radiance.jvm.client._
import com.typesafe.scalalogging.Logger
import org.scalatest.matchers.should.Matchers._

class NetModuleTest extends AnyFlatSpec with TestBase {

  private val logger = Logger[NetModuleTest]

  override protected val config = ClientConfig(
    NetworkConfig("http://net.ton.dev/graphql".some).some
  )

  override def init(): Unit = {
    ctx = Context(config)
    netModule = new NetModule(ctx)
  }

  behavior.of("NetModule")

  // TODO fix it
  it should "execute simple query" ignore {
    val query =
      fromFields(Seq("last_paid" -> fromFields(Seq("in" -> fromValues(List(1601332024L, 1601331924L).map(fromLong))))))
    logger.info(s"Query:\n${query.spaces2}")
    val res = netModule
      .waitForCollection(
        "accounts",
        query.some,
        "id,last_paid",
        60000L.some
      )
      .get
    logger.info(s"Result:\n$res")
    res.result.hcursor.get[Long]("last_paid").get shouldBe 1601331924
  }

  // TODO fix it
  it should "execute 2nd simple query" ignore {
    val query = fromFields(
      Seq(
        "last_paid" -> fromFields(
          Seq("in" -> fromValues(List(1601332024L, 1601331924L, 1601332491L, 1601332679L).map(fromLong)))
        )
      )
    )
    logger.info(s"Query:\n${query.spaces2}")
    val res = netModule
      .queryCollection(
        "accounts",
        query.some,
        "acc_type,acc_type_name,balance,boc,id,last_paid,workchain_id",
        List(OrderBy("last_paid", SortDirectionEnum.ASC)).some,
        2L.some
      )
      .get
    logger.info(s"Result:\n$res")
    res.result.size shouldBe 2
  }

  it should "return the event" in {
    var eventsAcc: List[Json] = Nil
    val res = netModule
      .subscribeCollection(
        "transactions",
        None,
        "id account_addr",
        e => eventsAcc = e :: eventsAcc
      )
      .get
    logger.info("Handle: " + res.handle)
    Thread.sleep(5000)
    logger.info(eventsAcc.map(_.dropNullValues.spaces2).mkString("\n"))
    netModule.unsubscribe(res.handle).get
    logger.info("Unsubscribe successfully")
    // TODO find more accurate criteria
    assert(true)
  }

  it should "observe the collection" in {
    var eventsAcc: List[Json] = Nil
    val query =
      parse("""{"balance_delta":{"gt":"0x5f5e100"}}""").getOrElse(Json.Null)
    logger.info(s"Query:\n${query.spaces2}")

    val res = netModule
      .subscribeCollection(
        "transactions",
        query.some,
        "id,block_id,balance_delta",
        e => eventsAcc = e :: eventsAcc
      )
      .get
    logger.info("Handle: " + res.handle)
    netModule.suspend()
    Thread.sleep(5000)
    netModule.resume()
    logger.info(eventsAcc.map(_.dropNullValues.spaces2).mkString("\n"))
    netModule.unsubscribe(res.handle).get
    logger.info("Unsubscribe successfully")
    // TODO find more accurate criteria
    assert(true)
  }
}
