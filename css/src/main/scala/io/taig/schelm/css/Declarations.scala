package io.taig.schelm.css

import cats.Monoid

final case class Declarations(values: List[Declaration]) extends AnyVal {
  def :+(declaration: Declaration): Declarations =
    Declarations(values :+ declaration)

  def ++(declarations: Declarations): Declarations =
    Declarations(values ++ declarations.values)

  def rows: List[String] = values.map(_.toString + ";")

  override def toString: String = rows.mkString("\n")
}

object Declarations {
  val Empty: Declarations = Declarations(List.empty)

  def of(declarations: Declaration*): Declarations = from(declarations)

  def from(values: Iterable[Declaration]): Declarations =
    Declarations(values.toList)

  implicit val monoid: Monoid[Declarations] = new Monoid[Declarations] {
    override def empty: Declarations = Empty

    override def combine(x: Declarations, y: Declarations): Declarations =
      x ++ y
  }
}
