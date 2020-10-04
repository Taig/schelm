package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data._

final class HtmlRenderer[F[_]: Monad](dom: Dom[F]) extends Renderer[F, Html[F], HtmlReference[F]] {
  override def render(html: Html[F]): F[HtmlReference[F]] = html.node match {
    case node @ Node.Element(_, _, _) => element(node)
    case node @ Node.Fragment(_)      => fragment(node)
    case node @ Node.Text(_, _, _)    => text(node)
  }

  def element(node: Node.Element[F, Listeners[F], Html[F]]): F[HtmlReference[F]] =
    for {
      element <- dom.createElement(node.tag.name)
      _ <- node.tag.attributes.toList.traverse_ { attribute =>
        dom.setAttribute(element, attribute.key.value, attribute.value.value)
      }
      listeners <- listeners(element)(node.tag.listeners)
      variant <- variant(element)(node.variant)
      tag = node.tag.copy(listeners = listeners)
      reference = NodeReference.Element(node.copy(tag = tag, variant = variant), element)
    } yield HtmlReference(reference)

  def fragment(node: Node.Fragment[Html[F]]): F[HtmlReference[F]] =
    node.children
      .traverse(render)
      .map(children => HtmlReference(NodeReference.Fragment(node.copy(children = children))))

  def text(node: Node.Text[F, Listeners[F]]): F[HtmlReference[F]] =
    for {
      text <- dom.createTextNode(node.value)
      listeners <- listeners(text)(node.listeners)
    } yield HtmlReference(NodeReference.Text(node.copy(listeners = listeners), text))

  def listeners(node: Dom.Node): Listeners[F] => F[ListenerReferences[F]] =
    _.toList
      .traverse { listener =>
        val reference = dom.unsafeRun(listener.action)

        dom
          .addEventListener(node, listener.name.value, reference)
          .as(listener.name -> ((reference, listener.action)))
      }
      .map(listeners => ListenerReferences(listeners.toMap))

  def variant(element: Dom.Element): Node.Element.Variant[Html[F]] => F[Variant[HtmlReference[F]]] = {
    case Variant.Normal(children) =>
      for {
        children <- children.traverse(render)
        _ <- children.toList.flatMap(_.dom).traverse_(dom.appendChild(element, _))
      } yield Node.Element.Variant.Normal(children)
    case Variant.Void => Variant.Void.pure[F].widen
  }
}

object HtmlRenderer {
  def apply[F[_]: Monad](dom: Dom[F]): Renderer[F, Html[F], HtmlReference[F]] = new HtmlRenderer[F](dom)
}
