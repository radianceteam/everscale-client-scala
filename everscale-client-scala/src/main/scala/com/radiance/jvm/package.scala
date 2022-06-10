package com.radiance

package object jvm {
  type Value = io.circe.Json
  type API = io.circe.Json
  type Request = Function[io.circe.Json, Unit]
}
