package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Patcher, Renderer, StateManager}
import io.taig.schelm.data._

final class HtmlRenderer[F[_]: Monad](
    dom: Dom[F],
    patcher: Patcher[F, List[Dom.Node], HtmlDiff[F]],
    manager: StateManager[F]
) extends Renderer[F, Html[F], HtmlReference[F]] { self =>
  override def render(html: Html[F], path: Path): F[HtmlReference[F]] = render(html.node, path)

  def render(node: Node[F, Html[F]], path: Path): F[HtmlReference[F]] =
    node match {
      case node @ Node.Element(tag, Node.Element.Variant.Normal(children), _) =>
        for {
          element <- dom.createElement(tag.name)
          _ <- tag.attributes.toList.traverse_ { attribute =>
            dom.setAttribute(element, attribute.key.value, attribute.value.value)
          }
          _ <- tag.listeners.toList.traverse_ { listener =>
            dom.addEventListener(element, listener.name.value, dom.unsafeRun(listener.action))
          }
          children <- children.traverseWithKey((key, child) => render(child, path / key))
          _ <- children.toList.flatMap(_.toNodes).traverse_(dom.appendChild(element, _))
          variant = Node.Element.Variant.Normal(children)
          reference = NodeReference.Element(node.copy(variant = variant), element)
        } yield HtmlReference(reference)
      case node @ Node.Element(tag, Node.Element.Variant.Void, _) =>
        for {
          element <- dom.createElement(tag.name)
          _ <- tag.attributes.toList.traverse_ { attribute =>
            dom.setAttribute(element, attribute.key.value, attribute.value.value)
          }
          _ <- tag.listeners.toList.traverse_ { listener =>
            dom.addEventListener(element, listener.name.value, dom.unsafeRun(listener.action))
          }
          reference = NodeReference.Element(node.copy(variant = Node.Element.Variant.Void), element)
        } yield HtmlReference(reference)
      case node @ Node.Fragment(children) =>
        children
          .traverseWithKey((key, child) => render(child, path / key))
          .map(children => HtmlReference(NodeReference.Fragment(node.copy(children = children))))
      case Node.Stateful(initial, render) =>
        for {
          state <- manager.get(path).map(_.getOrElse(initial))
          node = render(manager.submit(path, _), state)
          reference <- self.render(node, path)
        } yield reference
      case node: Node.Text[F] =>
        for {
          text <- dom.createTextNode(node.value)
          _ <- node.listeners.toList.traverse_ { listener =>
            dom.addEventListener(text, listener.name.value, dom.unsafeRun(listener.action))
          }
        } yield HtmlReference(NodeReference.Text(node, text))
    }
}

object HtmlRenderer {
  def apply[F[_]: Monad](
      dom: Dom[F],
      patcher: Patcher[F, List[Dom.Node], HtmlDiff[F]],
      manager: StateManager[F]
  ): Renderer[F, Html[F], HtmlReference[F]] =
    new HtmlRenderer[F](dom, patcher, manager)
}
