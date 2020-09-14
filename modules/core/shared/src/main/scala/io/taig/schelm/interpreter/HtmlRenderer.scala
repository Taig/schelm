package io.taig.schelm.interpreter

import cats.{Applicative, Monad}
import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object HtmlRenderer {
  def apply[F[_]: Monad](dom: Dom[F]): Renderer[F, Html, HtmlReference[dom.Node, dom.Element, dom.Text]] =
    new Renderer[F, Html, HtmlReference[dom.Node, dom.Element, dom.Text]] {
      val renderer: Renderer[F, Component[Html], ComponentReference[
        dom.Element,
        dom.Text,
        HtmlReference[dom.Node, dom.Element, dom.Text]
      ]] = ComponentRenderer(dom, this)(_.toNodes)

      override def render(html: Html): F[HtmlReference[dom.Node, dom.Element, dom.Text]] =
        renderer.render(html.component).map(HtmlReference(_))
    }
}
