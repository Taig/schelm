package io.taig.schelm.dsl.util

import cats.Functor

object Functors {
  implicit final def compose2[F[_]: Functor, G[_]: Functor]: Functor[λ[A => F[G[A]]]] =
    Functor[F].compose[G]

  implicit final def compose3[F[_]: Functor, G[_]: Functor, H[_]: Functor]: Functor[λ[A => F[G[H[A]]]]] =
    Functor[F].compose[G].compose[H]

  implicit final def compose4[F[_]: Functor, G[_]: Functor, H[_]: Functor, I[_]: Functor]
      : Functor[λ[A => F[G[H[I[A]]]]]] =
    Functor[F].compose[G].compose[H].compose[I]

  implicit final def compose5[F[_]: Functor, G[_]: Functor, H[_]: Functor, I[_]: Functor, J[_]: Functor]
      : Functor[λ[A => F[G[H[I[J[A]]]]]]] =
    Functor[F].compose[G].compose[H].compose[I].compose[J]
}
