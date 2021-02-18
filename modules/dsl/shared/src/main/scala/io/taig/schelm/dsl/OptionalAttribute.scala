package io.taig.schelm.dsl

import io.taig.schelm.data.Attribute
import simulacrum.typeclass

final case class OptionalAttribute(key: Attribute.Key, value: Option[Attribute.Value]) {
  def toAttribute: Option[Attribute] = value.map(Attribute(key, _))
}

object OptionalAttribute {
  @typeclass
  trait Evidence[-A] {
    def lift(value: A): Option[Attribute.Value]
  }

  object Evidence {
    implicit val value: Evidence[Attribute.Value] = Some(_)

    implicit val string: Evidence[String] = value => Some(Attribute.Value(value))

    implicit def option[A](implicit evidence: Evidence[A]): Evidence[Option[A]] = {
      case Some(value) => evidence.lift(value)
      case None        => None
    }
  }
}
