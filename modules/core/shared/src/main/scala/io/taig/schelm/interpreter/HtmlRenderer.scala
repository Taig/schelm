package io.taig.schelm.interpreter

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object HtmlRenderer {
  def apply[F[_]: Sync](dom: Dom): Renderer[F, Html[F], HtmlReference[F]] =
    new Renderer[F, Html[F], HtmlReference[F]] {
      val renderer: Renderer[F, Component[F, Html[F]], ComponentReference[F, HtmlReference[F]]] =
        ComponentRenderer(dom, this)(_.toNodes)

      override def render(html: Html[F]): F[HtmlReference[F]] = renderer.render(html.component).map(HtmlReference(_))
    }
}
