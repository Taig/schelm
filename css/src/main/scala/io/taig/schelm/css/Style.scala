package io.taig.schelm.css

import cats.Monoid

final case class Style(
    declarations: Declarations,
    pseudos: PseudoDeclarations
) {
  def :+(declaration: Declaration): Style =
    Style(declarations :+ declaration, pseudos)

  def ++(declarations: Declarations): Style =
    Style(this.declarations ++ declarations, pseudos)

  def :+(pseudo: PseudoDeclaration): Style =
    Style(declarations, pseudos :+ pseudo)

  def ++(pseudos: PseudoDeclarations): Style =
    Style(declarations, this.pseudos ++ pseudos)

  def ++(style: Style): Style =
    Style(declarations ++ style.declarations, pseudos ++ style.pseudos)

  def toStylesheet(selectors: Selectors): Stylesheet =
    pseudos.toStylesheet(selectors) + Rule.Style(selectors, declarations)
}

object Style {
  val Empty: Style = Style(Declarations.Empty, PseudoDeclarations.Empty)

  implicit val monoid: Monoid[Style] = new Monoid[Style] {
    override def empty: Style = Empty

    override def combine(x: Style, y: Style): Style =
      Style(x.declarations ++ y.declarations, x.pseudos ++ y.pseudos)
  }
}
