package com.radiance.scala.tonclient.types


import io.circe._
import io.circe.derivation._

case class OrderBy(
                    path: String,
                    direction: SortDirection
                  )

object OrderBy {
  implicit val orderByCodec: Codec[OrderBy] = deriveCodec[OrderBy]
}
