package io.taig.schelm.css.data

import cats.Monoid

final case class Stylesheet(values: List[Stylesheet.Statement])

object Stylesheet {
  val Empty: Stylesheet = Stylesheet(List.empty)

  sealed abstract class Statement extends Product with Serializable

  object Statement {
    final case class Context(value: String, styles: Stylesheet) extends Statement
    final case class Directive(value: String) extends Statement
    final case class Block(value: String, rules: List[Rule]) extends Statement
  }

  final case class Rule(name: Rule.Name, value: Rule.Value)

  object Rule {
    final case class Name(value: String) extends AnyVal
    final case class Value(value: String) extends AnyVal
  }

  implicit val monoid: Monoid[Stylesheet] = new Monoid[Stylesheet] {
    override val empty: Stylesheet = Empty

    override def combine(x: Stylesheet, y: Stylesheet): Stylesheet = Stylesheet(x.values ++ y.values)
  }
}
