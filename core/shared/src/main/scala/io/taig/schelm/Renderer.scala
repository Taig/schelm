package io.taig.schelm

abstract class Renderer[F[_], A, B] {
  final def render(html: Html[A]): F[Node[A, B]] = render(html, Path.Empty)

  def render(html: Html[A], path: Path): F[Node[A, B]]
}
