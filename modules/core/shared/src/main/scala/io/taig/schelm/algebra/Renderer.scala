package io.taig.schelm.algebra

import cats.~>

abstract class Renderer[F[_], A, B] {
  def render(value: A): F[B]

  final def mapK[G[_]](fk: F ~> G): Renderer[G, A, B] = value => fk(render(value))
}
