package io.taig.schelm.data

sealed abstract class Key extends Product with Serializable

object Key {
  final case class Identifier(value: String) extends Key
  final case class Index(value: Int) extends Key
}
