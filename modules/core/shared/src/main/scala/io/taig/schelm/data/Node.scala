package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.Navigator

sealed abstract class Node[+Event, +A] extends Product with Serializable

object Node {
  final case class Element[+Event, +A](tag: Tag[Event], tpe: Element.Type[A]) extends Node[Event, A]

  object Element {

    /** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
    sealed abstract class Type[+A] extends Product with Serializable

    object Type {
      final case class Normal[+A](children: Children[A]) extends Type[A]
      final case object Void extends Type[Nothing]

      implicit val traverse: Traverse[Type] = new Traverse[Type] {
        override def traverse[G[_]: Applicative, A, B](fa: Type[A])(f: A => G[B]): G[Type[B]] =
          fa match {
            case tpe: Normal[A] => tpe.children.traverse(f).map(Normal[B])
            case Void           => Void.pure[G].widen
          }

        override def foldLeft[A, B](fa: Type[A], b: B)(f: (B, A) => B): B = fa match {
          case tpe: Normal[A] => tpe.children.foldl(b)(f)
          case Void           => b
        }

        override def foldRight[A, B](fa: Type[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
          case tpe: Normal[A] => tpe.children.foldr(lb)(f)
          case Void           => lb
        }
      }

      implicit def navigator[A]: Navigator[Nothing, Type[A], A] = new Navigator[Nothing, Type[A], A] {
        override def attributes(tpe: Type[A], f: Attributes => Attributes): Type[A] = tpe

        override def listeners(tpe: Type[A], f: Listeners[Nothing] => Listeners[Nothing]): Type[A] = tpe

        override def children(tpe: Type[A], f: Children[A] => Children[A]): Type[A] =
          tpe match {
            case Normal(children) => Normal(f(children))
            case Void             => Void
          }
      }
    }

    implicit def traverse[Event]: Traverse[Element[Event, *]] = new Traverse[Element[Event, *]] {
      override def traverse[G[_]: Applicative, A, B](fa: Element[Event, A])(f: A => G[B]): G[Element[Event, B]] =
        fa.tpe.traverse(f).map(tpe => fa.copy(tpe = tpe))

      override def foldLeft[A, B](fa: Element[Event, A], b: B)(f: (B, A) => B): B = fa.tpe.foldl(b)(f)

      override def foldRight[A, B](fa: Element[Event, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.tpe.foldr(lb)(f)
    }

    implicit def navigator[Event, A, B]: Navigator[Event, Element[Event, B], B] =
      new Navigator[Event, Element[Event, B], B] {
        override def attributes(element: Element[Event, B], f: Attributes => Attributes): Element[Event, B] =
          element.copy(tag = Navigator[Event, Tag[Event], Nothing].attributes(element.tag, f))

        override def listeners(element: Element[Event, B], f: Listeners[Event] => Listeners[Event]): Element[Event, B] =
          element.copy(tag = Navigator[Event, Tag[Event], Nothing].listeners(element.tag, f))

        override def children(element: Element[Event, B], f: Children[B] => Children[B]): Element[Event, B] =
          element.copy(tpe = Navigator[Nothing, Type[B], B].children(element.tpe, f))
      }
  }

  final case class Fragment[+A](children: Children[A]) extends Node[Nothing, A]

  final case class Text[+Event](value: String, listeners: Listeners[Event]) extends Node[Event, Nothing]

  implicit def traverse[Event]: Traverse[Node[Event, *]] = new Traverse[Node[Event, *]] {
    override def traverse[G[_]: Applicative, A, B](
        fa: Node[Event, A]
    )(f: A => G[B]): G[Node[Event, B]] =
      fa match {
        case node: Element[Event, A] => node.traverse(f).widen
        case node: Fragment[A]       => node.children.traverse(f).map(children => node.copy(children = children))
        case node: Text[Event]       => node.pure[G].widen
      }

    override def foldLeft[A, B](fa: Node[Event, A], b: B)(f: (B, A) => B): B =
      fa match {
        case node: Element[Event, A] => node.foldl(b)(f)
        case node: Fragment[A]       => node.children.foldl(b)(f)
        case _: Text[Event]          => b
      }

    override def foldRight[A, B](fa: Node[Event, A], lb: Eval[B])(
        f: (A, Eval[B]) => Eval[B]
    ): Eval[B] =
      fa match {
        case node: Element[Event, A] => node.foldr(lb)(f)
        case node: Fragment[A]       => node.children.foldr(lb)(f)
        case _: Text[Event]          => lb
      }
  }

  implicit def navigator[Event, A]: Navigator[Event, Node[Event, A], A] = new Navigator[Event, Node[Event, A], A] {
    override def attributes(
        node: Node[Event, A],
        f: Attributes => Attributes
    ): Node[Event, A] =
      node match {
        case node: Element[Event, A]         => Navigator[Event, Element[Event, A], A].attributes(node, f)
        case _: Fragment[A] | _: Text[Event] => node
      }

    override def listeners(
        node: Node[Event, A],
        f: Listeners[Event] => Listeners[Event]
    ): Node[Event, A] =
      node match {
        case node: Element[Event, A] => Navigator[Event, Element[Event, A], A].listeners(node, f)
        case node: Text[Event]       => node.copy(listeners = f(node.listeners))
        case _: Fragment[A]          => node
      }

    override def children(
        node: Node[Event, A],
        f: Children[A] => Children[A]
    ): Node[Event, A] =
      node match {
        case node: Element[Event, A] => Navigator[Event, Element[Event, A], A].children(node, f)
        case node: Text[Event]       => node
        case node: Fragment[A]       => node.copy(children = f(node.children))
      }
  }
}
