package io.taig.schelm.instance

import cats.{Applicative, Eval, Traverse}
import cats.syntax.all._
import io.taig.schelm.data.NodeReference

trait NodeReferenceFragmentInstances {
  implicit def nodeReferenceFragmentTraverse[F[_]]: Traverse[NodeReference.Fragment[F, *]] =
    new Traverse[NodeReference.Fragment[F, *]] {
      override def traverse[G[_]: Applicative, A, B](fa: NodeReference.Fragment[F, A])(
          f: A => G[B]
      ): G[NodeReference.Fragment[F, B]] =
        fa.node.traverse(f).map(fragment => fa.copy(node = fragment))

      override def foldLeft[A, B](fa: NodeReference.Fragment[F, A], b: B)(f: (B, A) => B): B = fa.node.foldl(b)(f)

      override def foldRight[A, B](fa: NodeReference.Fragment[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
        fa.node.foldr(lb)(f)
    }
}
