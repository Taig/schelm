package io.taig.schelm.algebra

abstract class Renderer[F[_], A, Node] {
  def render(value: A): F[List[Node]]
}
