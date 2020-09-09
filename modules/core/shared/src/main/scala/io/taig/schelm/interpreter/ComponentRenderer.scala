package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object ComponentRenderer {
  def apply[F[_]: Monad, Event, View, Structure](dom: Dom[F], renderer: Renderer[F, View, Structure])(
      extract: Structure => List[dom.Node]
  ): Renderer[F, Component[Event, View], NodeReference[Event, dom.Element, dom.Text, Structure]] =
    new Renderer[F, Component[Event, View], NodeReference[Event, dom.Element, dom.Text, Structure]] {
      override def render(
          component: Component[Event, View]
      ): F[NodeReference[Event, dom.Element, dom.Text, Structure]] = component match {
        case Component.Element(tag, Component.Element.Type.Void) =>
          for {
            element <- dom.createElement(tag.name)
            _ <- tag.attributes.toList.traverse_ { attribute =>
              dom.setAttribute(element, attribute.key.value, attribute.value.value)
            }
          } yield NodeReference.Element(Component.Element(tag, Component.Element.Type.Void), element)
        case Component.Element(tag, Component.Element.Type.Normal(children)) =>
          for {
            element <- dom.createElement(tag.name)
            _ <- tag.attributes.toList.traverse_ { attribute =>
              dom.setAttribute(element, attribute.key.value, attribute.value.value)
            }
            children <- children.traverse(view => renderer.render(view))
            _ <- children.indexed.flatMap(extract).traverse_(dom.appendChild(element, _))
          } yield NodeReference.Element(Component.Element(tag, Component.Element.Type.Normal(children)), element)
        case Component.Fragment(children) =>
          children
            .traverse(renderer.render)
            //          .flatTap { children =>
            //            children
            //          }
            .map(children => NodeReference.Fragment(Component.Fragment(children)))
        case node @ Component.Text(value, _) => dom.createTextNode(value).map(NodeReference.Text(node, _))
      }
    }
}
