package io.taig.schelm.instance

import cats.syntax.all._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.data.NodeReference
import io.taig.schelm.data.NodeReference.{Element, Fragment, Text}

trait NodeReferenceInstances {
  implicit def nodeReferenceTraverse[F[_]]: Traverse[NodeReference[F, *]] = new Traverse[NodeReference[F, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: NodeReference[F, A])(f: A => G[B]): G[NodeReference[F, B]] =
      fa match {
        case reference: Element[F, A]  => reference.traverse(f).widen
        case reference: Fragment[F, A] => reference.traverse(f).widen
        case reference: Text[F]        => reference.pure[G].widen
      }

    override def foldLeft[A, B](fa: NodeReference[F, A], b: B)(f: (B, A) => B): B = fa match {
      case reference: Element[F, A]  => reference.foldl(b)(f)
      case reference: Fragment[F, A] => reference.foldl(b)(f)
      case _: Text[F]                => b
    }

    override def foldRight[A, B](fa: NodeReference[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
      case reference: Element[F, A]  => reference.foldr(lb)(f)
      case reference: Fragment[F, A] => reference.foldr(lb)(f)
      case _: Text[F]                => lb
    }
  }
}
