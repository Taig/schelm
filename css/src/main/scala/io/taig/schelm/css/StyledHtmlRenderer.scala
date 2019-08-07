package io.taig.schelm.css

import cats._
import cats.implicits._
import io.taig.schelm._

final class StyledHtmlRenderer[F[_]: Functor, Event](
    renderer: Renderer[F, Html[Event], Reference[Event]]
) extends Renderer[F, StyledHtml[Event], StyledReference[Event]] {
  override def render(
      styled: StyledHtml[Event],
      path: Path
  ): F[StyledReference[Event]] =
    renderer.render(toHtml(styled), path).map { reference =>
      val stylesheet = toStylesheet(styled)
      StyledReference(reference, stylesheet)
    }
}

object StyledHtmlRenderer {
  def apply[F[_]: Monad, Event, Node](
      renderer: Renderer[F, Html[Event], Reference[Event]]
  ): Renderer[F, StyledHtml[Event], StyledReference[Event]] =
    new StyledHtmlRenderer[F, Event](renderer)
}
