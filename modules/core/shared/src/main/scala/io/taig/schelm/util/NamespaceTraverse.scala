package io.taig.schelm.util

import cats.{Applicative, Traverse}
import io.taig.schelm.data.Identification
import simulacrum.typeclass

@typeclass
trait NamespaceTraverse[F[_]] extends NamespaceFunctor[F] with Traverse[F] {
  def foldLeftWithIdentification[A, B](fa: F[A], b: B)(f: (B, Identification, A) => B): B

  def traverseWithIdentification[G[_]: Applicative, A, B](fa: F[A])(f: (Identification, A) => G[B]): G[F[B]]
}
