package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object LifecycleHtmlRenderer {
  def apply[F[+_]: Monad, Event](dom: Dom[F]): Renderer[
    F,
    LifecycleHtml[F, Event, dom.Node, dom.Element, dom.Text],
    LifecycleHtmlReference[F, Event, dom.Node, dom.Element, dom.Text]
  ] =
    new Renderer[
      F,
      LifecycleHtml[F, Event, dom.Node, dom.Element, dom.Text],
      LifecycleHtmlReference[F, Event, dom.Node, dom.Element, dom.Text]
    ] {
      val renderer
          : Renderer[F, Component[Event, LifecycleHtml[F, Event, dom.Node, dom.Element, dom.Text]], NodeReference[
            Event,
            dom.Element,
            dom.Text,
            LifecycleHtmlReference[F, Event, dom.Node, dom.Element, dom.Text]
          ]] = ComponentRenderer(dom, this)(_.toHtmlReference.toNodes)

      override def render(
          html: LifecycleHtml[F, Event, dom.Node, dom.Element, dom.Text]
      ): F[LifecycleHtmlReference[F, Event, dom.Node, dom.Element, dom.Text]] =
        renderer.render(html.lifecycle.value).map { reference =>
          LifecycleHtmlReference(html.lifecycle.copy(value = reference))
        }
    }
}
