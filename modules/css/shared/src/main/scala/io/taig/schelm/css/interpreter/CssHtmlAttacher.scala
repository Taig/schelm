package io.taig.schelm.css.interpreter

import cats.{Applicative, Monad}
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}
import io.taig.schelm.interpreter.NodeAttacher

final class CssHtmlAttacher[F[_]: Applicative, Event, A, B](
    html: Attacher[F, List[Dom.Node], A],
    css: Attacher[F, Map[Selector, Style], B]
) extends Attacher[F, (List[Dom.Node], Map[Selector, Style]), (A, B)] {
  override def attach(structure: (List[Dom.Node], Map[Selector, Style])): F[(A, B)] = {
    val (nodes, styles) = structure
    (html.attach(nodes), css.attach(styles)).tupled
  }
}

object CssHtmlAttacher {
  def apply[F[_]: Applicative, A, B](
      html: Attacher[F, List[Dom.Node], A],
      css: Attacher[F, Map[Selector, Style], B]
  ): Attacher[F, (List[Dom.Node], Map[Selector, Style]), (A, B)] =
    new CssHtmlAttacher(html, css)

  def default[F[_]: Monad, Event, A, B](
      main: Dom.Element,
      dom: Dom[F, _]
  ): F[Attacher[F, (List[Dom.Node], Map[Selector, Style]), (Dom.Element, Dom.Element)]] =
    CssStyleAttacher.auto(dom).map(CssHtmlAttacher(HtmlAttacher(dom, main), _))
}
