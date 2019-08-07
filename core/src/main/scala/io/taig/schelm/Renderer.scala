package io.taig.schelm

abstract class Renderer[F[_], A, B] {
  def render(value: A, path: Path): F[B]
}
