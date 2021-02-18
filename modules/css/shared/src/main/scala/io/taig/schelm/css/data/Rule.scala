package io.taig.schelm.css.data

sealed abstract class Rule extends Product with Serializable {
  override def toString: String = this match {
    case Rule.Context(value, stylesheet)     => ???
    case Rule.Directive(value)               => ???
    case Rule.Block(selectors, declarations) => s"$selectors $declarations"
  }
}

object Rule {
  final case class Context(value: String, stylesheet: Stylesheet) extends Rule
  final case class Directive(value: String) extends Rule
  final case class Block(selectors: Selectors, declarations: Declarations) extends Rule
}
