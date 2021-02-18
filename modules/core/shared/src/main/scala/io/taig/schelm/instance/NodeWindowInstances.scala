package io.taig.schelm.instance

import cats.syntax.all._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.data.Node

trait NodeWindowInstances {
  implicit def nodeWindowTraverse[F[_]]: Traverse[Node.Environment[F, *]] = new Traverse[Node.Environment[F, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Node.Environment[F, A])(
        f: A => G[B]
    ): G[Node.Environment[F, B]] =
      f(fa.value).map(value => fa.copy(value = value))

    override def foldLeft[A, B](fa: Node.Environment[F, A], b: B)(f: (B, A) => B): B = f(b, fa.value)

    override def foldRight[A, B](fa: Node.Environment[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      f(fa.value, lb)
  }
}
