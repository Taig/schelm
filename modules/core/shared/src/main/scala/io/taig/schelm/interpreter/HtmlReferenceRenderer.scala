//package io.taig.schelm.interpreter
//
//import cats.Functor
//import cats.implicits._
//import io.taig.schelm.algebra.{Dom, Renderer}
//import io.taig.schelm.data.{Granularity, Html, HtmlReference, Node, Reference}
//
//final class HtmlReferenceRenderer[F[_]: Functor, Event](renderer: Renderer[F, Html[Event], HtmlReference[Event]])
//    extends Renderer[F, Html[Event], HtmlReference[Event]] {
//  override def render(html: Html[Event]): F[HtmlReference[Event]] = {
//
//
//    renderer.tapWith { (aa: Node[Granularity[Dom.Node], Event, HtmlReference[Event]], granularity) =>
//      HtmlReference(Reference(granularity, aa)) : HtmlReference[Event]
//    }.contramap[HtmlReference[Event]](???).render(???)
//  }
//}
