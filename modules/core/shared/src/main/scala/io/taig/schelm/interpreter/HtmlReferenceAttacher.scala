package io.taig.schelm.interpreter

import cats.Applicative
import cats.effect.LiftIO
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom, EventManager}
import io.taig.schelm.data.{HtmlReference, Node, NodeReference, Platform}

object HtmlReferenceAttacher {
  def apply[F[_]: Applicative: LiftIO, Event](platform: Platform)(
      attacher: Attacher[F, List[platform.Node], platform.Element],
      manager: EventManager[F, Event]
  ): Attacher[F, HtmlReference[Event, platform.Node, platform.Element, platform.Text], platform.Element] =
    new Attacher[F, HtmlReference[Event, platform.Node, platform.Element, platform.Text], platform.Element] {
      override def attach(
          html: HtmlReference[Event, platform.Node, platform.Element, platform.Text]
      ): F[platform.Element] =
        attacher.attach(html.toNodes) <* notify(html)

      def notify(html: HtmlReference[Event, platform.Node, platform.Element, platform.Text]): F[Unit] =
        html.reference match {
          case NodeReference
                .Element(Node.Element(_, Node.Element.Type.Normal(children), lifecycle), element) =>
            children.traverse_(notify) *> lifecycle.mounted.traverse_ { callback =>
              callback.apply(platform)(element).traverse_(manager.submit)
            }
          case NodeReference.Element(Node.Element(_, Node.Element.Type.Void, lifecycle), element) =>
            lifecycle.mounted.traverse_(callback => callback.apply(platform)(element).traverse_(manager.submit))
          case NodeReference.Fragment(node) =>
            node.children.traverse_(notify) *> node.lifecycle.mounted.traverse_ { callback =>
              callback.apply(platform)(html.toNodes).traverse_(manager.submit)
            }
          case NodeReference.Text(node, text) =>
            node.lifecycle.mounted.traverse_(callback => callback.apply(platform)(text).traverse_(manager.submit))
        }
    }

  def default[F[_]: Applicative: LiftIO, Event](
      dom: Dom[F],
      manager: EventManager[F, Event]
  )(parent: dom.Element): Attacher[F, HtmlReference[Event, dom.Node, dom.Element, dom.Text], dom.Element] =
    HtmlReferenceAttacher(dom)(NodeAttacher(dom)(parent), manager)
}
