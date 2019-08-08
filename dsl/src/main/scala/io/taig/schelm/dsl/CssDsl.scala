package io.taig.schelm.dsl

import cats.implicits._
import io.taig.schelm.css._

trait CssDsl extends CssKeysDsl with CssValuesDsl {
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

  def css(declarations: DeclarationOrPseudo*): Property[Nothing] =
    Property.fromStyles(Styles.of(reduce(declarations)))

  object & extends CssPseudoDsl
}
