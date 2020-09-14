package io.taig.schelm.interpreter

import cats.Monad
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object ComponentRenderer {
  def apply[F[_]: Monad, A, B](dom: Dom[F], renderer: Renderer[F, A, B])(
      extract: B => List[dom.Node]
  ): Renderer[F, Component[A], ComponentReference[dom.Element, dom.Text, B]] =
    new Renderer[F, Component[A], ComponentReference[dom.Element, dom.Text, B]] {
      override def render(component: Component[A]): F[ComponentReference[dom.Element, dom.Text, B]] =
        component match {
          case component @ Component.Element(tag, Component.Element.Type.Normal(children), _) =>
            for {
              element <- dom.createElement(tag.name)
              _ <- tag.attributes.toList.traverse_ { attribute =>
                dom.setAttribute(element, attribute.key.value, attribute.value.value)
              }
              children <- children.traverse(renderer.render)
              _ <- children.indexed.flatMap(extract).traverse_(dom.appendChild(element, _))
            } yield ComponentReference.Element(component.copy(tpe = Component.Element.Type.Normal(children)), element)
          case component @ Component.Element(tag, Component.Element.Type.Void, _) =>
            for {
              element <- dom.createElement(tag.name)
              _ <- tag.attributes.toList.traverse_ { attribute =>
                dom.setAttribute(element, attribute.key.value, attribute.value.value)
              }
            } yield ComponentReference.Element(component.copy(tpe = Component.Element.Type.Void), element)
          case component @ Component.Fragment(children, _) =>
            children
              .traverse(renderer.render)
              .map(children => ComponentReference.Fragment(component.copy(children = children)))
          case component: Component.Text =>
            dom.createTextNode(component.value).map(ComponentReference.Text(component, _))
        }
    }
}
