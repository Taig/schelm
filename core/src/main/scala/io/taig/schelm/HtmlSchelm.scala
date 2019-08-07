//package io.taig.schelm
//
//import cats.effect.ConcurrentEffect
//
//object HtmlSchelm {
//  def apply[F[_]: ConcurrentEffect, Event, Node](
//      manager: EventManager[F, Event],
//      dom: Dom[F, Event, Node]
//  ): Schelm[F, Event, Node, Html[Event], Reference[Event, Node], HtmlDiff[
//    Event
//  ]] =
//    Schelm(
//      dom,
//      manager,
//      HtmlRenderer(dom),
//      ReferenceAttacher(dom),
//      HtmlDiffer[Event],
//      ReferencePatcher(HtmlRenderer(dom), dom)
//    )
//}
