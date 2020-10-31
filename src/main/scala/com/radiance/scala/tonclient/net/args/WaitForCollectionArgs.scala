package com.radiance.scala.tonclient.net.args

import com.radiance.scala.tonclient.Args
import io.circe.Encoder
import io.circe.derivation.deriveCodec

private[net] case class WaitForCollectionArgs(
                                               collection: String,
                                               filter: String,
                                               result: String,
                                               timeout: Long
                                             ) extends Args {
  override val functionName: String = "net.wait_for_collection"
  override val fieldName: Option[String] = Some("result")
}

private[net] object WaitForCollectionArgs {
  implicit val waitForCollectionArgsEncoder: Encoder[WaitForCollectionArgs] = deriveCodec[WaitForCollectionArgs]
}