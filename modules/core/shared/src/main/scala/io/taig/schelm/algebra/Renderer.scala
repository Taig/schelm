package io.taig.schelm.algebra

abstract class Renderer[F[+_], -View, +Structure] {
  def render(view: View): F[Structure]
}
