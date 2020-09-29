package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Functor, Traverse}

sealed abstract class Node[F[_], +A] extends Product with Serializable

object Node {
  final case class Element[F[_], +A](tag: Tag[F], variant: Element.Variant[A], lifecycle: Lifecycle.Element[F])
      extends Node[F, A]

  object Element {

    /** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
    sealed abstract class Variant[+A] extends Product with Serializable

    object Variant {
      final case class Normal[+A](children: Children[A]) extends Variant[A]
      final case object Void extends Variant[Nothing]

      implicit val traverse: Traverse[Variant] = new Traverse[Variant] {
        override def traverse[G[_]: Applicative, A, B](fa: Variant[A])(f: A => G[B]): G[Variant[B]] =
          fa match {
            case tpe: Normal[A] => tpe.children.traverse(f).map(Normal[B])
            case Void           => Void.pure[G].widen
          }

        override def foldLeft[A, B](fa: Variant[A], b: B)(f: (B, A) => B): B = fa match {
          case tpe: Normal[A] => tpe.children.foldl(b)(f)
          case Void           => b
        }

        override def foldRight[A, B](fa: Variant[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
          case tpe: Normal[A] => tpe.children.foldr(lb)(f)
          case Void           => lb
        }
      }
    }

    implicit def traverse[F[_]]: Traverse[Element[F, *]] = new Traverse[Element[F, *]] {
      override def traverse[G[_]: Applicative, A, B](fa: Element[F, A])(f: A => G[B]): G[Element[F, B]] =
        fa.variant.traverse(f).map(variant => fa.copy(variant = variant))

      override def foldLeft[A, B](fa: Element[F, A], b: B)(f: (B, A) => B): B = fa.variant.foldl(b)(f)

      override def foldRight[A, B](fa: Element[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.variant.foldr(lb)(f)
    }
  }

  final case class Fragment[F[_], +A](children: Children[A]) extends Node[F, A]

  object Fragment {
    implicit def traverse[F[_]]: Traverse[Fragment[F, *]] = new Traverse[Fragment[F, *]] {
      override def traverse[G[_]: Applicative, A, B](fa: Fragment[F, A])(f: A => G[B]): G[Fragment[F, B]] =
        fa.children.traverse(f).map(children => fa.copy(children = children))

      override def foldLeft[A, B](fa: Fragment[F, A], b: B)(f: (B, A) => B): B = fa.children.foldl(b)(f)

      override def foldRight[A, B](fa: Fragment[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.children.foldr(lb)(f)
    }
  }

  final case class Stateful[F[_], A, +B](initial: A, render: (A => F[Unit], A) => B) extends Node[F, B]

  object Stateful {
    implicit def functor[F[_], A]: Functor[Stateful[F, A, *]] = new Functor[Stateful[F, A, *]] {
      override def map[B, C](fa: Stateful[F, A, B])(f: B => C): Stateful[F, A, C] =
        Stateful(fa.initial, (update, state) => f(fa.render(update, state)))
    }
  }

  final case class Text[F[_]](value: String, listeners: Listeners[F], lifecycle: Lifecycle.Text[F])
      extends Node[F, Nothing]

  implicit def functor[F[_]]: Functor[Node[F, *]] = new Functor[Node[F, *]] {
    override def map[A, B](fa: Node[F, A])(f: A => B): Node[F, B] = fa match {
      case component: Element[F, A]     => component.map(f).widen
      case component: Fragment[F, A]    => component.map(f).widen
      case component: Stateful[F, _, A] => component.map(f).widen
      case component: Text[F]           => component
    }
  }
}
