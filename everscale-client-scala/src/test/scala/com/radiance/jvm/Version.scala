package com.radiance.jvm

sealed trait Version {
  val name: String
}

case object V1 extends Version {
  override val name: String = "abi_v1"
}

case object V2 extends Version {
  override val name: String = "abi_v2"
}
