package io.taig.schelm.css.interpreter

import cats.{Functor, Monad}
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}

/** Attach styles to a given `<style>` tag */
final class CssStyleAttacher[F[_]: Functor](dom: Dom[F, _], parent: Dom.Element)
    extends Attacher[F, Map[Selector, Style], Dom.Element] {
  override def attach(styles: Map[Selector, Style]): F[Dom.Element] = {
    val stylesheet = CssRenderer.render(styles)
    val text = CssPrinter.print(stylesheet, pretty = true)
    dom.innerHtml(parent, text).as(parent)
  }
}

object CssStyleAttacher {
  val Id = "schelm-css"

  def apply[F[_]: Functor](dom: Dom[F, _], parent: Dom.Element): Attacher[F, Map[Selector, Style], Dom.Element] =
    new CssStyleAttacher(dom, parent)

  /** Create a `<style>` tag in the document's `<head>` and attach the styles to it */
  def auto[F[_]: Monad](dom: Dom[F, _]): F[Attacher[F, Map[Selector, Style], Dom.Element]] =
    for {
      style <- dom.createElement("style")
      _ <- dom.setAttribute(style, "id", Id)
      head <- dom.head
      _ <- dom.appendChild(head, style)
    } yield CssStyleAttacher(dom, style)
}
