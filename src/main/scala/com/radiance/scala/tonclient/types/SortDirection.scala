package com.radiance.scala.tonclient.types

sealed trait SortDirection

case object ASC extends SortDirection
case object DESC extends SortDirection

