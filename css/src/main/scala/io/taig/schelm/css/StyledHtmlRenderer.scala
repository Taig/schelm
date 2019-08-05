//package io.taig.schelm.css
//
//import cats._
//import cats.implicits._
//import io.taig.schelm._
//
//final class StyledHtmlRenderer[F[_]: Functor, Event, Node](
//    renderer: Renderer[F, Html[Event], Reference[Event, Node]]
//) extends Renderer[F, StyledHtml[Event], StyledReference[Event, Node]] {
//  override def render(
//      value: StyledHtml[Event]
//  ): F[StyledReference[Event, Node]] =
//    renderer.render(value.html).map(StyledReference(_, value.stylesheet))
//}
//
//object StyledHtmlRenderer {
//  def apply[F[_]: Monad, Event, Node](
//      renderer: Renderer[F, Html[Event], Reference[Event, Node]]
//  ): Renderer[F, StyledHtml[Event], StyledReference[Event, Node]] =
//    new StyledHtmlRenderer(renderer)
//
//  def apply[F[_]: Monad, Event, Node](
//      dom: Dom[F, Event, Node]
//  ): Renderer[F, StyledHtml[Event], StyledReference[Event, Node]] =
//    StyledHtmlRenderer[F, Event, Node](HtmlRenderer(dom))
//}
