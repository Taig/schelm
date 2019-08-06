package io.taig.schelm

import io.taig.schelm.css.{Declaration, PseudoDeclaration, Style}
import cats.implicits._

package object dsl {
  type Property[+A] = Either[Attribute, Listener[A]]

  type DeclarationOrPseudo = Either[Declaration, PseudoDeclaration]

  private[dsl] def split[A](
      properties: Iterable[Property[A]]
  ): (List[Attribute], List[Listener[A]]) =
    properties.toList.partitionEither(identity)

  private[dsl] def reduce(declarations: Iterable[DeclarationOrPseudo]): Style =
    declarations.foldLeft(Style.Empty) {
      case (style, Left(declaration))  => style :+ declaration
      case (style, Right(declaration)) => style :+ declaration
    }
}
