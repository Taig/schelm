//package io.taig.schelm.css
//
//import cats.effect.ConcurrentEffect
//import cats.implicits._
//import io.taig.schelm.{EventManager, Schelm, ServerDom}
//import org.jsoup.nodes.{Node => JNode}
//
//object WidgetServerSchelm {
//  def apply[F[_]: ConcurrentEffect, Event]: F[
//    Schelm[
//      F,
//      Event,
//      JNode,
//      StyledHtml[Event],
//      StyledReference[Event, JNode],
//      StyledDiff[Event]
//    ]
//  ] = ServerDom[F, Event].map(WidgetSchelm(EventManager.noop, _))
//}
