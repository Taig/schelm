package io.taig.schelm.css.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}
import io.taig.schelm.interpreter.HtmlAttacher

final class CssHtmlAttacher[F[_]: Applicative, Event, Node, Element](
    html: Attacher[F, List[Node]],
    css: Attacher[F, Map[Selector, Style]]
) extends Attacher[F, (List[Node], Map[Selector, Style])] {
  override def attach(structure: (List[Node], Map[Selector, Style])): F[Unit] = {
    val (nodes, styles) = structure
    css.attach(styles) *> html.attach(nodes)
  }
}

object CssHtmlAttacher {
  def apply[F[_]: Applicative, Event, Node](
      html: Attacher[F, List[Node]],
      css: Attacher[F, Map[Selector, Style]]
  ): Attacher[F, (List[Node], Map[Selector, Style])] =
    new CssHtmlAttacher(html, css)

  def default[F[_]: Applicative, Event, Node, Element](
      main: Element,
      style: Element,
      dom: Dom.Aux[F, _, Node, Element, _]
  ): Attacher[F, (List[Node], Map[Selector, Style])] =
    CssHtmlAttacher(HtmlAttacher(dom, main), CssAttacher(dom, style))
}
