package types

import io.circe.Decoder

trait ApiNew {
  type Out
  val decoder: Decoder[Out]
}
