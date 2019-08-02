package io.taig.schelm

abstract class Renderer[F[_], Event, Node] {
  final def render(html: Html[Event]): F[Reference[Event, Node]] =
    render(html, Path.Empty)

  def render(html: Html[Event], path: Path): F[Reference[Event, Node]]
}
