package io.taig.schelm.interpreter

import cats.Parallel
import cats.effect.Concurrent
import io.taig.schelm.algebra.{Dom, EventManager, Schelm}
import io.taig.schelm.data.Html

object HtmlSchelm {
  def default[F[_]: Concurrent: Parallel, View, Event, Structure, Node, Element <: Node, Diff](
      root: Element,
      manager: EventManager[F, Event],
      dom: Dom.Aux[F, Event, Node, Element, _]
  ): Schelm[F, Html[Event], Event] = {
    val renderer = HtmlRenderer(dom)
    val attacher = HtmlAttacher(dom, root)
    val differ = HtmlDiffer[Event]
    val patcher = HtmlPatcher(dom, renderer)
    DomSchelm(manager, renderer, attacher, differ, patcher)
  }
}
