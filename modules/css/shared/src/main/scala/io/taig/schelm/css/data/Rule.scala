package io.taig.schelm.css.data

sealed abstract class Rule extends Product with Serializable

object Rule {
  final case class Context(value: String, styles: Stylesheet) extends Rule
  final case class Directive(value: String) extends Rule
  final case class Block(selectors: Selectors, declarations: Declarations) extends Rule
}
