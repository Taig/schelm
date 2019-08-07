package io.taig.schelm

import io.taig.schelm.css.{Declaration, PseudoDeclaration, Style}
import cats.implicits._

import scala.annotation.tailrec

package object dsl {
  type DeclarationOrPseudo = Either[Declaration, PseudoDeclaration]

  @tailrec
  private [dsl] def flatten[A](optional: Property.Optional[A]): Option[Either[Attribute, Listener[A]]] =
    optional.property match {
      case Some(Property.Attribute(value)) => value.asLeft.some
      case Some(listener: Property.Listener[A]) => listener.value.asRight.some
      case Some(optional: Property.Optional[A]) => flatten(optional)
      case None => None
    }

  private[dsl] def split[A](
      properties: Iterable[Property[A]]
  ): (List[Attribute], List[Listener[A]]) = {
    val attributes = collection.mutable.ListBuffer.empty[Attribute]
    val listeners = collection.mutable.ListBuffer.empty[Listener[A]]

    properties.foreach {
      case Property.Attribute(attribute) => attributes += attribute
      case listener: Property.Listener[A] => listeners += listener.value
      case optional: Property.Optional[A] =>
        flatten(optional) match {
          case Some(Left(attribute)) => attributes += attribute
          case Some(Right(listener)) => listeners += listener
          case None => //
        }
    }

    (attributes.toList, listeners.toList)
  }

  private[dsl] def reduce(declarations: Iterable[DeclarationOrPseudo]): Style =
    declarations.foldLeft(Style.Empty) {
      case (style, Left(declaration))  => style :+ declaration
      case (style, Right(declaration)) => style :+ declaration
    }
}
