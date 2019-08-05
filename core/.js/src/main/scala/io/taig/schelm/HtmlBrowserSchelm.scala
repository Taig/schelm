//package io.taig.schelm
//
//import cats.effect.ConcurrentEffect
//import cats.implicits._
//import org.scalajs.dom
//
//object HtmlBrowserSchelm {
//  def apply[F[_]: ConcurrentEffect, Event]: F[
//    Schelm[
//      F,
//      Event,
//      dom.Node,
//      Html[Event],
//      Reference[Event, dom.Node],
//      HtmlDiff[Event]
//    ]
//  ] =
//    for {
//      manager <- EventManager.unbounded[F, Event]
//      dom <- BrowserDom(manager)
//    } yield HtmlSchelm(manager, dom)
//}
