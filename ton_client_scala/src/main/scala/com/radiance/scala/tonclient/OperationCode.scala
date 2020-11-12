package com.radiance.scala.tonclient

sealed trait OperationCode

case object SuccessCode extends OperationCode
case object ErrorCode extends OperationCode
case object NopCode extends OperationCode
case object CustomCode extends OperationCode

object OperationCode {
  def fromInt(i: Int): OperationCode = i match {
    case 0 => SuccessCode
    case 1 => ErrorCode
    case 2 => NopCode
    case 100 => CustomCode
    case x => throw new IllegalArgumentException(s"Unexpected code: $x")
  }
}