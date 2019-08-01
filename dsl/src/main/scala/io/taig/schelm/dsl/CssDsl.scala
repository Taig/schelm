package io.taig.schelm.dsl

import cats.implicits._
import io.taig.schelm.css._

trait CssDsl[A] extends CssKeysDsl with CssValuesDsl {
  implicit def declarationToEither(
      declaration: Declaration
  ): DeclarationOrPseudo = declaration.asLeft

  implicit def pseudoToEither(
      declaration: PseudoDeclaration
  ): DeclarationOrPseudo = declaration.asRight

  implicit def numericToScaleUnitsOps[B: Numeric](value: B): CssScaleUnitOps =
    new CssScaleUnitOps(value.toString)

  implicit def numericToTimeUnitsOps[B: Numeric](value: B): CssTimeUnitOps =
    new CssTimeUnitOps(value.toString)

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
