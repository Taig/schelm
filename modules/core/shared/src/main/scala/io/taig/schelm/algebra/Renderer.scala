package io.taig.schelm.algebra

import io.taig.schelm.data.Path

abstract class Renderer[F[_], -View, Structure] {
  def render(view: View): F[Structure]
}
