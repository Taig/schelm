package io.taig.schelm.interpreter

import cats.Applicative
import cats.effect.LiftIO
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}
import io.taig.schelm.data.{Component, ComponentReference, HtmlReference, Platform}

object HtmlReferenceAttacher {
  def apply[F[_]: Applicative: LiftIO](platform: Platform)(
      attacher: Attacher[F, List[platform.Node], platform.Element]
  ): Attacher[F, HtmlReference[platform.Node, platform.Element, platform.Text], platform.Element] =
    new Attacher[F, HtmlReference[platform.Node, platform.Element, platform.Text], platform.Element] {
      override def attach(html: HtmlReference[platform.Node, platform.Element, platform.Text]): F[platform.Element] =
        attacher.attach(html.toNodes) <* notify(html)

      def notify(html: HtmlReference[platform.Node, platform.Element, platform.Text]): F[Unit] =
        html.reference match {
          case ComponentReference
                .Element(Component.Element(_, Component.Element.Type.Normal(children), lifecycle), element) =>
            children.traverse_(notify) *> lifecycle.mounted.traverse_(_.apply(platform)(element).to[F])
          case ComponentReference.Element(Component.Element(_, Component.Element.Type.Void, lifecycle), element) =>
            lifecycle.mounted.traverse_(_.apply(platform)(element).to[F])
          case ComponentReference.Fragment(component) =>
            component.children.traverse_(notify) *> component.lifecycle.mounted
              .traverse_(_.apply(platform)(html.toNodes).to[F])
          case ComponentReference.Text(component, text) =>
            component.lifecycle.mounted.traverse_(_.apply(platform)(text).to[F])
        }
    }

  def default[F[_]: Applicative: LiftIO](
      dom: Dom[F]
  )(parent: dom.Element): Attacher[F, HtmlReference[dom.Node, dom.Element, dom.Text], dom.Element] =
    HtmlReferenceAttacher(dom)(NodeAttacher(dom)(parent))
}
