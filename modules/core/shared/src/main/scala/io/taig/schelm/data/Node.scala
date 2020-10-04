package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Bifunctor, Eval, Functor, Traverse}

sealed abstract class Node[+F[_], +Listeners, +A] extends Product with Serializable {
  final def traverseWithKey[G[α] >: F[α], H[_]: Applicative, L >: Listeners, B](f: (Key, A) => H[B]): H[Node[G, L, B]] =
    this match {
      case node: Node.Element[F, Listeners, A] =>
        node.variant.traverseWithKey(f).map(variant => node.copy(variant = variant))
      case node: Node.Fragment[A]        => node.children.traverseWithKey(f).map(children => node.copy(children = children))
      case node: Node.Text[F, Listeners] => node.pure[H].widen
    }

  final override def toString: String = this match {
    case Node.Element(tag, variant, lifecycle) =>
      s"Node.Element(tag = $tag, variant = $variant, lifecycle = $lifecycle)"
    case Node.Fragment(children) => s"Node.Fragment(children = $children)"
    case Node.Text(value, listeners, lifecycle) =>
      s"Node.Text(value = $value, listeners = $listeners, lifecycle = $lifecycle)"
  }
}

object Node {
  final case class Element[F[_], +Listeners, +A](
      tag: Tag[Listeners],
      variant: Element.Variant[A],
      lifecycle: Lifecycle.Element[F]
  ) extends Node[F, Listeners, A]

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

    implicit def traverse[F[_], Listener]: Traverse[Element[F, Listener, *]] = new Traverse[Element[F, Listener, *]] {
      override def traverse[G[_]: Applicative, A, B](
          fa: Element[F, Listener, A]
      )(f: A => G[B]): G[Element[F, Listener, B]] =
        fa.variant.traverse(f).map(variant => fa.copy(variant = variant))

      override def foldLeft[A, B](fa: Element[F, Listener, A], b: B)(f: (B, A) => B): B = fa.variant.foldl(b)(f)

      override def foldRight[A, B](fa: Element[F, Listener, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.variant.foldr(lb)(f)
    }

    implicit def bifunctor[F[_]]: Bifunctor[Element[F, *, *]] = new Bifunctor[Element[F, *, *]] {
      override def bimap[A, B, C, D](fab: Element[F, A, B])(f: A => C, g: B => D): Element[F, C, D] =
        fab.copy(tag = fab.tag.map(f), variant = fab.variant.map(g))
    }
  }

  final case class Fragment[+A](children: Children[A]) extends Node[Nothing, Nothing, A]

  object Fragment {
    implicit val traverse: Traverse[Fragment] = new Traverse[Fragment] {
      override def traverse[G[_]: Applicative, A, B](fa: Fragment[A])(f: A => G[B]): G[Fragment[B]] =
        fa.children.traverse(f).map(children => fa.copy(children = children))

      override def foldLeft[A, B](fa: Fragment[A], b: B)(f: (B, A) => B): B = fa.children.foldl(b)(f)

      override def foldRight[A, B](fa: Fragment[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.children.foldr(lb)(f)
    }
  }

  final case class Text[F[_], +Listeners](value: String, listeners: Listeners, lifecycle: Lifecycle.Text[F])
      extends Node[F, Listeners, Nothing]

  object Text {
    implicit def functor[F[_]]: Functor[Text[F, *]] = new Functor[Text[F, *]] {
      override def map[A, B](fa: Text[F, A])(f: A => B): Text[F, B] = fa.copy(listeners = f(fa.listeners))
    }
  }

  implicit def traverse[F[_], Listener]: Traverse[Node[F, Listener, *]] = new Traverse[Node[F, Listener, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Node[F, Listener, A])(f: A => G[B]): G[Node[F, Listener, B]] =
      fa match {
        case node: Element[F, Listener, A] => node.traverse(f).widen
        case node: Fragment[A]             => node.traverse(f).widen
        case node: Text[F, Listener]       => node.pure[G].widen
      }

    override def foldLeft[A, B](fa: Node[F, Listener, A], b: B)(f: (B, A) => B): B =
      fa match {
        case node: Element[F, Listener, A] => node.foldl(b)(f)
        case node: Fragment[A]             => node.foldl(b)(f)
        case _: Text[F, Listener]          => b
      }

    override def foldRight[A, B](fa: Node[F, Listener, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case node: Element[F, Listener, A] => node.foldr(lb)(f)
        case node: Fragment[A]             => node.foldr(lb)(f)
        case _: Text[F, Listener]          => lb
      }
  }

  implicit def bifunctor[F[_]]: Bifunctor[Node[F, *, *]] = new Bifunctor[Node[F, *, *]] {
    override def bimap[A, B, C, D](fab: Node[F, A, B])(f: A => C, g: B => D): Node[F, C, D] =
      fab match {
        case node: Element[F, A, B] => node.bimap(f, g)
        case node: Fragment[B]      => node.map(g)
        case node: Text[F, A]       => node.map(f)
      }
  }
}
