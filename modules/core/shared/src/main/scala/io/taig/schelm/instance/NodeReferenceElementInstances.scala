package io.taig.schelm.instance

import cats.{Applicative, Eval, Traverse}
import cats.syntax.all._
import io.taig.schelm.data.NodeReference.Element

trait NodeReferenceElementInstances {
  implicit def nodeReferenceElementTraverse[F[_]]: Traverse[Element[F, *]] = new Traverse[Element[F, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Element[F, A])(f: A => G[B]): G[Element[F, B]] =
      fa.node.traverse(f).map(element => fa.copy(node = element))

    override def foldLeft[A, B](fa: Element[F, A], b: B)(f: (B, A) => B): B = fa.node.foldl(b)(f)

    override def foldRight[A, B](fa: Element[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa.node.foldr(lb)(f)
  }
}
