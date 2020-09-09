package io.taig.schelm.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data.{HtmlReference, LifecycleHtmlReference}

object LifecycleHtmlReferenceAttacher {
  def apply[F[+_]: Applicative, Event, Node, Element <: Node, Text <: Node](
      attacher: Attacher[F, HtmlReference[Event, Node, Element, Text], Element]
  ): Attacher[F, LifecycleHtmlReference[F, Event, Node, Element, Text], Element] =
    new Attacher[F, LifecycleHtmlReference[F, Event, Node, Element, Text], Element] {
      override def attach(html: LifecycleHtmlReference[F, Event, Node, Element, Text]): F[Element] =
        attacher.attach(html.toHtmlReference) <* notify(html)

      def notify(html: LifecycleHtmlReference[F, Event, Node, Element, Text]): F[Unit] =
        html.lifecycle.value.traverse_(reference => notify(reference)) *> html.lifecycle.mounted
          .apply(html.toHtmlReference)
    }

  def default[F[+_]: Applicative, Event](
      dom: Dom[F]
  )(parent: dom.Element): Attacher[F, LifecycleHtmlReference[F, Event, dom.Node, dom.Element, dom.Text], dom.Element] =
    LifecycleHtmlReferenceAttacher(HtmlReferenceAttacher.default(dom)(parent))
}
