package io.taig.schelm.instance

import cats.syntax.all._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.data.Node
import io.taig.schelm.data.Node.Element

trait NodeElementInstances {
  implicit def nodeElementTraverse[F[_]]: Traverse[Node.Element[F, *]] = new Traverse[Node.Element[F, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Element[F, A])(f: A => G[B]): G[Element[F, B]] =
      fa.variant.traverse(f).map(variant => fa.copy(variant = variant))

    override def foldLeft[A, B](fa: Element[F, A], b: B)(f: (B, A) => B): B =
      fa.variant.foldl(b)(f)

    override def foldRight[A, B](fa: Element[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa.variant.foldr(lb)(f)
  }
}
