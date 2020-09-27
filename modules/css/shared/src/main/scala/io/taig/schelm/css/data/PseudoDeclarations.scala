package io.taig.schelm.css.data

import cats.Monoid

final case class PseudoDeclarations(values: List[PseudoDeclaration]) {
  def isEmpty: Boolean = values.isEmpty

  def ++(declarations: PseudoDeclarations): PseudoDeclarations = PseudoDeclarations(values ++ declarations.values)

  def toRules(selectors: Selectors): List[Rule.Block] = values.map(_.toRule(selectors))
}

object PseudoDeclarations {
  val Empty: PseudoDeclarations = PseudoDeclarations(List.empty)

  implicit val monoid: Monoid[PseudoDeclarations] =
    new Monoid[PseudoDeclarations] {
      override def empty: PseudoDeclarations = Empty

      override def combine(x: PseudoDeclarations, y: PseudoDeclarations): PseudoDeclarations = x ++ y
    }
}
