package io.taig.schelm.css.interpreter

import cats.data.Kleisli
import cats.implicits._
import cats.{Monad, MonadError}
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.Stylesheet

object StylesheetAttacher {
  val Id = "schelm-css"

  /** Attach styles to a given `<style>` tag */
  def apply[F[_]: Monad](dom: Dom[F])(root: Dom.Element): Attacher[F, Stylesheet, Dom.Element] = Kleisli { stylesheet =>
    val text = CssPrinter(stylesheet, pretty = true)
    dom.innerHtml(root, text).as(root)
  }

  /** Create a `<style>` tag in the document's `<head>` and attach the styles to it */
  def auto[F[_]: MonadError[*[_], Throwable]](dom: Dom[F]): F[Attacher[F, Stylesheet, Dom.Element]] =
    for {
      style <- dom.createElement("style")
      _ <- dom.setAttribute(style, "id", Id)
      head <- dom.head.flatMap(_.liftTo[F](new IllegalStateException("head element missing")))
      _ <- dom.appendChild(head, style)
    } yield StylesheetAttacher(dom)(style)
}
