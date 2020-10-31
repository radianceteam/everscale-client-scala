package com.radiance.scala.tonclient.types.in

import com.radiance.scala.tonclient.types.both.FunctionHeader
import io.circe._
import io.circe.derivation._

case class CallSet(
                    /**
                     * Function name that is being called.
                     */
                    function_name: String,

                    /**
                     * Function header.<p> If an application omits some header parameters required by the contract's ABI, the library will set the default values for them.
                     */
                    header: FunctionHeader,

                    /**
                     * Function input parameters according to ABI.
                     */
                    input: String
                  )

object CallSet {
  implicit val callSetEncoder: Encoder[CallSet] = deriveEncoder[CallSet]
}
