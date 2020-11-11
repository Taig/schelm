package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}

import scala.annotation.nowarn

@nowarn("msg=shadows")
sealed abstract class Node[+F[_], +A] extends Product with Serializable {
  final override def toString: String = this match {
    case Node.Element(tag, variant, lifecycle) =>
      s"Node.Element(tag = $tag, variant = $variant, lifecycle = $lifecycle)"
    case Node.Fragment(children) => s"Node.Fragment(children = $children)"
    case Node.Text(value, listeners, lifecycle) =>
      s"Node.Text(value = $value, listeners = $listeners, lifecycle = $lifecycle)"
  }
}

object Node {
  @nowarn("msg=shadows")
  final case class Element[+F[_], +A](tag: Tag[F], variant: Element.Variant[A], lifecycle: Lifecycle.Element[F])
      extends Node[F, A]

  object Element {

    /** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
    sealed abstract class Variant[+A] extends Product with Serializable {
      final def traverseWithKey[G[_]: Applicative, B](f: (Key, A) => G[B]): G[Variant[B]] =
        this match {
          case Variant.Normal(children) => children.traverseWithKey(f).map(Variant.Normal(_))
          case Variant.Void             => Variant.Void.pure[G].widen
        }
    }

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

    implicit def traverse[F[_]]: Traverse[Element[F, *]] =
      new Traverse[Element[F, *]] {
        override def traverse[G[_]: Applicative, A, B](
            fa: Element[F, A]
        )(f: A => G[B]): G[Element[F, B]] =
          fa.variant.traverse(f).map(variant => fa.copy(variant = variant))

        override def foldLeft[A, B](fa: Element[F, A], b: B)(f: (B, A) => B): B = fa.variant.foldl(b)(f)

        override def foldRight[A, B](fa: Element[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
          fa.variant.foldr(lb)(f)
      }
  }

  final case class Fragment[+A](children: Children[A]) extends Node[Nothing, A]

  object Fragment {
    implicit val traverse: Traverse[Fragment] = new Traverse[Fragment] {
      override def traverse[G[_]: Applicative, A, B](fa: Fragment[A])(f: A => G[B]): G[Fragment[B]] =
        fa.children.traverse(f).map(children => fa.copy(children = children))

      override def foldLeft[A, B](fa: Fragment[A], b: B)(f: (B, A) => B): B = fa.children.foldl(b)(f)

      override def foldRight[A, B](fa: Fragment[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.children.foldr(lb)(f)
    }
  }

  final case class Text[+F[_]](value: String, listeners: Listeners[F], lifecycle: Lifecycle.Text[F])
      extends Node[F, Nothing]
}
