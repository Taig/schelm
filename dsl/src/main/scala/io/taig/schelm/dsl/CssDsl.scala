package io.taig.schelm.dsl

import cats.implicits._
import io.taig.schelm.Widget
import io.taig.schelm.css._

trait CssDsl[Context] extends CssKeysDsl with CssValuesDsl {
  this: WidgetDsl[Context, Styles] =>
  implicit final def declarationToEither(
      declaration: Declaration
  ): DeclarationOrPseudo = declaration.asLeft

  implicit final def pseudoToEither(
      declaration: PseudoDeclaration
  ): DeclarationOrPseudo = declaration.asRight

  implicit final def numericToScaleUnitsOps[B: Numeric](
      value: B
  ): CssScaleUnitOps =
    new CssScaleUnitOps(value.toString)

  implicit final def numericToTimeUnitsOps[B: Numeric](
      value: B
  ): CssTimeUnitOps =
    new CssTimeUnitOps(value.toString)

  implicit final class CssBuilder[A](component: Widget[A, Context, Styles]) {
    def styles(styles: Styles): Widget[A, Context, Styles] =
      updatePayload(component, _ ++ styles)

    def styles(style: Style): Widget[A, Context, Styles] =
      updatePayload(component, _ :+ style)

    def styles(
        declarations: DeclarationOrPseudo*
    ): Widget[A, Context, Styles] = styles(css(declarations: _*))
  }

  def css(declarations: DeclarationOrPseudo*): Style =
    declarations.foldLeft(Style.Empty) {
      case (style, Left(declaration))  => style :+ declaration
      case (style, Right(declaration)) => style :+ declaration
    }

  object & extends CssPseudoDsl
}
