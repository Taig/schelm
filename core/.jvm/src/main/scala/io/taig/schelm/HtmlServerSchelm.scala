//package io.taig.schelm
//
//import cats.effect.ConcurrentEffect
//import cats.implicits._
//import org.jsoup.nodes.{Node => JNode}
//
//object HtmlServerSchelm {
//  def apply[F[_]: ConcurrentEffect, Event]: F[
//    Schelm[
//      F,
//      Event,
//      JNode,
//      Html[Event],
//      Reference[Event, JNode],
//      HtmlDiff[Event]
//    ]
//  ] = ServerDom[F, Event].map(HtmlSchelm(EventManager.noop, _))
//}
