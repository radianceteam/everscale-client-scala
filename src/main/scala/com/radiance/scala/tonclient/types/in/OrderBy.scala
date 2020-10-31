package com.radiance.scala.tonclient.types.in

import io.circe._
import io.circe.derivation._

case class OrderBy(
                    path: String,
                    direction: SortDirection
                  )

object OrderBy {
  implicit val orderByEncoder: Encoder[OrderBy] = deriveEncoder[OrderBy]
}
