package io.taig.schelm.algebra

import cats.~>

abstract class Renderer[F[_], View, Structure] { self =>
  def render(view: View): F[Structure]

  final def mapK[G[_]](fK: F ~> G): Renderer[G, View, Structure] =
    new Renderer[G, View, Structure] {
      override def render(view: View): G[Structure] = fK(self.render(view))
    }
}
