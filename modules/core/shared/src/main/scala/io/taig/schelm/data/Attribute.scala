package io.taig.schelm.data

import io.taig.schelm.instance.{AttributeInstances, AttributeKeyInstances, AttributeValueInstances}

final case class Attribute(key: Attribute.Key, value: Attribute.Value) {
  def toTuple: (Attribute.Key, Attribute.Value) = (key, value)

  override def toString: String = s"${key.value} = ${value.value}"
}

object Attribute extends AttributeInstances {
  final case class Key(value: String) extends AnyVal

  object Key extends AttributeKeyInstances {
    val Class: Key = Key("class")
  }

  final case class Value(value: String) extends AnyVal {
    def combine(value: Value, separator: String): Value = Value(this.value + separator + value.value)

    def ++(value: Value): Value = combine(value, separator = " ")
  }

  object Value extends AttributeValueInstances {
    val Empty: Attribute.Value = Value("")

    def fromInt(value: Int): Value = Value(String.valueOf(value))

    def fromLong(value: Long): Value = Value(String.valueOf(value))
  }
}
