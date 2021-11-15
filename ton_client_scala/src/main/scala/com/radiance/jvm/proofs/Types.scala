package com.radiance.jvm.proofs

import com.radiance.jvm.Value
import io.circe.Encoder
import io.circe.derivation.deriveEncoder

case class ParamsOfProofBlockData(block: Value)

object ParamsOfProofBlockData {
  implicit val encoder: Encoder[ParamsOfProofBlockData] = deriveEncoder[ParamsOfProofBlockData]
}

case class ParamsOfProofTransactionData(transaction: Value)

object ParamsOfProofTransactionData {
  implicit val encoder: Encoder[ParamsOfProofTransactionData] = deriveEncoder[ParamsOfProofTransactionData]
}

object ProofsErrorCodeEnum {
  sealed trait ProofsErrorCode {
    val code: String
  }
  case object DataDiffersFromProven extends ProofsErrorCode {
    override val code: String = "904"
  }
  case object InternalError extends ProofsErrorCode {
    override val code: String = "903"
  }
  case object InvalidData extends ProofsErrorCode {
    override val code: String = "901"
  }
  case object ProofCheckFailed extends ProofsErrorCode {
    override val code: String = "902"
  }
}
