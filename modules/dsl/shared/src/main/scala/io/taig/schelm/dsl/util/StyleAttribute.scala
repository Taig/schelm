package io.taig.schelm.dsl.util

import io.taig.color.Color
import simulacrum.typeclass
import cats.implicits._
import io.taig.schelm.css.data.Declaration

@typeclass
trait StyleAttribute[A] {
  def lift(value: A): Option[Declaration.Value]
}

object StyleAttribute {
  implicit val color: StyleAttribute[Color] = color => Declaration.Value(color.toHex).some

  implicit def option[A: StyleAttribute]: StyleAttribute[Option[A]] = _.flatMap(StyleAttribute[A].lift)

  implicit val string: StyleAttribute[String] = value => Declaration.Value(value).some
}
