package io.taig.schelm.css.interpreter

import cats.Parallel
import cats.effect.Concurrent
import io.taig.schelm.algebra.{Dom, EventManager, Schelm}
import io.taig.schelm.css.data.CssHtml
import io.taig.schelm.interpreter.{DomSchelm, HtmlRenderer}

object CssHtmlSchelm {
  def default[F[_]: Concurrent: Parallel, View, Event, Structure, Node, Element, Diff](
      main: Element,
      style: Element,
      manager: EventManager[F, Event],
      dom: Dom.Aux[F, Event, Node, Element, _]
  ): Schelm[F, CssHtml[Event], Event] = {
    val renderer = HtmlRenderer(dom)
    val attacher = CssHtmlAttacher.default(main, style, dom)
    val differ = CssHtmlDiffer.default[Event]
    val patcher = CssHtmlPatcher.default(dom, renderer)
    DomSchelm(manager, CssHtmlRenderer(renderer), attacher, differ, patcher)
  }
}
