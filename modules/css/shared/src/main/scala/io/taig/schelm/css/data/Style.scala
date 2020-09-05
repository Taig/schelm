package io.taig.schelm.css.data

import cats.Monoid
import cats.implicits._

final case class Style(declarations: Declarations, pseudos: PseudoDeclarations) {
  def isEmpty: Boolean = declarations.isEmpty && pseudos.isEmpty

  def toRules(selectors: Selectors): List[Rule.Block] =
    Rule.Block(selectors, declarations) +: pseudos.toRules(selectors)
}

object Style {
  val Empty: Style = Style(Declarations.Empty, PseudoDeclarations.Empty)

  implicit val monoid: Monoid[Style] = new Monoid[Style] {
    override def empty: Style = Empty

    override def combine(x: Style, y: Style): Style =
      Style(x.declarations |+| y.declarations, x.pseudos |+| y.pseudos)
  }
}
