package io.taig.schelm.instance

import cats.syntax.all._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.data.Node
import io.taig.schelm.data.Node.{Element, Fragment, Text}

trait NodeInstances {
  implicit def nodeTraverse[F[_]]: Traverse[Node[F, *]] = new Traverse[Node[F, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Node[F, A])(f: A => G[B]): G[Node[F, B]] = fa match {
      case node: Node.Element[F, A]     => node.traverse(f).widen
      case node: Node.Fragment[F, A]    => node.traverse(f).widen
      case node: Node.Text[F]           => node.pure[G].widen
      case node: Node.Environment[F, A] => node.traverse(f).widen
      case node: Node.Portal[F, A]      => node.traverse(f).widen
    }

    override def foldLeft[A, B](fa: Node[F, A], b: B)(f: (B, A) => B): B = fa match {
      case node: Node.Element[F, A]     => node.foldl(b)(f)
      case node: Node.Fragment[F, A]    => node.foldl(b)(f)
      case _: Node.Text[F]              => b
      case node: Node.Environment[F, A] => node.foldl(b)(f)
      case node: Node.Portal[F, A]      => node.foldl(b)(f)
    }

    override def foldRight[A, B](fa: Node[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
      case node: Node.Element[F, A]     => node.foldr(lb)(f)
      case node: Node.Fragment[F, A]    => node.foldr(lb)(f)
      case _: Node.Text[F]              => lb
      case node: Node.Environment[F, A] => node.foldr(lb)(f)
      case node: Node.Portal[F, A]      => node.foldr(lb)(f)
    }
  }
}
