package io.taig.schelm.interpreter

import cats.effect.ConcurrentEffect
import cats.implicits._
import io.taig.schelm.algebra.Schelm
import io.taig.schelm.data.Html
import org.scalajs.dom

object JsHtmlSchelm {
  def default[F[_]: ConcurrentEffect, Event]: F[Schelm[F, Html[Event], Event, dom.Element]] =
    QueueEventManager.unbounded[F, Event].map { manager =>
      val dom = BrowserDom(manager)
      val renderer = HtmlRenderer(dom)
      val differ = HtmlDiffer[Event]
      val patcher = HtmlPatcher(dom, renderer)
      DomSchelm(dom, manager, renderer, differ, patcher)
    }
}
