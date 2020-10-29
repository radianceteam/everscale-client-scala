package com.radiance.scala.tonclient.types


sealed trait MessageBodyType

case object Input extends MessageBodyType
case object Output extends MessageBodyType
case object InternalOutput extends MessageBodyType
case object Event extends MessageBodyType



