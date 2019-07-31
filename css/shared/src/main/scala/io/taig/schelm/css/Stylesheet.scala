package io.taig.schelm.css

import cats.Monoid
import cats.implicits._

final case class Stylesheet(values: List[Rule]) {
  def +:(rule: Rule): Stylesheet = Stylesheet(rule +: values)

  def :+(rule: Rule): Stylesheet = Stylesheet(values :+ rule)

  def ++(stylesheet: Stylesheet): Stylesheet =
    Stylesheet(values ++ stylesheet.values)

  override def toString: String = values.map(_.toString).mkString("\n")
}

object Stylesheet {
  val Empty: Stylesheet = Stylesheet(List.empty)

  def of(rules: Rule*): Stylesheet = from(rules)

  def from(rules: Iterable[Rule]): Stylesheet = Stylesheet(rules.toList)

  implicit val monoid: Monoid[Stylesheet] = new Monoid[Stylesheet] {
    override def empty: Stylesheet = Empty

    override def combine(x: Stylesheet, y: Stylesheet): Stylesheet = x ++ y
  }
}
