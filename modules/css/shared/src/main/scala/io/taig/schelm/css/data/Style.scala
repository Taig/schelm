package io.taig.schelm.css.data

import cats.Monoid

final case class Style(declarations: Declarations, pseudos: PseudoDeclarations) {
  def isEmpty: Boolean = declarations.isEmpty && pseudos.isEmpty

  def ++(style: Style): Style = Style(declarations ++ style.declarations, pseudos ++ style.pseudos)

  def toRules(selectors: Selectors): List[Rule.Block] =
    Rule.Block(selectors, declarations) +: pseudos.toRules(selectors)
}

object Style {
  val Empty: Style = Style(Declarations.Empty, PseudoDeclarations.Empty)

  def of(declarations: Declaration*): Style = Style(Declarations.from(declarations), PseudoDeclarations.Empty)

  implicit val monoid: Monoid[Style] = new Monoid[Style] {
    override def empty: Style = Empty

    override def combine(x: Style, y: Style): Style = x ++ y
  }
}
