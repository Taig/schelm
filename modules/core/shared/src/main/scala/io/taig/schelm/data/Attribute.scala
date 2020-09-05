package io.taig.schelm.data

final case class Attribute(key: Attribute.Key, value: Attribute.Value)

object Attribute {
  final case class Key(value: String) extends AnyVal

  object Key {
    val Class: Key = Key("class")
  }

  final case class Value(value: String) extends AnyVal {
    def ++(value: Value, separator: String = " "): Value = Value(this.value + separator + value.value)
  }
}
