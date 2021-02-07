package com.radiance.jvm

sealed trait OperationCode

case object SuccessCode extends OperationCode
case object ErrorCode extends OperationCode
case object NopCode extends OperationCode
case object CustomCode extends OperationCode
case object AppRequestCode extends OperationCode
case object AppNotifyCode extends OperationCode

object OperationCode {
  def fromInt(i: Int): OperationCode =
    i match {
      case 0   => SuccessCode
      case 1   => ErrorCode
      case 2   => NopCode
      case 3   => AppRequestCode
      case 4   => AppNotifyCode
      case 100 => CustomCode
      // TODO
      case 101 => throw new IllegalArgumentException(s"Unexpected code: 101")
      case x   => throw new IllegalArgumentException(s"Unexpected code: $x")
    }
}
