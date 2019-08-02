package io.taig.schelm

import cats.effect.ConcurrentEffect
import cats.implicits._
import org.scalajs.dom

object HtmlBrowserSchelm {
  def apply[F[_]: ConcurrentEffect, Event, Node]
      : F[Schelm[F, Event, dom.Node]] =
    for {
      manager <- EventManager.unbounded[F, Event]
      dom <- BrowserDom[F, Event](manager)
    } yield new HtmlSchelm(dom, manager)
}
