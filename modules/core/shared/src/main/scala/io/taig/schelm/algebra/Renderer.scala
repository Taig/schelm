package io.taig.schelm.algebra

abstract class Renderer[F[_], View, Structure] { self =>
  def render(view: View): F[Structure]
}
