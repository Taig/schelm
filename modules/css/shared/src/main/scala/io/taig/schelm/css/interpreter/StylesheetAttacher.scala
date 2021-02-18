package io.taig.schelm.css.interpreter

import cats.Monad
import cats.syntax.all._
import io.taig.schelm.algebra.Dom.Element
import io.taig.schelm.algebra.{Attacher, Dom, Renderer}
import io.taig.schelm.css.data.Stylesheet

final class StylesheetAttacher[F[_]: Monad](renderer: Renderer[F, Stylesheet, String], dom: Dom[F])(root: Dom.Element)
    extends Attacher[F, Stylesheet, Dom.Element] {
  override def attach(stylesheet: Stylesheet): F[Element] =
    for {
      css <- renderer.render(stylesheet)
      _ <- dom.innerHtml(root, css)
    } yield root
}

object StylesheetAttacher {
  def apply[F[_]: Monad](renderer: Renderer[F, Stylesheet, String], dom: Dom[F])(
      root: Dom.Element
  ): Attacher[F, Stylesheet, Dom.Element] =
    new StylesheetAttacher[F](renderer, dom)(root)

  def default[F[_]: Monad](dom: Dom[F])(root: Dom.Element): Attacher[F, Stylesheet, Dom.Element] =
    StylesheetAttacher[F](StylesheetRenderer(pretty = false), dom)(root)
}
