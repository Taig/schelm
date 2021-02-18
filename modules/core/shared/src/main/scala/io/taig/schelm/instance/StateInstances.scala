package io.taig.schelm.instance

import cats.Functor
import io.taig.schelm.data.State

trait StateInstances {
  implicit def stateFunctor[F[_]]: Functor[State[F, *]] = new Functor[State[F, *]] {
    override def map[A, B](fa: State[F, A])(f: A => B): State[F, B] = fa.map(f)
  }
}
