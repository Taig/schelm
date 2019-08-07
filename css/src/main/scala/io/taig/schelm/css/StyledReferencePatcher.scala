package io.taig.schelm.css

import cats.{Functor, MonadError}
import cats.implicits._
import cats.data.Ior
import io.taig.schelm._

final class StyledReferencePatcher[F[_]: Functor, Event](
    patcher: Patcher[F, Reference[Event], HtmlDiff[Event]]
) extends Patcher[F, StyledReference[Event], StyledHtmlDiff[Event]] {
  override def patch(
      reference: StyledReference[Event],
      diff: StyledHtmlDiff[Event],
      path: Path
  ): F[StyledReference[Event]] = {
    diff match {
      case Ior.Left(html) =>
        patcher
          .patch(reference.reference, html, path)
          .map(StyledReference(_, reference.stylesheet))
      case Ior.Right(stylesheet)      => ???
      case Ior.Both(html, stylesheet) => ???
    }
  }
}

object StyledReferencePatcher {
  def apply[F[_]: MonadError[?[_], Throwable], Event](
      dom: Dom[F, Event],
      renderer: Renderer[F, Html[Event], Reference[Event]]
  ): Patcher[F, StyledReference[Event], StyledHtmlDiff[Event]] =
    new StyledReferencePatcher[F, Event](ReferencePatcher(dom, renderer))
}
