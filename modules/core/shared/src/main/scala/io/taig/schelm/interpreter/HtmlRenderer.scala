package io.taig.schelm.interpreter

import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer, StateManager}
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data._

final class HtmlRenderer[F[_]: Sync](dom: Dom[F], manager: StateManager[F])
    extends Renderer[F, Html[F], HtmlReference[F]] {
  override def render(html: Html[F], path: Path): F[HtmlReference[F]] = html.node match {
    case node @ Node.Element(_, _, _) => element(node, path)
    case node @ Node.Fragment(_)      => fragment(node, path)
    case node @ Node.Stateful(_, _)   => stateful(node, path)
    case node @ Node.Text(_, _, _)    => text(node)
  }

  def element(node: Node.Element[F, Listeners[F], Html[F]], path: Path): F[HtmlReference[F]] =
    for {
      element <- dom.createElement(node.tag.name)
      _ <- node.tag.attributes.toList.traverse_ { attribute =>
        dom.setAttribute(element, attribute.key.value, attribute.value.value)
      }
      listeners <- listeners(element)(node.tag.listeners)
      variant <- variant(element, path)(node.variant)
      tag = node.tag.copy(listeners = listeners)
      reference = NodeReference.Element(node.copy(tag = tag, variant = variant), element)
    } yield HtmlReference(reference)

  def fragment(node: Node.Fragment[F, Html[F]], path: Path): F[HtmlReference[F]] =
    node.children
      .traverseWithKey((key, html) => render(html, path / key))
      .map(children => HtmlReference(NodeReference.Fragment(node.copy(children = children))))

  def stateful[A](node: Node.Stateful[F, A, Html[F]], path: Path): F[HtmlReference[F]] =
    for {
      state <- manager.get[A](path).map(_.getOrElse(node.initial))
      update = (value: A) => manager.submit(path, node.initial, value)
      html = node.render(update, state)
      value <- render(html, path)
      reference = NodeReference.Stateful(node, value)
    } yield HtmlReference(reference)

  def text[A](node: Node.Text[F, Listeners[F]]): F[HtmlReference[F]] =
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

  def variant(element: Dom.Element, path: Path): Node.Element.Variant[Html[F]] => F[Variant[HtmlReference[F]]] = {
    case Variant.Normal(children) =>
      for {
        children <- children.traverseWithKey((key, html) => render(html, path / key))
        _ <- children.toList.flatMap(_.dom).traverse_(dom.appendChild(element, _))
      } yield Node.Element.Variant.Normal(children)
    case Variant.Void => Variant.Void.pure[F].widen
  }
}

object HtmlRenderer {
  def apply[F[_]: Sync](dom: Dom[F], manager: StateManager[F]): Renderer[F, Html[F], HtmlReference[F]] =
    new HtmlRenderer[F](dom, manager)
}
