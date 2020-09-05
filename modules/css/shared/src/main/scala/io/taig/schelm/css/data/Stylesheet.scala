package io.taig.schelm.css.data

import cats.Monoid

final case class Stylesheet(values: List[Rule])

object Stylesheet {
  val Empty: Stylesheet = Stylesheet(List.empty)

  def from(rules: Iterable[Rule]): Stylesheet = Stylesheet(rules.toList)

  def of(rules: Rule*): Stylesheet = Stylesheet(rules.toList)

  implicit val monoid: Monoid[Stylesheet] = new Monoid[Stylesheet] {
    override val empty: Stylesheet = Empty

    override def combine(x: Stylesheet, y: Stylesheet): Stylesheet = Stylesheet(x.values ++ y.values)
  }
}
