//package io.taig.schelm.interpreter
//
//import cats.Functor
//import cats.implicits._
//import io.taig.schelm.algebra.Renderer
//import io.taig.schelm.data._
//
//final class LifecycleHtmlRenderer[F[_]: Functor, Event](renderer: Renderer[F, Html[Event], HtmlReference[Event]])
//    extends Renderer[F, LifecycleHtml[F, Event], LifecycleHtmlReference[F, Event]] {
//  override def render(lifecycle: LifecycleHtml[F, Event]): F[LifecycleHtmlReference[F, Event]] = {
//
//    ???
//  }
//}
