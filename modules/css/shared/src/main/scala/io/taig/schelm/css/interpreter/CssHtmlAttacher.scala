package io.taig.schelm.css.interpreter

import cats.effect.LiftIO
import cats.implicits._
import cats.{Applicative, MonadError}
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.css.data.{Selector, Style}
import io.taig.schelm.data.{HtmlAttachedReference, HtmlReference}
import io.taig.schelm.interpreter.HtmlReferenceAttacher

object CssHtmlAttacher {
  def apply[F[_]: Applicative, Event, Node, Element <: Node, Text <: Node, A, B](
      html: Attacher[F, HtmlReference[Event, Node, Element, Text], A],
      css: Attacher[F, Map[Selector, Style], B]
  ): Attacher[F, (HtmlReference[Event, Node, Element, Text], Map[Selector, Style]), (A, B)] =
    new Attacher[F, (HtmlReference[Event, Node, Element, Text], Map[Selector, Style]), (A, B)] {
      override def attach(structure: (HtmlReference[Event, Node, Element, Text], Map[Selector, Style])): F[(A, B)] = {
        val (nodes, styles) = structure
        (html.attach(nodes), css.attach(styles)).tupled
      }
    }

  def default[F[_]: MonadError[*[_], Throwable]: LiftIO, Event](dom: Dom[F])(main: dom.Element): F[
    Attacher[
      F,
      (HtmlReference[Event, dom.Node, dom.Element, dom.Text], Map[Selector, Style]),
      (HtmlAttachedReference[Event, dom.Node, dom.Element, dom.Text], dom.Element)
    ]
  ] = CssStyleAttacher.auto(dom).map(CssHtmlAttacher(HtmlReferenceAttacher.default(dom)(main), _))
}
