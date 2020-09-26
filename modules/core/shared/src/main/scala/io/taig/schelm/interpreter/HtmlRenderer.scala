package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object HtmlRenderer {
  def apply[F[_]: Monad, Event](
      dom: Dom[F]
  ): Renderer[F, Html[Event], HtmlReference[Event]] =
    new Renderer[F, Html[Event], HtmlReference[Event]] {
      val renderer: Renderer[F, Node[Event, Html[Event]], NodeReference[Event, HtmlReference[Event]]] =
        NodeRenderer(dom, this)(_.toNodes)

      override def render(html: Html[Event]): F[HtmlReference[Event]] =
        renderer.render(html.node).map(HtmlReference(_))
    }
}
