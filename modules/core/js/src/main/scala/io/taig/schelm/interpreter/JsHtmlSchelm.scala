package io.taig.schelm.interpreter

import cats.Parallel
import cats.effect.ConcurrentEffect
import cats.implicits._
import io.taig.schelm.algebra.Schelm
import io.taig.schelm.data.Html
import org.scalajs.dom

object JsHtmlSchelm {
  def default[F[_]: ConcurrentEffect: Parallel, Event]: F[Schelm[F, Html[Event], Event, dom.Element]] =
    QueueEventManager.unbounded[F, Event].map { manager =>
      val dom = BrowserDom(manager)
      val renderer = HtmlRenderer(dom)
      val attacher = HtmlAttacher(dom)
      val differ = HtmlDiffer[Event]
      val patcher = HtmlPatcher(dom, renderer)
      DomSchelm(manager, renderer, attacher, differ, patcher, JsHtmlPrinter)
    }
}
