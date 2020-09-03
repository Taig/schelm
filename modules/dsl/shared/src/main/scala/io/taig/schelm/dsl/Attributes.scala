package io.taig.schelm.dsl

import io.taig.schelm.data.Attribute

trait Attributes {
  val src: Attribute.Key = Attribute.Key("src")
  val style: Attribute.Key = Attribute.Key("style")
}
