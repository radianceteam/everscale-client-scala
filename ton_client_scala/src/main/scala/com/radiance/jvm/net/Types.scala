package com.radiance.jvm.net

import com.radiance.jvm._
import io.circe._
import io.circe.derivation._

case class EndpointsSet(endpoints: List[String])

object EndpointsSet {
  implicit val codec: Codec[EndpointsSet] = deriveCodec[EndpointsSet]
}

sealed trait NetErrorCode {
  val code: String
}

object NetErrorCode {

  case object QueryFailed extends NetErrorCode {
    override val code: String = "601"
  }

  case object SubscribeFailed extends NetErrorCode {
    override val code: String = "602"
  }

  case object WaitForFailed extends NetErrorCode {
    override val code: String = "603"
  }

  case object GetSubscriptionResultFailed extends NetErrorCode {
    override val code: String = "604"
  }

  case object InvalidServerResponse extends NetErrorCode {
    override val code: String = "605"
  }

  case object ClockOutOfSync extends NetErrorCode {
    override val code: String = "606"
  }

  case object WaitForTimeout extends NetErrorCode {
    override val code: String = "607"
  }

  case object GraphqlError extends NetErrorCode {
    override val code: String = "608"
  }

  case object NetworkModuleSuspended extends NetErrorCode {
    override val code: String = "609"
  }

  case object WebsocketDisconnected extends NetErrorCode {
    override val code: String = "610"
  }

  case object NotSupported extends NetErrorCode {
    override val code: String = "611"
  }

  case object NoEndpointsProvided extends NetErrorCode {
    override val code: String = "612"
  }

}

case class OrderBy(path: String, direction: SortDirection)

object OrderBy {
  implicit val encoder: Encoder[OrderBy] = deriveEncoder[OrderBy]
}

case class ParamsOfFindLastShardBlock(address: String) extends Bind {
  override type Out = ResultOfFindLastShardBlock
  override val decoder: Decoder[ResultOfFindLastShardBlock] =
    implicitly[Decoder[ResultOfFindLastShardBlock]]
}

object ParamsOfFindLastShardBlock {
  implicit val encoder: Encoder[ParamsOfFindLastShardBlock] =
    deriveEncoder[ParamsOfFindLastShardBlock]
}

case class ParamsOfQuery(query: String, variables: Option[Value]) extends Bind {
  override type Out = ResultOfQuery
  override val decoder: Decoder[ResultOfQuery] =
    implicitly[Decoder[ResultOfQuery]]
}

object ParamsOfQuery {
  implicit val encoder: Encoder[ParamsOfQuery] = deriveEncoder[ParamsOfQuery]
}

case class ParamsOfQueryCollection(
    collection: String,
    filter: Option[Value],
    result: String,
    order: Option[List[OrderBy]],
    limit: Option[Long]
) extends Bind {
  override type Out = ResultOfQueryCollection
  override val decoder: Decoder[ResultOfQueryCollection] =
    implicitly[Decoder[ResultOfQueryCollection]]
}

object ParamsOfQueryCollection {
  implicit val encoder: Encoder[ParamsOfQueryCollection] =
    deriveEncoder[ParamsOfQueryCollection]
}

case class ParamsOfSubscribeCollection(
    collection: String,
    filter: Option[Value],
    result: String
) extends Bind {
  override type Out = ResultOfSubscribeCollection
  override val decoder: Decoder[ResultOfSubscribeCollection] =
    implicitly[Decoder[ResultOfSubscribeCollection]]
}

object ParamsOfSubscribeCollection {
  implicit val encoder: Encoder[ParamsOfSubscribeCollection] =
    deriveEncoder[ParamsOfSubscribeCollection]
}

// TODO were added
case class ParamsOfUnsubscribeCollection(handle: Long) extends Bind {
  override type Out = Unit
  override val decoder: Decoder[Unit] = implicitly[Decoder[Unit]]
}

object ParamsOfUnsubscribeCollection {
  implicit val encoder: Encoder[ParamsOfUnsubscribeCollection] =
    deriveEncoder[ParamsOfUnsubscribeCollection]
}

case class ParamsOfWaitForCollection(
    collection: String,
    filter: Option[Value],
    result: String,
    timeout: Option[Long]
) extends Bind {
  override type Out = ResultOfWaitForCollection
  override val decoder: Decoder[ResultOfWaitForCollection] =
    implicitly[Decoder[ResultOfWaitForCollection]]
}

object ParamsOfWaitForCollection {
  implicit val encoder: Encoder[ParamsOfWaitForCollection] =
    deriveEncoder[ParamsOfWaitForCollection]
}

case class ResultOfFindLastShardBlock(block_id: String)

object ResultOfFindLastShardBlock {
  implicit val decoder: Decoder[ResultOfFindLastShardBlock] =
    deriveDecoder[ResultOfFindLastShardBlock]
}

case class ResultOfQuery(result: Value)

object ResultOfQuery {
  implicit val decoder: Decoder[ResultOfQuery] = deriveDecoder[ResultOfQuery]
}

case class ResultOfQueryCollection(result: List[Value])

object ResultOfQueryCollection {
  implicit val decoder: Decoder[ResultOfQueryCollection] =
    deriveDecoder[ResultOfQueryCollection]
}

case class ResultOfSubscribeCollection(handle: Long)

object ResultOfSubscribeCollection {
  implicit val decoder: Decoder[ResultOfSubscribeCollection] =
    deriveDecoder[ResultOfSubscribeCollection]
}

case class ResultOfWaitForCollection(result: Value)

object ResultOfWaitForCollection {
  implicit val decoder: Decoder[ResultOfWaitForCollection] =
    deriveDecoder[ResultOfWaitForCollection]
}

sealed trait SortDirection

object SortDirection {
  import io.circe.Json._
  case object ASC extends SortDirection
  case object DESC extends SortDirection

  implicit val encoder: Encoder[SortDirection] = {
    case ASC  => fromString("ASC")
    case DESC => fromString("DESC")
  }
}
