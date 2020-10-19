package io.taig.schelm.dsl.util

import io.taig.color.Color
import simulacrum.typeclass
import cats.implicits._
import io.taig.schelm.css.data.Declaration

@typeclass
trait DeclarationValue[A] {
  def lift(value: A): Option[Declaration.Value]
}

object DeclarationValue {
  implicit val color: DeclarationValue[Color] = color => Declaration.Value(color.toHex).some

  implicit val declarationValue: DeclarationValue[Declaration.Value] = _.some

  implicit def option[A: DeclarationValue]: DeclarationValue[Option[A]] = _.flatMap(DeclarationValue[A].lift)

  implicit val string: DeclarationValue[String] = value => Declaration.Value(value).some
}
