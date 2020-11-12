package com.radiance.scala.methods

import com.radiance.scala.types.AbiTypes.{Abi, AbiContract, Serialized}
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec

import scala.io.Source
import io.circe.parser._
import io.circe.syntax._

trait Config extends BeforeAndAfter { this: AnyFlatSpec =>
  import TestDecoders._

  private val eventsAbi: Abi = {
    val contract = parse(Source.fromResource("Events.abi.json").mkString).fold(
      t => throw t,
      r => r.as[AbiContract].fold(
        t => throw t,
        p => p
      )
    )
    Serialized(contract)
  }

  before {
    println(eventsAbi)
  }

}
