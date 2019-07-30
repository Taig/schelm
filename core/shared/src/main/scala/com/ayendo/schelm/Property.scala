package com.ayendo.schelm

import cats.{Eq, Semigroup}
import cats.implicits._

sealed abstract class Property[+A] extends Product with Serializable

object Property {
  implicit def eq[A: Eq]: Eq[Property[A]] =
    (x, y) =>
      PartialFunction.cond((x, y)) {
        case (x: Value, y: Value)             => x == y
        case (x: Listener[A], y: Listener[A]) => x === y
      }
}

sealed abstract class Value extends Property[Nothing] {
  final def render: Option[String] = this match {
    case Value.Multiple(values, accumulator) =>
      values.mkString(accumulator.value).some
    case Value.One(value)  => value.some
    case Value.Flag(true)  => "".some
    case Value.Flag(false) => None
  }
}

object Value {
  final case class Flag(value: Boolean) extends Value
  final case class Multiple(values: List[String], accumulator: Accumulator)
      extends Value
  final case class One(value: String) extends Value

  implicit val semigroup: Semigroup[Value] = {
    case (Multiple(x, accumulator), Multiple(y, _)) =>
      Multiple(x ++ y, accumulator)
    case (value, _) => value
  }
}

sealed abstract class Listener[A] extends Property[A]

object Listener {
  final case class Pure[A](event: A) extends Listener[A]
  final case class Input[A](event: String => A) extends Listener[A]

  implicit def eq[A: Eq]: Eq[Listener[A]] =
    (x, y) =>
      PartialFunction.cond((x, y)) {
        case (Pure(x), Pure(y))   => x === y
        case (Input(x), Input(y)) => x == y
      }
}
