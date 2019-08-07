package io.taig.schelm

sealed abstract class Keys extends Product with Serializable {
  final def length: Int = this match {
    case Keys.Indices(length)     => length
    case Keys.Identifiers(values) => values.length
  }
}

object Keys {
  final case class Indices(count: Int) extends Keys
  final case class Identifiers(values: List[String]) extends Keys
}
