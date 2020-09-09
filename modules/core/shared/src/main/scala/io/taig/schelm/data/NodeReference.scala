package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

sealed abstract class NodeReference[+Event, +Element, +Text, +A] extends Product with Serializable

object NodeReference {
  final case class Element[+Event, Dom, +A](component: Component.Element[Event, A], dom: Dom)
      extends NodeReference[Event, Dom, Nothing, A]

  final case class Fragment[+Event, +A](component: Component.Fragment[A])
      extends NodeReference[Nothing, Nothing, Nothing, A]

  final case class Text[+Event, Dom](component: Component.Text[Event], dom: Dom)
      extends NodeReference[Event, Nothing, Dom, Nothing]

  implicit def traverse[Event, Element, Text]: Traverse[NodeReference[Event, Element, Text, *]] =
    new Traverse[NodeReference[Event, Element, Text, *]] {
      override def traverse[G[_]: Applicative, A, B](
          fa: NodeReference[Event, Element, Text, A]
      )(f: A => G[B]): G[NodeReference[Event, Element, Text, B]] =
        fa match {
          case reference: NodeReference.Element[Event, Element, A] =>
            reference.component.traverse(f).map(component => reference.copy(component = component))
          case reference: NodeReference.Fragment[Event, A] =>
            reference.component.children
              .traverse(f)
              .map(children => NodeReference.Fragment(Component.Fragment(children)))
          case reference: NodeReference.Text[Event, Text] => reference.pure[G].widen
        }

      override def foldLeft[A, B](fa: NodeReference[Event, Element, Text, A], b: B)(f: (B, A) => B): B =
        fa match {
          case reference: NodeReference.Element[Event, Element, A] => reference.component.foldl(b)(f)
          case reference: NodeReference.Fragment[Event, A]         => reference.component.children.foldl(b)(f)
          case _: NodeReference.Text[Event, Text]          => b
        }

      override def foldRight[A, B](fa: NodeReference[Event, Element, Text, A], lb: Eval[B])(
          f: (A, Eval[B]) => Eval[B]
      ): Eval[B] =
        fa match {
          case reference: NodeReference.Element[Event, Element, A] => reference.component.foldr(lb)(f)
          case reference: NodeReference.Fragment[Event, A]         => reference.component.children.foldr(lb)(f)
          case _: NodeReference.Text[Event, Text]          => lb
        }
    }
}
