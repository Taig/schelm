package io.taig.schelm.util

import cats.{Applicative, Traverse}
import io.taig.schelm.data.Key
import simulacrum.typeclass

@typeclass
trait NodeTraverse[F[_]] extends Traverse[F] {
  def traverseWithKey[G[_]: Applicative, A, B](fa: F[A])(f: (Key, A) => G[B]): G[F[B]]
}
