package io.taig.schelm.css.interpreter

import cats.{Applicative, Monad}
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.css.data.{CssHtml, Selector, Style}
import io.taig.schelm.data.Html
import io.taig.schelm.interpreter.HtmlRenderer

final class CssHtmlRenderer[F[_]: Applicative, Event](renderer: Renderer[F, Html[Event], List[Dom.Node]])
    extends Renderer[F, CssHtml[Event], (List[Dom.Node], Map[Selector, Style])] {
  override def render(view: CssHtml[Event]): F[(List[Dom.Node], Map[Selector, Style])] = {
    val (html, stylesheet) = CssHtml.toHtml(view)
    renderer.render(html).tupleRight(stylesheet)
  }
}

object CssHtmlRenderer {
  def apply[F[_]: Applicative, Event](
      renderer: Renderer[F, Html[Event], List[Dom.Node]]
  ): Renderer[F, CssHtml[Event], (List[Dom.Node], Map[Selector, Style])] = new CssHtmlRenderer(renderer)

  def default[F[_]: Monad, Event](
      dom: Dom[F, Event]
  ): Renderer[F, CssHtml[Event], (List[Dom.Node], Map[Selector, Style])] =
    CssHtmlRenderer(HtmlRenderer(dom))
}
