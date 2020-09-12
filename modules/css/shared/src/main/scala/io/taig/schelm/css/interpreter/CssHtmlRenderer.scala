package io.taig.schelm.css.interpreter

import cats.effect.Sync
import cats.{Applicative, Monad}
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.css.data.{CssHtml, Selector, Style}
import io.taig.schelm.data.{Html, HtmlReference}
import io.taig.schelm.interpreter.HtmlRenderer

object CssHtmlRenderer {
  def apply[F[_]: Applicative, Node, Element <: Node, Text <: Node](
      renderer: Renderer[F, Html[F], HtmlReference[F, Node, Element, Text]]
  ): Renderer[F, CssHtml[F], (HtmlReference[F, Node, Element, Text], Map[Selector, Style])] = { view =>
    val (html, stylesheet) = CssHtml.toHtml(view)
    renderer.render(html).tupleRight(stylesheet)
  }

  def default[F[_]: Sync](dom: Dom): Renderer[F, CssHtml[F], (HtmlReference[F, dom.Node, dom.Element, dom.Text], Map[Selector, Style])] =
    CssHtmlRenderer(HtmlRenderer(dom))
}
