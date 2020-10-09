package io.taig.schelm.util

import cats.implicits._
import cats.{Applicative, Traverse}
import io.taig.schelm.data.Key
import simulacrum.typeclass

@typeclass
trait NodeTraverse[F[_]] {
  def traverseWithKey[G[_]: Applicative, A, B](fa: F[A])(f: (Key, A) => G[B]): G[F[B]]
}

object NodeTraverse {
  implicit def nested[F[_]: Traverse, G[_]: NodeTraverse]: NodeTraverse[λ[A => F[G[A]]]] =
    new NodeTraverse[λ[A => F[G[A]]]] {
      override def traverseWithKey[H[_]: Applicative, A, B](fa: F[G[A]])(f: (Key, A) => H[B]): H[F[G[B]]] =
        fa.traverse(NodeTraverse[G].traverseWithKey(_)(f))
    }
}
