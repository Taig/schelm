package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

sealed abstract class NodeReference[+Event, +Element, +Text, +A] extends Product with Serializable

object NodeReference {
  final case class Element[Event, Dom, A](node: Node.Element[Event, A], dom: Dom)
      extends NodeReference[Event, Dom, Nothing, A]
  final case class Fragment[Event, A](node: Node.Fragment[Event, A]) extends NodeReference[Event, Nothing, Nothing, A]
  final case class Text[Event, Dom](node: Node.Text[Event], dom: Dom)
      extends NodeReference[Event, Nothing, Dom, Nothing]

  implicit def traverse[Event, Element, Text]: Traverse[NodeReference[Event, Element, Text, *]] =
    new Traverse[NodeReference[Event, Element, Text, *]] {
      override def traverse[G[_]: Applicative, A, B](
          fa: NodeReference[Event, Element, Text, A]
      )(f: A => G[B]): G[NodeReference[Event, Element, Text, B]] =
        fa match {
          case reference @ NodeReference.Element(component, _) =>
            component.traverse(f).map(component => reference.copy(node = component))
          case reference @ NodeReference.Fragment(component) =>
            component.traverse(f).map(component => reference.copy(node = component))
          case reference @ NodeReference.Text(_, _) => reference.pure[G].widen
        }

      override def foldLeft[A, B](fa: NodeReference[Event, Element, Text, A], b: B)(f: (B, A) => B): B =
        fa match {
          case NodeReference.Element(component, _) => component.foldl(b)(f)
          case NodeReference.Fragment(component)   => component.foldl(b)(f)
          case _: NodeReference.Text[Event, Text]  => b
        }

      override def foldRight[A, B](fa: NodeReference[Event, Element, Text, A], lb: Eval[B])(
          f: (A, Eval[B]) => Eval[B]
      ): Eval[B] =
        fa match {
          case NodeReference.Element(component, _) => component.foldr(lb)(f)
          case NodeReference.Fragment(component)   => component.foldr(lb)(f)
          case _: NodeReference.Text[Event, Text]  => lb
        }
    }
}
