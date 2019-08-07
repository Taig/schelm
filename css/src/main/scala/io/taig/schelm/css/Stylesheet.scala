package io.taig.schelm.css

import cats.Monoid

final case class Stylesheet(values: Set[Rule]) {
  def isEmpty: Boolean = values.isEmpty

  def +(rule: Rule): Stylesheet = Stylesheet(values + rule)

  def ++(stylesheet: Stylesheet): Stylesheet =
    Stylesheet(values ++ stylesheet.values)

  def toSet: Set[Rule] = values

  def toList: List[Rule] = values.toList

  override def toString: String = values.map(_.toString).mkString("\n")
}

object Stylesheet {
  val Empty: Stylesheet = Stylesheet(Set.empty)

  def of(rules: Rule*): Stylesheet = from(rules)

  def from(rules: Iterable[Rule]): Stylesheet = Stylesheet(rules.toSet)

  implicit val monoid: Monoid[Stylesheet] = new Monoid[Stylesheet] {
    override def empty: Stylesheet = Empty

    override def combine(x: Stylesheet, y: Stylesheet): Stylesheet = x ++ y
  }
}
