package io.taig.schelm.css.interpreter

import cats.Parallel
import cats.effect.Concurrent
import io.taig.schelm.algebra.{Dom, EventManager, Schelm}
import io.taig.schelm.css.data.CssHtml

object CssHtmlSchelm {
  def default[F[_]: Concurrent: Parallel, View, Event, Structure, Node, Element, Diff](
      main: Element,
      style: Element,
      manager: EventManager[F, Event],
      dom: Dom.Aux[F, Event, Node, Element, _]
  ): Schelm[F, CssHtml[Event], Event] = {
    val renderer = CssHtmlRenderer.default(dom)
    val attacher = CssHtmlAttacher.default(main, style, dom)

    ???
//    val differ = HtmlDiffer[Event]
//    val patcher = HtmlPatcher(dom, renderer)
//    DomSchelm(manager, renderer, attacher, differ, patcher)
  }
}
