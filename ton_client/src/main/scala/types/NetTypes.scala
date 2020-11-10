package types
import io.circe._
import io.circe.derivation._
import io.circe.syntax._

object NetTypes {
  type Value = String

  case class OrderBy(path: String, direction: SortDirection)

  sealed trait SortDirection

  case object ASC extends SortDirection

  case object DESC extends SortDirection

  case class ParamsOfQueryCollection(collection: String, filter: Option[Value], result: String, order: Option[List[OrderBy]], limit: Option[Long]) extends ApiNew {
    override type Out = ResultOfQueryCollection
    override val decoder: Decoder[ResultOfQueryCollection] = implicitly[Decoder[ResultOfQueryCollection]]
  }

  case class ResultOfQueryCollection(result: List[Value])

  case class ParamsOfWaitForCollection(collection: String, filter: Option[Value], result: String, timeout: Option[Long]) extends ApiNew {
    override type Out = ResultOfWaitForCollection
    override val decoder: Decoder[ResultOfWaitForCollection] = implicitly[Decoder[ResultOfWaitForCollection]]
  }

  case class ResultOfWaitForCollection(result: Value)

  case class ResultOfSubscribeCollection(handle: Long)

  case object unit

  case class ParamsOfSubscribeCollection(collection: String, filter: Option[Value], result: String) extends ApiNew {
    override type Out = ResultOfSubscribeCollection
    override val decoder: Decoder[ResultOfSubscribeCollection] = implicitly[Decoder[ResultOfSubscribeCollection]]
  }

  object OrderBy {
    implicit val OrderByEncoder: Encoder[OrderBy] = deriveEncoder[OrderBy]
  }

  // TODO check it
  object SortDirection {
    implicit val SortDirectionEncoder: Encoder[SortDirection] = {
      case ASC => "ASC".asJson
      case DESC => "DESC".asJson
    }
  }

  object ParamsOfQueryCollection {
    implicit val ParamsOfQueryCollectionEncoder: Encoder[ParamsOfQueryCollection] =
      deriveEncoder[ParamsOfQueryCollection]
  }

  object ResultOfQueryCollection {
    implicit val ResultOfQueryCollectionDecoder: Decoder[ResultOfQueryCollection] =
      deriveDecoder[ResultOfQueryCollection]
  }

  object ParamsOfWaitForCollection {
    implicit val ParamsOfWaitForCollectionEncoder: Encoder[ParamsOfWaitForCollection] =
      deriveEncoder[ParamsOfWaitForCollection]
  }

  object ResultOfWaitForCollection {
    implicit val ResultOfWaitForCollectionDecoder: Decoder[ResultOfWaitForCollection] =
      deriveDecoder[ResultOfWaitForCollection]
  }

  object ResultOfSubscribeCollection {
    implicit val ResultOfSubscribeCollectionCodec: Decoder[ResultOfSubscribeCollection] =
      deriveDecoder[ResultOfSubscribeCollection]
  }

  object ParamsOfSubscribeCollection {
    implicit val ParamsOfSubscribeCollectionEncoder: Encoder[ParamsOfSubscribeCollection] =
      deriveEncoder[ParamsOfSubscribeCollection]
  }

  // TODO were added

  case class ParamsOfUnsubscribeCollection(handle: Long) extends ApiNew {
    override type Out = Unit
    override val decoder: Decoder[Unit] = implicitly[Decoder[Unit]]
  }

  object ParamsOfUnsubscribeCollection {
    implicit val ParamsOfUnsubscribeCollectionEncoder: Encoder[ParamsOfUnsubscribeCollection] =
      deriveEncoder[ParamsOfUnsubscribeCollection]
  }
}
