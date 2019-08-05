package io.taig.schelm.css

import io.taig.schelm._

final class WidgetRenderer[F[_], Event, Node](
    renderer: Renderer[F, Html[Event], Reference[Event, Node]]
) extends Renderer[F, StyledHtml[Event], StyledReference[Event, Node]] {
  override def render(
      styled: StyledHtml[Event],
      path: Path
  ): F[StyledReference[Event, Node]] = {
    renderer.render(toHtml(styled), path)
    ???
  }
}

object WidgetRenderer {
  def apply[F[_], Event, Node](
      renderer: Renderer[F, Html[Event], Reference[Event, Node]]
  ): Renderer[F, StyledHtml[Event], StyledReference[Event, Node]] =
    new WidgetRenderer[F, Event, Node](renderer)
}
