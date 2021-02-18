package io.taig.schelm.dsl

import io.taig.schelm.data.{Attribute, Attributes}
import cats.syntax.all._

trait attributes {
  implicit final class AttributeKeyOps(key: Attribute.Key) {
    def :=[A](value: A)(implicit evidence: OptionalAttribute.Evidence[A]): OptionalAttribute =
      OptionalAttribute(key, evidence.lift(value))
  }

  def data(name: String): Attribute.Key = Attribute.Key(s"data-$name")

  val ariaLabel: Attribute.Key = Attribute.Key("aria-label")
  val cls: Attribute.Key = Attribute.Key("class")
  val forId: Attribute.Key = Attribute.Key("for")
  val id: Attribute.Key = Attribute.Key("id")
  val placeholder: Attribute.Key = Attribute.Key("placeholder")
  val role: Attribute.Key = Attribute.Key("role")
  val src: Attribute.Key = Attribute.Key("src")
  val style: Attribute.Key = Attribute.Key("style")
  val tabindex: Attribute.Key = Attribute.Key("tabindex")
  val tpe: Attribute.Key = Attribute.Key("type")
  val value: Attribute.Key = Attribute.Key("value")
}

object attributes extends attributes {
  def apply(values: OptionalAttribute*): Attributes = Attributes.from(values.mapFilter(_.toAttribute))
}
