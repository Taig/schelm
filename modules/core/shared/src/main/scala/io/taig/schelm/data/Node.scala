package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

sealed abstract class Node[+Event, +A] extends Product with Serializable

object Node {
  implicit def traverse[Event]: Traverse[Node[Event, *]] = new Traverse[Node[Event, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Node[Event, A])(f: A => G[B]): G[Node[Event, B]] =
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

    override def foldRight[A, B](fa: Node[Event, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case node: Element[Event, A] => node.foldr(lb)(f)
        case node: Fragment[A]       => node.children.foldr(lb)(f)
        case _: Text[Event]          => lb
      }
  }
}

sealed abstract class Element[+Event, +A] extends Node[Event, A]

/** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
object Element {
  final case class Normal[+Event, +A](tag: Tag[Event], children: Children[A]) extends Element[Event, A]

  final case class Void[Event](tag: Tag[Event]) extends Element[Event, Nothing]

  implicit def traverse[Event]: Traverse[Element[Event, *]] = new Traverse[Element[Event, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Element[Event, A])(f: A => G[B]): G[Element[Event, B]] =
      fa match {
        case element: Element.Normal[Event, A] =>
          element.children.traverse(f).map(children => element.copy(children = children))
        case element: Element.Void[Event] => element.pure[G].widen
      }

    override def foldLeft[A, B](fa: Element[Event, A], b: B)(f: (B, A) => B): B =
      fa match {
        case element: Element.Normal[Event, A] => element.children.foldl(b)(f)
        case _: Element.Void[Event]            => b
      }

    override def foldRight[A, B](fa: Element[Event, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case element: Element.Normal[Event, A] => element.children.foldr(lb)(f)
        case _: Element.Void[Event]            => lb
      }
  }
}

final case class Fragment[A](children: Children[A]) extends Node[Nothing, A]

final case class Text[Event](value: String, listeners: Listeners[Event]) extends Node[Event, Nothing]
