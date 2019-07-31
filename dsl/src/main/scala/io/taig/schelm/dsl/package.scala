package io.taig.schelm

import io.taig.schelm.css.{Declaration, PseudoDeclaration, Style}

package object dsl {
  type DeclarationOrPseudo = Either[Declaration, PseudoDeclaration]

  private[dsl] def reduce(declarations: Iterable[DeclarationOrPseudo]): Style =
    declarations.foldLeft(Style.Empty) {
      case (style, Left(declaration))  => style :+ declaration
      case (style, Right(declaration)) => style :+ declaration
    }
}
