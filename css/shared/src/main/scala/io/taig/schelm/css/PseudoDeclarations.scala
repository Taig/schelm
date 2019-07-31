package io.taig.schelm.css

import cats.Monoid
import cats.implicits._

final case class PseudoDeclarations(values: List[PseudoDeclaration]) {
  def :+(declaration: PseudoDeclaration): PseudoDeclarations =
    PseudoDeclarations(values :+ declaration)

  def ++(declarations: PseudoDeclarations): PseudoDeclarations =
    PseudoDeclarations(values ++ declarations.values)

  def toStylesheet(selectors: Selectors): Stylesheet =
    Stylesheet.from(values.map(_.toRule(selectors)))
}

object PseudoDeclarations {
  val Empty: PseudoDeclarations = PseudoDeclarations(List.empty)

  implicit val monoid: Monoid[PseudoDeclarations] =
    new Monoid[PseudoDeclarations] {
      override def empty: PseudoDeclarations = Empty

      override def combine(
          x: PseudoDeclarations,
          y: PseudoDeclarations
      ): PseudoDeclarations = x ++ y
    }
}
