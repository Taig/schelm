package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

sealed abstract class Node[+Event, +A] extends Product with Serializable

object Node {
  final case class Element[+Event, +A](
      tag: Tag[Event],
      tpe: Element.Type[A],
      lifecycle: Lifecycle.Element[Event]
  ) extends Node[Event, A]

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
    }

    implicit def traverse[Event]: Traverse[Element[Event, *]] = new Traverse[Element[Event, *]] {
      override def traverse[G[_]: Applicative, A, B](fa: Element[Event, A])(f: A => G[B]): G[Element[Event, B]] =
        fa.tpe.traverse(f).map(tpe => fa.copy(tpe = tpe))

      override def foldLeft[A, B](fa: Element[Event, A], b: B)(f: (B, A) => B): B = fa.tpe.foldl(b)(f)

      override def foldRight[A, B](fa: Element[Event, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.tpe.foldr(lb)(f)
    }
  }

  final case class Fragment[+Event, +A](children: Children[A], lifecycle: Lifecycle.Fragment[Event])
      extends Node[Event, A]

  object Fragment {
    implicit def traverse[Event]: Traverse[Fragment[Event, *]] = new Traverse[Fragment[Event, *]] {
      override def traverse[G[_]: Applicative, A, B](fa: Fragment[Event, A])(f: A => G[B]): G[Fragment[Event, B]] =
        fa.children.traverse(f).map(children => fa.copy(children = children))

      override def foldLeft[A, B](fa: Fragment[Event, A], b: B)(f: (B, A) => B): B = fa.children.foldl(b)(f)

      override def foldRight[A, B](fa: Fragment[Event, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.children.foldr(lb)(f)
    }
  }

  final case class Text[+Event](value: String, listeners: Listeners[Event], lifecycle: Lifecycle.Text[Event])
      extends Node[Event, Nothing]

  implicit def traverse[Event]: Traverse[Node[Event, *]] = new Traverse[Node[Event, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Node[Event, A])(f: A => G[B]): G[Node[Event, B]] =
      fa match {
        case component: Element[Event, A]  => component.traverse(f).widen
        case component: Fragment[Event, A] => component.traverse(f).widen
        case component: Text[Event]        => component.pure[G].widen
      }

    override def foldLeft[A, B](fa: Node[Event, A], b: B)(f: (B, A) => B): B =
      fa match {
        case component: Element[Event, A]  => component.foldl(b)(f)
        case component: Fragment[Event, A] => component.foldl(b)(f)
        case _: Text[Event]                => b
      }

    override def foldRight[A, B](fa: Node[Event, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case component: Element[Event, A]  => component.foldr(lb)(f)
        case component: Fragment[Event, A] => component.foldr(lb)(f)
        case _: Text[Event]                => lb
      }
  }
}
