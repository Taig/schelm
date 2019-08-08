package io.taig.schelm

import io.taig.schelm.css._

package object dsl {
  type DeclarationOrPseudo = Either[Declaration, PseudoDeclaration]

  private[dsl] def split[A](
      properties: Iterable[Property[A]]
  ): (Attributes, Listeners[A], Styles) =
    // format: off
    properties.foldLeft((Attributes.Empty, Listeners.empty[A], Styles.Empty)) {
      case ((attributes, listeners, styles), Property.Attribute(attribute)) =>
        (attributes + attribute, listeners, styles)
      case ((attributes, listeners, styles), property: Property.Listener[A]) =>
        (attributes, listeners + property.value, styles)
      case ((attributes, listeners, styles), Property.Optional(property)) =>
        val (x, y, z) = split(property)
        (attributes ++ x, listeners ++ y, styles ++ z)
      case ((attributes, listeners, styles), Property.Styles(values)) =>
        (attributes, listeners, styles ++ values)
    }
    // format: on

  private[dsl] def reduce(declarations: Iterable[DeclarationOrPseudo]): Style =
    declarations.foldLeft(Style.Empty) {
      case (style, Left(declaration))  => style :+ declaration
      case (style, Right(declaration)) => style :+ declaration
    }
}
