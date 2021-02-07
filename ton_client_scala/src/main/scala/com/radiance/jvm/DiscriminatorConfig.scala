package com.radiance.jvm

import io.circe.generic.extras

object DiscriminatorConfig {
  implicit val discriminatorConfig: extras.Configuration =
    extras.Configuration.default.withDiscriminator("type")
}
