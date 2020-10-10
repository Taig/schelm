package io.taig.schelm.css.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{Monad, MonadError}
import io.taig.schelm.algebra.{Attacher, Dom, Renderer}
import io.taig.schelm.css.data.{Selector, Style, Stylesheet}

/** Attach styles to a given `<style>` tag */
object StylesheetAttacher {
  val Id = "schelm-css"

  def apply[F[_]: Monad](dom: Dom[F], renderer: Renderer[F, Map[Selector, Style], Stylesheet])(
      parent: Dom.Element
  ): Attacher[F, Stylesheet, Dom.Element] = Kleisli { stylesheet =>
    val text = CssPrinter(stylesheet, pretty = true)
    dom.innerHtml(parent, text).as(parent)
  }

  def default[F[_]: Monad](dom: Dom[F])(root: Dom.Element): Attacher[F, Stylesheet, Dom.Element] =
    StylesheetAttacher(dom, CssRenderer[F])(root)

  /** Create a `<style>` tag in the document's `<head>` and attach the styles to it */
  def auto[F[_]: MonadError[*[_], Throwable]](
      dom: Dom[F],
      renderer: Renderer[F, Map[Selector, Style], Stylesheet]
  ): F[Attacher[F, Stylesheet, Dom.Element]] =
    for {
      style <- dom.createElement("style")
      _ <- dom.setAttribute(style, "id", Id)
      head <- dom.head.flatMap(_.liftTo[F](new IllegalStateException("head element missing")))
      _ <- dom.appendChild(head, style)
    } yield StylesheetAttacher(dom, renderer)(style)
}
