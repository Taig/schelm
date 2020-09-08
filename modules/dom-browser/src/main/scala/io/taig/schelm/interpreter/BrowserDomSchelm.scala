package io.taig.schelm.interpreter

import cats.Parallel
import cats.effect.ConcurrentEffect
import cats.implicits._
import io.taig.schelm.algebra.Schelm
import io.taig.schelm.data.Html
import org.scalajs.dom

object BrowserDomSchelm {
  def default[F[_]: ConcurrentEffect: Parallel, Event](root: dom.Element): F[Schelm[F, Html[Event], Event]] =
    QueueEventManager.unbounded[F, Event].map(manager => HtmlSchelm.default(root, manager, BrowserDom(manager)))
}
