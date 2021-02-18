package io.taig.schelm.dsl

import io.taig.color.Color
import io.taig.schelm.css.data.Declaration
import simulacrum.typeclass

final case class OptionalDeclaration(name: Declaration.Name, value: Option[Declaration.Value]) {
  def toDeclaration: Option[Declaration] = value.map(Declaration(name, _))
}

object OptionalDeclaration {
  @typeclass
  trait Evidence[-A] {
    def lift(value: A): Option[Declaration.Value]

    final def contramap[B](f: B => A): Evidence[B] = value => lift(f(value))
  }

  object Evidence {
    implicit val value: Evidence[Declaration.Value] = Some(_)

    implicit val string: Evidence[String] = value => Some(Declaration.Value(value))

    implicit val color: Evidence[Color] = Evidence[String].contramap(_.toHex)

    implicit val lengthUnit: Evidence[LengthUnit] = Evidence[String].contramap(_.render)

    implicit def option[A](implicit evidence: Evidence[A]): Evidence[Option[A]] = {
      case Some(value) => evidence.lift(value)
      case None        => None
    }
  }
}
