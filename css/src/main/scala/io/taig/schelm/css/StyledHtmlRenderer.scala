package io.taig.schelm.css

import cats._
import cats.implicits._
import io.taig.schelm._

final class StyledHtmlRenderer[F[_]: Functor, Event, Node](
    renderer: Renderer[F, Html[Event], List[Node]]
) extends Renderer[F, StyledHtml[Event], StyledNodes[Node]] {
  override def render(
      styled: StyledHtml[Event],
      path: Path
  ): F[StyledNodes[Node]] =
    renderer.render(toHtml(styled), path).map { nodes =>
      val stylesheet = toStylesheet(styled)
      StyledNodes(nodes, stylesheet)
    }
}

object StyledHtmlRenderer {
  def apply[F[_]: Monad, Event, Node](
      renderer: Renderer[F, Html[Event], List[Node]]
  ): Renderer[F, StyledHtml[Event], StyledNodes[Node]] =
    new StyledHtmlRenderer[F, Event, Node](renderer)
}
