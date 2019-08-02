package io.taig.schelm

abstract class Renderer[F[_], A, B] {
  final def render(value: A): F[B] = render(value, Path.Empty)

  def render(value: A, path: Path): F[B]
}
