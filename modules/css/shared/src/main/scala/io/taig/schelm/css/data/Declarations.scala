package io.taig.schelm.css.data

import cats.Monoid

final case class Declarations(values: List[Declaration]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty
}

object Declarations {
  val Empty: Declarations = Declarations(List.empty)

  def from(declarations: Iterable[Declaration]): Declarations = Declarations(declarations.toList)

  implicit val monoid: Monoid[Declarations] = new Monoid[Declarations] {
    override def empty: Declarations = Empty

    override def combine(x: Declarations, y: Declarations): Declarations = Declarations(x.values ++ y.values)
  }
}
