package io.taig.schelm.dsl

import io.taig.schelm.{Attribute => SAttribute}
import io.taig.schelm.{Listener => SListener}
import io.taig.schelm.css.{Styles => SStyles}

sealed abstract class Property[+A] extends Product with Serializable

object Property {
  final case class Attribute(value: SAttribute) extends Property[Nothing]
  final case class Listener[A](value: SListener[A]) extends Property[A]
  final case class Optional[A](property: Option[Property[A]])
      extends Property[A]
  final case class Styles(value: SStyles) extends Property[Nothing]

  implicit def optional[A](property: Option[Property[A]]): Property[A] =
    Optional(property)

  val none: Property[Nothing] = optional(None)

  def fromAttribute(attribute: SAttribute): Property[Nothing] =
    Attribute(attribute)

  def fromListener[A](listener: SListener[A]): Property[A] = Listener(listener)

  def fromStyles(styles: SStyles): Property[Nothing] = Styles(styles)
}
