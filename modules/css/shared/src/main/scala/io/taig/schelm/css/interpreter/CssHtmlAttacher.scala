package io.taig.schelm.css.interpreter

import cats.effect.LiftIO
import cats.{Applicative, Monad, MonadError}
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}
import io.taig.schelm.data.HtmlReference
import io.taig.schelm.interpreter.HtmlReferenceAttacher

object CssHtmlAttacher {
  def apply[F[_]: Applicative, Node, Element <: Node, Text <: Node, A, B](
      html: Attacher[F, HtmlReference[Node, Element, Text], A],
      css: Attacher[F, Map[Selector, Style], B]
  ): Attacher[F, (HtmlReference[Node, Element, Text], Map[Selector, Style]), (A, B)] =
    new Attacher[F, (HtmlReference[Node, Element, Text], Map[Selector, Style]), (A, B)] {
      override def attach(structure: (HtmlReference[Node, Element, Text], Map[Selector, Style])): F[(A, B)] = {
        val (nodes, styles) = structure
        (html.attach(nodes), css.attach(styles)).tupled
      }
    }

  def default[F[_]: MonadError[*[_], Throwable]: LiftIO, Event, A, B](
      dom: Dom[F]
  )(main: dom.Element): F[
    Attacher[F, (HtmlReference[dom.Node, dom.Element, dom.Text], Map[Selector, Style]), (dom.Element, dom.Element)]
  ] =
    CssStyleAttacher.auto(dom).map(CssHtmlAttacher(HtmlReferenceAttacher.default(dom)(main), _))
}
