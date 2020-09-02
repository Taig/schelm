package io.taig.schelm.data

import cats.{Applicative, Eval, Functor, Traverse}
import cats.implicits._

sealed abstract class Node[+A, +Event] extends Product with Serializable

object Node {
  implicit def functor[Event]: Functor[Node[*, Event]] = ???

  implicit def traverse[Event]: Traverse[Node[*, Event]] = new Traverse[Node[*, Event]] {
    override def traverse[G[_]: Applicative, A, B](fa: Node[A, Event])(f: A => G[B]): G[Node[B, Event]] =
      fa match {
        case node: Element.Normal[A, Event] => node.children.traverse(f).map(children => node.copy(children = children))
        case node: Element.Void[Event]      => node.pure[G].widen
        case node: Fragment[A]              => node.children.traverse(f).map(children => node.copy(children = children))
        case node: Text[Event]              => node.pure[G].widen
      }

    override def foldLeft[A, B](fa: Node[A, Event], b: B)(f: (B, A) => B): B = fa match {
      case node: Element.Normal[A, Event] => node.children.foldl(b)(f)
      case _: Element.Void[Event]         => b
      case node: Fragment[A]              => node.children.foldl(b)(f)
      case _: Text[Event]                 => b
    }

    override def foldRight[A, B](fa: Node[A, Event], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case node: Element.Normal[A, Event] => node.children.foldr(lb)(f)
        case _: Element.Void[Event]         => lb
        case node: Fragment[A]              => node.children.foldr(lb)(f)
        case _: Text[Event]                 => lb
      }
  }
}

/** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
object Element {
  final case class Normal[A, Event](tag: Tag[Event], children: Children[A]) extends Node[A, Event]

  final case class Void[Event](tag: Tag[Event]) extends Node[Nothing, Event]
}

final case class Fragment[A](children: Children[A]) extends Node[A, Nothing]

final case class Text[Event](value: String, listeners: Listeners[Event]) extends Node[Nothing, Event]
