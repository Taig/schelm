package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object NodeRenderer {
  def apply[F[_]: Monad, Event, A, B](dom: Dom[F], renderer: Renderer[F, A, B])(
      extract: B => List[Dom.Node]
  ): Renderer[F, Node[Event, A], NodeReference[Event, B]] =
    new Renderer[F, Node[Event, A], NodeReference[Event, B]] {
      override def render(node: Node[Event, A]): F[NodeReference[Event, B]] =
        node match {
          case node @ Node.Element(tag, Node.Element.Type.Normal(children), _) =>
            for {
              element <- dom.createElement(tag.name)
              _ <- tag.attributes.toList.traverse_ { attribute =>
                dom.setAttribute(element, attribute.key.value, attribute.value.value)
              }
              children <- children.traverse(renderer.render)
              _ <- children.indexed.flatMap(extract).traverse_(dom.appendChild(element, _))
            } yield NodeReference.Element(node.copy(tpe = Node.Element.Type.Normal(children)), element)
          case node @ Node.Element(tag, Node.Element.Type.Void, _) =>
            for {
              element <- dom.createElement(tag.name)
              _ <- tag.attributes.toList.traverse_ { attribute =>
                dom.setAttribute(element, attribute.key.value, attribute.value.value)
              }
            } yield NodeReference.Element(node.copy(tpe = Node.Element.Type.Void), element)
          case node @ Node.Fragment(children, _) =>
            children
              .traverse(renderer.render)
              .map(children => NodeReference.Fragment(node.copy(children = children)))
          case node: Node.Text[Event] =>
            dom.createTextNode(node.value).map(NodeReference.Text(node, _))
        }
    }
}
