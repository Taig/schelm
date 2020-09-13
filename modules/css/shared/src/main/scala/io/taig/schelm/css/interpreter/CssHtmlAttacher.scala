package io.taig.schelm.css.interpreter

import cats.effect.Sync
import cats.{Applicative, Monad}
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}
import io.taig.schelm.data.HtmlReference
import io.taig.schelm.interpreter.{HtmlReferenceAttacher, NodeAttacher}

object CssHtmlAttacher {
  def apply[F[_]: Applicative, A, B](
      html: Attacher[F, HtmlReference[F], A],
      css: Attacher[F, Map[Selector, Style], B]
  ): Attacher[F, (HtmlReference[F], Map[Selector, Style]), (A, B)] =
    new Attacher[F, (HtmlReference[F], Map[Selector, Style]), (A, B)] {
      override def attach(structure: (HtmlReference[F], Map[Selector, Style])): F[(A, B)] = {
        val (nodes, styles) = structure
        (html.attach(nodes), css.attach(styles)).tupled
      }
    }

  def default[F[_]: Sync, Event, A, B](
      dom: Dom
  )(main: Dom.Element): F[Attacher[F, (HtmlReference[F], Map[Selector, Style]), (Dom.Element, Dom.Element)]] =
    CssStyleAttacher.auto(dom).map(CssHtmlAttacher(HtmlReferenceAttacher.default(dom)(main), _))
}
