package io.taig.schelm.interpreter

import cats.Monad
import cats.syntax.all._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data.{Html, HtmlReference, Node, NodeReference}

final class HtmlRenderer[F[_]: Monad](dom: Dom[F]) extends Renderer[F, Html[F], HtmlReference[F]] {
  override def render(html: Html[F]): F[HtmlReference[F]] = html.unfix match {
    case node: Node.Element[F, Html[F]]  => element(node)
    case node: Node.Fragment[F, Html[F]] => fragment(node)
    case node: Node.Text[F]              => text(node)
    case node: Node.Portal[F, Html[F]]   => portal(node)
    case node: Node.Environment[F, Html[F]] => ???
  }

  val nodes: HtmlReference[F] => List[Dom.Node] = _.unfix match {
    case reference: NodeReference.Element[F, HtmlReference[F]]  => List(reference.dom)
    case reference: NodeReference.Fragment[F, HtmlReference[F]] => reference.node.children.toList.flatMap(nodes)
    case reference: NodeReference.Text[F]                       => List(reference.dom)
  }

  val Void: F[Node.Element.Variant[HtmlReference[F]]] = Node.Element.Variant.Void.pure[F].widen

  def element(node: Node.Element[F, Html[F]]): F[HtmlReference[F]] =
    for {
      element <- dom.createElement(node.tag.name.value)
      _ <- node.tag.attributes.toList.traverse_ { attribute =>
        dom.setAttribute(element, attribute.key.value, attribute.value.value)
      }
      _ = node.tag.listeners.toList.traverse_ { listener =>
        dom.addEventListener(element, listener.name.value, dom.unsafeRun(listener.action))
      }
      variant <- variant(element)(node.variant)
      reference = NodeReference.Element(node.copy(variant = variant), element)
    } yield HtmlReference(reference)

  def fragment(node: Node.Fragment[F, Html[F]]): F[HtmlReference[F]] =
    node.children
      .traverse(render)
      .map(children => HtmlReference(NodeReference.Fragment(node.copy(children = children))))

  def text(node: Node.Text[F]): F[HtmlReference[F]] =
    dom.createTextNode(node.value).map(text => HtmlReference(NodeReference.Text(node, text)))

  def portal(node: Node.Portal[F, Html[F]]): F[HtmlReference[F]] =
    for {
      target <- node.target
      portal <- node.traverse(render)
      _ <- nodes(portal.value).traverse_(dom.appendChild(target, _))
    } yield HtmlReference(NodeReference.Portal(portal))

  def variant(element: Dom.Element): Node.Element.Variant[Html[F]] => F[Variant[HtmlReference[F]]] = {
    case Variant.Normal(children) =>
      for {
        children <- children.traverse(render)
        _ <- children.toList.flatMap(nodes).traverse_(dom.appendChild(element, _))
      } yield Node.Element.Variant.Normal(children)
    case Variant.Void => Void
  }
}

object HtmlRenderer {
  def apply[F[_]: Monad](dom: Dom[F]): Renderer[F, Html[F], HtmlReference[F]] =
    new HtmlRenderer[F](dom)
}
