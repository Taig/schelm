package io.taig.schelm.interpreter

import cats.Parallel
import cats.effect.ConcurrentEffect
import cats.implicits._
import io.taig.schelm.algebra.Schelm
import io.taig.schelm.data.Html
import org.scalajs.dom

object JsHtmlSchelm {
  def default[F[_]: ConcurrentEffect: Parallel, Event]: F[Schelm[F, Html[Event], Event, dom.Element]] =
    QueueEventManager.unbounded[F, Event].map { manager => DomSchelm.default(manager, BrowserDom(manager)) }
}
