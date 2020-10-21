package io.taig.schelm.dsl.syntax

import io.taig.schelm.data.{Attribute, Attributes}
import io.taig.schelm.dsl.data.DslAttribute
import io.taig.schelm.dsl.util.AttributeValue

trait attribute {
  implicit class AttributeKeyOps(key: Attribute.Key) {
    def :=[A](value: A)(implicit evidence: AttributeValue[A]): DslAttribute =
      DslAttribute(key, evidence.lift(value).map(Attribute.Value.apply))
  }

  def attrs(values: DslAttribute*): Attributes =
    Attributes.from(values.collect { case DslAttribute(key, Some(value)) => Attribute(key, value) })

  def dat(name: String): Attribute.Key = Attribute.Key(s"data-$name")

  val ariaLabel: Attribute.Key = Attribute.Key("aria-label")
  val cls: Attribute.Key = Attribute.Key("class")
  val `class`: Attribute.Key = cls
  val placeholder: Attribute.Key = Attribute.Key("placeholder")
  val role: Attribute.Key = Attribute.Key("role")
  val src: Attribute.Key = Attribute.Key("src")
  val style: Attribute.Key = Attribute.Key("style")
  val value: Attribute.Key = Attribute.Key("value")
  val tabindex: Attribute.Key = Attribute.Key("tabindex")
  val tpe: Attribute.Key = Attribute.Key("type")
  val `type`: Attribute.Key = tpe
}

object attribute extends attribute
