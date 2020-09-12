package io.taig.schelm.interpreter

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data.{Component, ComponentReference, HtmlReference}

object HtmlReferenceAttacher {
  def apply[F[_]: Applicative](dom: Dom)(
      attacher: Attacher[F, List[dom.Node], dom.Element]
  ): Attacher[F, HtmlReference[F, dom.Node, dom.Element, dom.Text], dom.Element] =
    new Attacher[F, HtmlReference[F, dom.Node, dom.Element, dom.Text], dom.Element] {
      override def attach(html: HtmlReference[F, dom.Node, dom.Element, dom.Text]): F[dom.Element] =
        attacher.attach(html.toNodes) <* notify(html)

      def notify(html: HtmlReference[F, dom.Node, dom.Element, dom.Text]): F[Unit] =
        html.reference match {
          case ComponentReference
                .Element(Component.Element(_, Component.Element.Type.Normal(children), lifecycle), element) =>
            children.traverse_(notify) *> lifecycle.mounted.traverse_(_.apply(dom)(element))
          case ComponentReference.Element(Component.Element(_, Component.Element.Type.Void, lifecycle), element) =>
            lifecycle.mounted.traverse_(_.apply(dom)(element))
          case ComponentReference.Fragment(component) =>
            component.children.traverse_(notify) *> component.lifecycle.mounted.traverse_(_.apply(dom)(html.toNodes))
          case ComponentReference.Text(component, text) =>
            component.lifecycle.mounted.traverse_(_.apply(dom)(text))
        }
    }

  def default[F[_]: Sync, Event](dom: Dom)(parent: dom.Element): Attacher[F, HtmlReference[F, dom.Node, dom.Element, dom.Text], dom.Element] =
    HtmlReferenceAttacher(dom)(NodeAttacher(dom)(parent))
}
