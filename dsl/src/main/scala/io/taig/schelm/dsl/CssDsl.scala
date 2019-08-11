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
    def styles(styles: Styles): Widget[A, Context, Payload] =
      updateStyles(component, _ ++ styles)

    def styles(style: Style): Widget[A, Context, Payload] =
      updateStyles(component, _ :+ style)

    def styles(
        declarations: DeclarationOrPseudo*
    ): Widget[A, Context, Payload] =
      styles(css(declarations: _*))
  }

  def updateStyles[A](
      widget: Widget[A, Context, Payload],
      f: Styles => Styles
  ): Widget[A, Context, Payload]

  def css(declarations: DeclarationOrPseudo*): Style = reduce(declarations)

  private def reduce(declarations: Iterable[DeclarationOrPseudo]): Style =
    declarations.foldLeft(Style.Empty) {
      case (style, Left(declaration))  => style :+ declaration
      case (style, Right(declaration)) => style :+ declaration
    }

  object & extends CssPseudoDsl
}
