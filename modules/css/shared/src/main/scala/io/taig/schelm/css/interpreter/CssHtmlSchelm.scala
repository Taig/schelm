package io.taig.schelm.css.interpreter

import cats.Parallel
import cats.implicits._
import cats.effect.Concurrent
import io.taig.schelm.algebra.{Dom, EventManager, Schelm}
import io.taig.schelm.css.data.CssHtml
import io.taig.schelm.interpreter.{DomSchelm, HtmlAttacher, HtmlRenderer}

object CssHtmlSchelm {
  def default[F[_]: Concurrent: Parallel, View, Event, Structure, Node, Element <: Node, Diff](
      main: Element,
      manager: EventManager[F, Event],
      dom: Dom.Aux[F, Event, Node, Element, _]
  ): F[Schelm[F, CssHtml[Event], Event]] =
    CssHtmlAttacher.default(main, dom).map { attacher =>
      val renderer = HtmlRenderer(dom)
      val differ = CssHtmlDiffer.default[Event]
      val patcher = CssHtmlPatcher.default(dom, renderer)
      DomSchelm(manager, CssHtmlRenderer(renderer), attacher, differ, patcher)
    }
}
