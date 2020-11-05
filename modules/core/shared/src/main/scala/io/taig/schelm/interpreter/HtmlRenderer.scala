package io.taig.schelm.interpreter

import cats.Monad
import cats.data.Kleisli
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data._
import org.scalajs.dom.raw.Event

object HtmlRenderer {
  def apply[F[_]: Monad](dom: Dom[F]): Renderer[F, Html[F], HtmlReference[F]] = {
    def element(node: Node.Element[F, Listeners[F], Html[F]]): F[HtmlReference[F]] =
      for {
        element <- dom.createElement(node.tag.name.value)
        _ <- node.tag.attributes.toList.traverse_ { attribute =>
          dom.setAttribute(element, attribute.key.value, attribute.value.value)
        }
        variant <- variant(element)(node.variant)
        reference = NodeReference.Element(node.copy(variant = variant), element)
      } yield HtmlReference(reference)

    def fragment(node: Node.Fragment[Html[F]]): F[HtmlReference[F]] =
      node.children
        .traverse(render)
        .map(children => HtmlReference(NodeReference.Fragment(node.copy(children = children))))

    def text(node: Node.Text[F, Listeners[F]]): F[HtmlReference[F]] =
      dom.createTextNode(node.value).map(text => HtmlReference(NodeReference.Text(node, text)))

    def variant(element: Dom.Element): Node.Element.Variant[Html[F]] => F[Variant[HtmlReference[F]]] = {
      case Variant.Normal(children) =>
        for {
          children <- children.traverse(render)
          _ <- children.toList.flatMap(_.dom).traverse_(dom.appendChild(element, _))
        } yield Node.Element.Variant.Normal(children)
      case Variant.Void => Variant.Void.pure[F].widen
    }

    def render(html: Html[F]): F[HtmlReference[F]] = html.unfix match {
      case node: Node.Element[F, Listeners[F], Html[F]] => element(node)
      case node: Node.Fragment[Html[F]]                 => fragment(node)
      case node: Node.Text[F, Listeners[F]]             => text(node)
    }

    Kleisli(render)
  }
}
