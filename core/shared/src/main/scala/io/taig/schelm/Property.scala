package io.taig.schelm

import cats.Semigroup

sealed abstract class Property[+Event] extends Product with Serializable

sealed abstract class Value extends Property[Nothing]

object Value {
  final case class Flag(value: Boolean) extends Value
  final case class Multiple(values: List[String], accumulator: Accumulator)
      extends Value
  final case class One(value: String) extends Value

  implicit val semigroup: Semigroup[Value] = new Semigroup[Value] {
    override def combine(x: Value, y: Value): Value = (x, y) match {
      case (Multiple(x, accumulator), Multiple(y, _)) =>
        Multiple(x ++ y, accumulator)
      case (value, _) => value
    }
  }
}

sealed abstract class Listener[A] extends Property[A]

object Listener {
  final case class Pure[A](event: A) extends Listener[A]
  final case class Input[A](event: String => A) extends Listener[A]
}
