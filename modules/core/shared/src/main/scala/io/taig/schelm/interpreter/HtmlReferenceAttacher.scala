package io.taig.schelm.interpreter

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data.{Component, ComponentReference, HtmlReference}

object HtmlReferenceAttacher {
  def apply[F[_]: Applicative](
      attacher: Attacher[F, List[Dom.Node], Dom.Element]
  ): Attacher[F, HtmlReference[F], Dom.Element] =
    new Attacher[F, HtmlReference[F], Dom.Element] {
      override def attach(html: HtmlReference[F]): F[Dom.Element] =
        attacher.attach(html.toNodes) <* notify(html)

      def notify(html: HtmlReference[F]): F[Unit] =
        html.reference match {
          case ComponentReference
                .Element(Component.Element(_, Component.Element.Type.Normal(children), lifecycle), element) =>
            children.traverse_(notify) *> lifecycle.mounted.traverse_(_.apply(element))
          case ComponentReference.Element(Component.Element(_, Component.Element.Type.Void, lifecycle), element) =>
            lifecycle.mounted.traverse_(_.apply(element))
          case ComponentReference.Fragment(component) =>
            component.children.traverse_(notify) *> component.lifecycle.mounted.traverse_(_.apply(html.toNodes))
          case ComponentReference.Text(component, text) =>
            component.lifecycle.mounted.traverse_(_.apply(text))
        }
    }

  def default[F[_]: Sync](dom: Dom)(parent: Dom.Element): Attacher[F, HtmlReference[F], Dom.Element] =
    HtmlReferenceAttacher(NodeAttacher(dom)(parent))
}
