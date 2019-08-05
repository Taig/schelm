package io.taig.schelm.css

import cats.effect.ConcurrentEffect
import cats.implicits._
import io.taig.schelm.{BrowserDom, EventManager, Schelm}
import org.scalajs.dom

object WidgetBrowserSchelm {
  def apply[F[_]: ConcurrentEffect, Event]: F[
    Schelm[
      F,
      Event,
      dom.Node,
      StyledHtml[Event],
      StyledReference[Event, dom.Node],
      StylesheetDiff
    ]
  ] =
    for {
      manager <- EventManager.unbounded[F, Event]
      dom <- BrowserDom(manager)
    } yield ???
}
