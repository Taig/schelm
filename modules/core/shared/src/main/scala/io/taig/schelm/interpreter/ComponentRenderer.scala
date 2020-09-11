package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object ComponentRenderer {
  def apply[F[+_]: Monad, A, B](dom: Dom[F], renderer: Renderer[F, A, B])(
      extract: B => List[dom.Node]
  ): Renderer[F, Component[F, A], ComponentReference[F, dom.Element, dom.Text, B]] =
    new Renderer[F, Component[F, A], ComponentReference[F, dom.Element, dom.Text, B]] {
      override def render(component: Component[F, A]): F[ComponentReference[F, dom.Element, dom.Text, B]] =
        component match {
          case Component.Element(tag, Component.Element.Type.Void, lifecycle) =>
            for {
              element <- dom.createElement(tag.name)
              _ <- tag.attributes.toList.traverse_ { attribute =>
                dom.setAttribute(element, attribute.key.value, attribute.value.value)
              }
            } yield ComponentReference.Element(Component.Element(tag, Component.Element.Type.Void, lifecycle), element)
          case Component.Element(tag, Component.Element.Type.Normal(children), lifecycle) =>
            for {
              element <- dom.createElement(tag.name)
              _ <- tag.attributes.toList.traverse_ { attribute =>
                dom.setAttribute(element, attribute.key.value, attribute.value.value)
              }
              children <- children.traverse(renderer.render)
              _ <- children.indexed.flatMap(extract).traverse_(dom.appendChild(element, _))
            } yield ComponentReference.Element(
              Component.Element(tag, Component.Element.Type.Normal(children), lifecycle),
              element
            )
          case Component.Fragment(children, lifecycle) =>
            children
              .traverse(renderer.render)
              .map(children => ComponentReference.Fragment(Component.Fragment(children, lifecycle)))
          case node @ Component.Text(value, _, _) => dom.createTextNode(value).map(ComponentReference.Text(node, _))
        }
    }
}
