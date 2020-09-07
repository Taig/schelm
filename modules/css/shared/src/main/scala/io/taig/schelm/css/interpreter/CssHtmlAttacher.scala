package io.taig.schelm.css.interpreter

import cats.{Applicative, Monad}
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}
import io.taig.schelm.interpreter.HtmlAttacher

final class CssHtmlAttacher[F[_]: Applicative, Event, Node, Element, A, B](
    html: Attacher[F, List[Node], A],
    css: Attacher[F, Map[Selector, Style], B]
) extends Attacher[F, (List[Node], Map[Selector, Style]), (A, B)] {
  override def attach(structure: (List[Node], Map[Selector, Style])): F[(A, B)] = {
    val (nodes, styles) = structure
    (html.attach(nodes), css.attach(styles)).tupled
  }
}

object CssHtmlAttacher {
  def apply[F[_]: Applicative, Event, Node, A, B](
      html: Attacher[F, List[Node], A],
      css: Attacher[F, Map[Selector, Style], B]
  ): Attacher[F, (List[Node], Map[Selector, Style]), (A, B)] =
    new CssHtmlAttacher(html, css)

  def default[F[_]: Monad, Event, Node, Element <: Node, A, B](
      main: Element,
      dom: Dom.Aux[F, _, Node, Element, _]
  ): F[Attacher[F, (List[Node], Map[Selector, Style]), (Element, Element)]] =
    CssStyleAttacher.auto(dom).map(CssHtmlAttacher(HtmlAttacher(dom, main), _))
}
