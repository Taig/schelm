package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object HtmlRenderer {
  def apply[F[_]: Monad, Event](
      dom: Dom[F]
  ): Renderer[F, Html[Event], HtmlReference[Event, dom.Node, dom.Element, dom.Text]] =
    new Renderer[F, Html[Event], HtmlReference[Event, dom.Node, dom.Element, dom.Text]] {
      val renderer: Renderer[F, Component[Event, Html[Event]], NodeReference[
        Event,
        dom.Element,
        dom.Text,
        HtmlReference[Event, dom.Node, dom.Element, dom.Text]
      ]] = ComponentRenderer(dom, this)(_.toNodes)

      override def render(html: Html[Event]): F[HtmlReference[Event, dom.Node, dom.Element, dom.Text]] =
        renderer.render(html.component).map(HtmlReference(_))
    }
}
