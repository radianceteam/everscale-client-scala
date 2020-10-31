package com.radiance.scala.tonclient.types.in

import io.circe._

sealed trait SortDirection

case object ASC extends SortDirection
case object DESC extends SortDirection

object SortDirection {
  implicit val sortDirectionEncoder: Encoder[SortDirection] = {
    case ASC => Json.fromString("asc")
    case DESC => Json.fromString("desc")
  }
}



