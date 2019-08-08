package io.taig.schelm.css

import cats._
import cats.data.Ior
import cats.implicits._
import io.taig.schelm._
import io.taig.schelm.css.internal.StyleHelpers

final class StyledReferencePatcher[F[_]: Monad, A](
    dom: Dom[F, A],
    html: Patcher[F, Reference[A], HtmlDiff[A]],
    stylesheet: StylesheetPatcher[F, A]
) extends Patcher[F, StyledReference[A], StyledHtmlDiff[A]] {
  override def patch(
      reference: StyledReference[A],
      diff: StyledHtmlDiff[A],
      path: Path
  ): F[StyledReference[A]] =
    StyleHelpers.getOrCreateStyleElement(dom).flatMap { style =>
      diff match {
        case Ior.Left(left) =>
          html
            .patch(reference.reference, left, path)
            .map(StyledReference(_, reference.stylesheet))
        case Ior.Right(right) =>
          stylesheet
            .patch(style, reference.stylesheet, right)
            .map(StyledReference(reference.reference, _))
        case Ior.Both(left, right) =>
          for {
            html <- html.patch(reference.reference, left, path)
            stylesheet <- stylesheet.patch(style, reference.stylesheet, right)
          } yield StyledReference(html, stylesheet)
      }
    }
}

object StyledReferencePatcher {
  def apply[F[_]: MonadError[?[_], Throwable], A](
      dom: Dom[F, A],
      renderer: Renderer[F, Html[A], Reference[A]]
  ): Patcher[F, StyledReference[A], StyledHtmlDiff[A]] =
    new StyledReferencePatcher[F, A](
      dom,
      ReferencePatcher(dom, renderer),
      StylesheetPatcher(dom)
    )
}
