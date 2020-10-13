package io.taig.schelm.css.data

import cats.Monoid

final case class Declarations(values: List[Declaration]) extends AnyVal {
  def isEmpty: Boolean = values.isEmpty

  def :+(declaration: Declaration): Declarations = Declarations(values :+ declaration)

  def +:(declaration: Declaration): Declarations = Declarations(declaration +: values)

  def ++(declarations: Declarations): Declarations = Declarations(values ++ declarations.values)
}

object Declarations {
  val Empty: Declarations = Declarations(List.empty)

  def from(declarations: Iterable[Declaration]): Declarations = Declarations(declarations.toList)

  def of(declarations: Declaration*): Declarations = from(declarations)

  implicit val monoid: Monoid[Declarations] = new Monoid[Declarations] {
    override def empty: Declarations = Empty

    override def combine(x: Declarations, y: Declarations): Declarations = x ++ y
  }
}
