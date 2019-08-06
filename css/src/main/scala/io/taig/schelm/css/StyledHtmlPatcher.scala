package io.taig.schelm.css

import cats.Applicative
import cats.data.Ior
import cats.effect.Sync
import cats.implicits._
import io.taig.schelm._

final class StyledHtmlPatcher[F[_]: Applicative, Event, Node](
    html: Patcher[F, Html[Event], List[Node], HtmlDiff[Event]]
) extends Patcher[F, StyledHtml[Event], StyledNodes[Node], StyledHtmlDiff[
      Event
    ]] {
  override def patch(
      html: StyledHtml[Event],
      reference: StyledNodes[Node],
      diff: StyledHtmlDiff[Event],
      path: Path
  ): F[StyledNodes[Node]] = {
    diff match {
      case Ior.Left(diff) =>
        this.html
          .patch(toHtml(html), reference.nodes, diff, path)
          .map(StyledNodes(_, reference.stylesheet))
      case Ior.Right(diff)            => ???
      case Ior.Both(html, stylesheet) => ???
    }
  }
}

object StyledHtmlPatcher {
  def apply[F[_]: Sync, Event, Node](
      renderer: Renderer[F, Html[Event], List[Node]],
      dom: Dom[F, Event, Node]
  ): Patcher[F, StyledHtml[Event], StyledNodes[Node], StyledHtmlDiff[Event]] =
    new StyledHtmlPatcher[F, Event, Node](HtmlPatcher(renderer, dom))
}
