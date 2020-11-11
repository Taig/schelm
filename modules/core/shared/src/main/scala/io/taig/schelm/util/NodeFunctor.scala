package io.taig.schelm.util

import cats.Functor
import io.taig.schelm.data._
import simulacrum.typeclass

@typeclass
trait NodeFunctor[F[_[_], _, _]] {
  def algebra[G[_], Listeners]: Functor[F[G, Listeners, *]]

  def mapWithKey[G[_], Listeners, A, B](fa: F[G, Listeners, A])(f: (Key, A) => B): F[G, Listeners, B]

  def mapAttributes[G[_], Listeners, A](fa: F[G, Listeners, A])(f: Attributes => Attributes): F[G, Listeners, A]

  def mapListeners[G[_], A, B, C](fa: F[G, A, C])(f: A => B): F[G, B, C]

  def mapChildren[G[_], Listeners, A, B](fa: F[G, Listeners, A])(f: Children[A] => Children[B]): F[G, Listeners, B]
}
