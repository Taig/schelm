package io.taig.schelm.instance

import cats.syntax.all._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.data.Node
import io.taig.schelm.data.Node.Fragment

trait NodePortalInstances {
  implicit def nodePortalTraverse[F[_]]: Traverse[Node.Portal[F, *]] = new Traverse[Node.Portal[F, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: Node.Portal[F, A])(f: A => G[B]): G[Node.Portal[F, B]] =
      f(fa.value).map(value => fa.copy(value = value))

    override def foldLeft[A, B](fa: Node.Portal[F, A], b: B)(f: (B, A) => B): B = f(b, fa.value)

    override def foldRight[A, B](fa: Node.Portal[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      f(fa.value, lb)
  }
}
