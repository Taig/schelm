package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object HtmlRenderer {
  def apply[F[_]: Sync](dom: Dom): Renderer[F, Html[F], HtmlReference[F, dom.Node, dom.Element, dom.Text]] =
    new Renderer[F, Html[F], HtmlReference[F, dom.Node, dom.Element, dom.Text]] {
      val renderer: Renderer[F, Component[F, Html[F]], ComponentReference[
        F,
        dom.Element,
        dom.Text,
        HtmlReference[F, dom.Node, dom.Element, dom.Text]
      ]] = ComponentRenderer(dom, this)(_.toNodes)

      override def render(html: Html[F]): F[HtmlReference[F, dom.Node, dom.Element, dom.Text]] =
        renderer.render(html.component).map(HtmlReference(_))
    }
}
