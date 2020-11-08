package io.taig.schelm.data

import cats.{Order, Show}

sealed abstract class Key extends Product with Serializable

object Key {
  final case class Identifier(value: String) extends Key
  final case class Index(value: Int) extends Key

  implicit val order: Order[Key] = new Order[Key] {
    override def compare(x: Key, y: Key): Int = (x, y) match {
      case (Identifier(x), Identifier(y)) => x compare y
      case (Index(x), Index(y))           => x compare y
      case (Identifier(_), Index(_))      => 1
      case (Index(_), Identifier(_))      => -1
    }
  }

  implicit val show: Show[Key] = {
    case Identifier(value) => value
    case Index(value)      => s"[$value]"
  }
}
