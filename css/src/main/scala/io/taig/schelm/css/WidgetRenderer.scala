package io.taig.schelm.css

import cats._
import cats.implicits._
import io.taig.schelm.{Dom, Html, HtmlRenderer, Path, Reference, Renderer}

final class WidgetRenderer[F[_]: Functor, Event, Node](
    renderer: Renderer[F, Html[Event], Reference[Event, Node]]
) extends Renderer[F, Widget[Event], StyledReference[Event, Node]] {
  override def render(
      widget: Widget[Event],
      path: Path
  ): F[StyledReference[Event, Node]] = {
    val (html, stylesheet) = widget.render
    renderer.render(html, path).map(StyledReference(_, stylesheet))
  }
}

object WidgetRenderer {
  def apply[F[_]: Monad, Event, Node](
      dom: Dom[F, Event, Node]
  ): Renderer[F, Widget[Event], StyledReference[Event, Node]] =
    new WidgetRenderer[F, Event, Node](HtmlRenderer(dom))
}
