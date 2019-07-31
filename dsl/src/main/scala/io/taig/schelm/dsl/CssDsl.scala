package io.taig.schelm.dsl

import cats.implicits._
import io.taig.schelm.css._

trait CssDsl[A] extends CssKeysDsl with CssValuesDsl with NormalizeCss {
  implicit def declarationToEither(
      declaration: Declaration
  ): DeclarationOrPseudo = declaration.asLeft

  implicit def pseudoToEither(
      declaration: PseudoDeclaration
  ): DeclarationOrPseudo = declaration.asRight

  implicit def numericToUnitsOps[B: Numeric](value: B): CssUnitOps =
    new CssUnitOps(value.toString)

  def css(widget: Widget[A], styles: Styles): Widget[A] =
    Widget(widget.tail, styles)

  def stylesheet(selector: String, selectors: String*)(
      declarations: DeclarationOrPseudo*
  ): Stylesheet =
    reduce(declarations).toStylesheet(
      Selectors.from(Selector(selector), selectors.map(Selector))
    )

  def styles(declarations: DeclarationOrPseudo*): Styles =
    Styles.of(reduce(declarations))

  object & extends CssPseudoDsl
}
