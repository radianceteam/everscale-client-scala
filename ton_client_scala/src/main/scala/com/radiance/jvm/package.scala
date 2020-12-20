package com.radiance

package object jvm {
  type Value = io.circe.Json
  type API = io.circe.Json
  type Request = io.circe.Json => ()
}
