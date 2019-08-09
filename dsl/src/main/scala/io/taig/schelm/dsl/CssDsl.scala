package io.taig.schelm.dsl

import cats.implicits._
import io.taig.schelm.css._

trait CssDsl[Component[+_]] extends CssKeysDsl with CssValuesDsl {
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

  final implicit class CssBuilder[A](component: Component[A]) {
    def css(styles: Styles): Component[A] = updateStyles(component, _ ++ styles)

    def css(
        declaration: DeclarationOrPseudo,
        declarations: DeclarationOrPseudo*
    ): Component[A] =
      css(styles(declaration, declarations: _*))
  }

  protected def updateStyles[A](
      component: Component[A],
      f: Styles => Styles
  ): Component[A]

  def styles(
      declaration: DeclarationOrPseudo,
      declarations: DeclarationOrPseudo*
  ): Styles = Styles.of(reduce(declaration +: declarations))

  object & extends CssPseudoDsl
}
