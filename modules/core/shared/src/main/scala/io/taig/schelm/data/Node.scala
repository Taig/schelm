package io.taig.schelm.data

import scala.annotation.{nowarn, tailrec}

import cats.implicits._
import cats.{Applicative, Bifunctor, Eval, Functor, Traverse}
import io.taig.schelm.data.Path./
import io.taig.schelm.util.NodeTraverse

@nowarn("msg=shadows")
sealed abstract class Node[+F[_], +Listeners, +A] extends Product with Serializable {
  final def traverseWithKey[G[B] >: F[B], H[_]: Applicative, M >: Listeners, B](f: (Key, A) => H[B]): H[Node[G, M, B]] =
    this match {
      case node: Node.Element[F, Listeners, A] =>
        node.variant.traverseWithKey(f).map(variant => node.copy(variant = variant))
      case node: Node.Fragment[A]        => node.children.traverseWithKey(f).map(children => node.copy(children = children))
      case node: Node.Text[F, Listeners] => node.pure[H].widen
    }

  final def modifyAttributes(f: Attributes => Attributes): Node[F, Listeners, A] = this match {
    case node: Node.Element[F, Listeners, A] => node.copy(tag = node.tag.copy(attributes = f(node.tag.attributes)))
    case node                                => node
  }

  final def modifyListeners[L >: Listeners](f: L => L): Node[F, L, A] = this match {
    case node: Node.Element[F, Listeners, A] => node.copy(tag = node.tag.copy(listeners = f(node.tag.listeners)))
    case node: Node.Fragment[A]              => node
    case node: Node.Text[F, Listeners]       => node.copy(listeners = f(node.listeners))
  }

  final def modifyChildren[B >: A](f: Children[B] => Children[B]): Node[F, Listeners, B] = this match {
    case node: Node.Element[F, Listeners, A] =>
      node.variant match {
        case Node.Element.Variant.Normal(children) => node.copy(variant = Node.Element.Variant.Normal(f(children)))
        case Node.Element.Variant.Void             => node
      }
    case node => node
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
  @nowarn("msg=shadows")
  final case class Element[+F[_], +Listeners, +A](
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

    @nowarn("msg=shadows")
    implicit def traverse[F[_], Listeners]: Traverse[Element[F, Listeners, *]] =
      new Traverse[Element[F, Listeners, *]] {
        override def traverse[G[_]: Applicative, A, B](
            fa: Element[F, Listeners, A]
        )(f: A => G[B]): G[Element[F, Listeners, B]] =
          fa.variant.traverse(f).map(variant => fa.copy(variant = variant))

        override def foldLeft[A, B](fa: Element[F, Listeners, A], b: B)(f: (B, A) => B): B = fa.variant.foldl(b)(f)

        override def foldRight[A, B](fa: Element[F, Listeners, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
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

  @nowarn("msg=shadows")
  final case class Text[+F[_], +Listeners](value: String, listeners: Listeners, lifecycle: Lifecycle.Text[F])
      extends Node[F, Listeners, Nothing]

  object Text {
    implicit def functor[F[_]]: Functor[Text[F, *]] = new Functor[Text[F, *]] {
      override def map[A, B](fa: Text[F, A])(f: A => B): Text[F, B] = fa.copy(listeners = f(fa.listeners))
    }
  }

  @nowarn("msg=shadows")
  implicit def traverse[F[_], Listeners]: Traverse[Node[F, Listeners, *]] = new Traverse[Node[F, Listeners, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Node[F, Listeners, A])(f: A => G[B]): G[Node[F, Listeners, B]] =
      fa match {
        case node: Element[F, Listeners, A] => node.traverse(f).widen
        case node: Fragment[A]              => node.traverse(f).widen
        case node: Text[F, Listeners]       => node.pure[G].widen
      }

    override def foldLeft[A, B](fa: Node[F, Listeners, A], b: B)(f: (B, A) => B): B =
      fa match {
        case node: Element[F, Listeners, A] => node.foldl(b)(f)
        case node: Fragment[A]              => node.foldl(b)(f)
        case _: Text[F, Listeners]          => b
      }

    override def foldRight[A, B](fa: Node[F, Listeners, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case node: Element[F, Listeners, A] => node.foldr(lb)(f)
        case node: Fragment[A]              => node.foldr(lb)(f)
        case _: Text[F, Listeners]          => lb
      }
  }

  @nowarn("msg=shadows")
  implicit def nodes[F[_], Listeners]: NodeTraverse[Node[F, Listeners, *]] = new NodeTraverse[Node[F, Listeners, *]] {
    override def traverseWithKey[G[_]: Applicative, A, B](
        fa: Node[F, Listeners, A]
    )(f: (Key, A) => G[B]): G[Node[F, Listeners, B]] =
      fa.traverseWithKey(f)
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
