package io.taig.schelm.css

import cats._
import cats.implicits._
import io.taig.schelm._

final class StyledHtmlRenderer[F[_]: Functor, A](
    renderer: Renderer[F, Html[A], Reference[A]]
) extends Renderer[F, StylesheetWidget[A], StyledReference[A]] {
  override def render(
      styled: StylesheetWidget[A],
      parent: Option[Element],
      path: Path
  ): F[StyledReference[A]] =
    renderer.render(toHtml(styled), parent, path).map { reference =>
      StyledReference(reference, styled.merge)
    }
}

object StyledHtmlRenderer {
  def apply[F[_]: Monad, A](
      renderer: Renderer[F, Html[A], Reference[A]]
  ): Renderer[F, StylesheetWidget[A], StyledReference[A]] =
    new StyledHtmlRenderer[F, A](renderer)
}
