package io.taig.schelm.instance

import cats.syntax.all._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.data.Node
import io.taig.schelm.data.Node.Fragment

trait NodeFragmentInstances {
  implicit def nodeFragmentTraverse[F[_]]: Traverse[Node.Fragment[F, *]] = new Traverse[Node.Fragment[F, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Fragment[F, A])(f: A => G[B]): G[Fragment[F, B]] =
      fa.children.traverse(f).map(children => fa.copy(children = children))

    override def foldLeft[A, B](fa: Fragment[F, A], b: B)(f: (B, A) => B): B = fa.children.foldl(b)(f)

    override def foldRight[A, B](fa: Fragment[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa.children.foldr(lb)(f)
  }
}
