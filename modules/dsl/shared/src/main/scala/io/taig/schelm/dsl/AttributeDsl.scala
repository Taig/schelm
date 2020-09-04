package io.taig.schelm.dsl

import io.taig.schelm.data.Attribute

trait AttributeDsl {
  val src: Attribute.Key = Attribute.Key("src")
  val style: Attribute.Key = Attribute.Key("style")
}
