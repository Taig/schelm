package io.taig.schelm.css.interpreter

import cats.implicits._
import cats.{Applicative, Monad}
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.css.data.{CssHtml, Selector, Style}
import io.taig.schelm.data.{Html, HtmlReference}
import io.taig.schelm.interpreter.HtmlRenderer

object CssHtmlRenderer {
  def apply[F[_]: Applicative, Event](
      renderer: Renderer[F, Html[Event], HtmlReference[Event]]
  ): Renderer[F, CssHtml[Event], (HtmlReference[Event], Map[Selector, Style])] = { view =>
    val (html, stylesheet) = CssHtml.toHtml(view)
    renderer.render(html).tupleRight(stylesheet)
  }

  def default[F[_]: Monad, Event](
      dom: Dom[F]
  ): Renderer[F, CssHtml[Event], (HtmlReference[Event], Map[Selector, Style])] =
    CssHtmlRenderer(HtmlRenderer(dom))
}
