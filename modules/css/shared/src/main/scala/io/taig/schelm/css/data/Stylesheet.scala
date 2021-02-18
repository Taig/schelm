package io.taig.schelm.css.data

import cats.Monoid
import io.taig.schelm.util.Text

final case class Stylesheet(values: List[Rule]) {
  override def toString: String =
    s"""Stylesheet(
       |  ${Text.align(2)(values.mkString(",\n"))}
       |)""".stripMargin
}

object Stylesheet {
  val Empty: Stylesheet = Stylesheet(List.empty)

  def from(rules: Iterable[Rule]): Stylesheet = Stylesheet(rules.toList)

  def of(rules: Rule*): Stylesheet = Stylesheet(rules.toList)

  implicit val monoid: Monoid[Stylesheet] = new Monoid[Stylesheet] {
    override val empty: Stylesheet = Empty

    override def combine(x: Stylesheet, y: Stylesheet): Stylesheet = Stylesheet(x.values ++ y.values)
  }
}
