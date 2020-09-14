package io.taig.schelm.css.interpreter

import cats.implicits._
import cats.{Applicative, MonadError}
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}

/** Attach styles to a given `<style>` tag */
object CssStyleAttacher {
  val Id = "schelm-css"

  def apply[F[_]: Applicative](dom: Dom[F])(parent: dom.Element): Attacher[F, Map[Selector, Style], dom.Element] =
    new Attacher[F, Map[Selector, Style], dom.Element] {
      override def attach(styles: Map[Selector, Style]): F[dom.Element] = {
        val stylesheet = CssRenderer.render(styles)
        val text = CssPrinter(stylesheet, pretty = true)
        dom.innerHtml(parent, text).as(parent)
      }
    }

  /** Create a `<style>` tag in the document's `<head>` and attach the styles to it */
  def auto[F[_]: MonadError[*[_], Throwable]](dom: Dom[F]): F[Attacher[F, Map[Selector, Style], dom.Element]] =
    for {
      style <- dom.createElement("style")
      _ <- dom.setAttribute(style, "id", Id)
      head <- dom.head.flatMap(_.liftTo[F](new IllegalStateException("head element missing")))
      _ <- dom.appendChild(head, style)
    } yield CssStyleAttacher(dom)(style)
}
