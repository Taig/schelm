package io.taig.schelm.dsl

import cats.implicits._
import io.taig.schelm.Widget
import io.taig.schelm.css._

trait CssDsl[Context, Payload] extends CssKeysDsl with CssValuesDsl {
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

  final implicit class CssBuilder[A](component: Widget[A, Context, Payload]) {
    def css(styles: Styles): Widget[A, Context, Payload] =
      updateStyles(component, _ ++ styles)

    def css(
        declaration: DeclarationOrPseudo,
        declarations: DeclarationOrPseudo*
    ): Widget[A, Context, Payload] =
      css(styles(declaration, declarations: _*))
  }

  def updateStyles[A](
      widget: Widget[A, Context, Payload],
      f: Styles => Styles
  ): Widget[A, Context, Payload]

  def styles(
      declaration: DeclarationOrPseudo,
      declarations: DeclarationOrPseudo*
  ): Styles = Styles.of(reduce(declaration +: declarations))

  object & extends CssPseudoDsl
}
