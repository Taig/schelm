package io.taig.schelm.util

import cats.Functor
import io.taig.schelm.data.{Identification, Path}
import simulacrum.typeclass

@typeclass
trait NamespaceFunctor[F[_]] extends Functor[F] {
  def mapWithIdentification[A, B](fa: F[A])(f: (Identification, A) => B): F[B]

  def mapWithPath[A, B](fa: F[A])(f: (Path, A) => B): F[B]
}
