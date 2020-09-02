package io.taig.schelm.data

final case class Attribute(key: Attribute.Key, value: Attribute.Value)

object Attribute {
  final case class Key(value: String) extends AnyVal

  final case class Value(value: String) extends AnyVal
}
