package io.taig.schelm.css.data

import cats.Monoid

final case class Style(declarations: Declarations, pseudos: PseudoDeclarations) {
  def isEmpty: Boolean = declarations.isEmpty && pseudos.isEmpty

  def concat(style: Style, divider: String): Style =
    Style(declarations.concat(style.declarations, divider), pseudos.concat(pseudos, divider))

  def ++(style: Style): Style = Style(declarations ++ style.declarations, pseudos ++ style.pseudos)

  def +:(declaration: Declaration): Style = copy(declarations = declaration +: declarations)

  def :+(declaration: Declaration): Style = copy(declarations = declarations :+ declaration)

  def toRules(selectors: Selectors): List[Rule.Block] =
    Rule.Block(selectors, declarations) +: pseudos.toRules(selectors)
}

object Style {
  val Empty: Style = Style(Declarations.Empty, PseudoDeclarations.Empty)

  def from(declarations: Iterable[Declaration]): Style =
    Style(Declarations.from(declarations), PseudoDeclarations.Empty)

  def of(declarations: Declaration*): Style = from(declarations)

  def one(declaration: Declaration): Style = Style(Declarations.one(declaration), PseudoDeclarations.Empty)

  implicit val monoid: Monoid[Style] = new Monoid[Style] {
    override def empty: Style = Empty

    override def combine(x: Style, y: Style): Style = x ++ y
  }
}
