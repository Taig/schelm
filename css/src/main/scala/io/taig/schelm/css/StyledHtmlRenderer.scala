package io.taig.schelm.css

import cats._
import cats.implicits._
import io.taig.schelm._

final class StyledHtmlRenderer[F[_]: Functor, A](
    renderer: Renderer[F, Html[A], Reference[A]]
) extends Renderer[F, StyledHtml[A], StyledReference[A]] {
  override def render(
      styled: StyledHtml[A],
      path: Path
  ): F[StyledReference[A]] =
    renderer.render(toHtml(styled), path).map { reference =>
      val stylesheet = toStylesheet(styled)
      StyledReference(reference, stylesheet)
    }
}

object StyledHtmlRenderer {
  def apply[F[_]: Monad, A](
      renderer: Renderer[F, Html[A], Reference[A]]
  ): Renderer[F, StyledHtml[A], StyledReference[A]] =
    new StyledHtmlRenderer[F, A](renderer)
}
