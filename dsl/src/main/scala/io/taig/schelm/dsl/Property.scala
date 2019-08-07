package io.taig.schelm.dsl

import io.taig.schelm.{Attribute => SAttribute}
import io.taig.schelm.{Listener => SListener}

sealed abstract class Property[+A] extends Product with Serializable

object Property {
  final case class Attribute(value: SAttribute) extends Property[Nothing]
  final case class Listener[A](value: SListener[A]) extends Property[A]
  final case class Optional[A](property: Option[Property[A]])
      extends Property[A]

  def apply(attribute: SAttribute): Property[Nothing] = Attribute(attribute)

  def apply[A](listener: SListener[A]): Property[A] = Listener(listener)

  def optional[A](property: Option[Property[A]]): Property[A] =
    Optional(property)

  val none: Property[Nothing] = optional(None)
}
