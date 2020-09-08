package io.taig.schelm.dsl

import io.taig.schelm.data.Attribute

trait AttributeDsl {
  def data(name: String): Attribute.Key = Attribute.Key(s"data-$name")

  val cls: Attribute.Key = Attribute.Key("class")
  val `class`: Attribute.Key = cls
  val role: Attribute.Key = Attribute.Key("role")
  val src: Attribute.Key = Attribute.Key("src")
  val style: Attribute.Key = Attribute.Key("style")
  val tabindex: Attribute.Key = Attribute.Key("tabindex")
}
