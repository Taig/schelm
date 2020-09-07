package io.taig.schelm.dsl

import io.taig.schelm.data.Attribute

trait AttributeDsl {
  def data(name: String): Attribute.Key = Attribute.Key(s"data-$name")

  val src: Attribute.Key = Attribute.Key("src")
  val style: Attribute.Key = Attribute.Key("style")
}
