package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data._

object ComponentRenderer {
  def apply[F[_], A, B](dom: Dom, renderer: Renderer[F, A, B])(
      extract: B => List[dom.Node]
  )(implicit F: Sync[F]): Renderer[F, Component[F, A], ComponentReference[F, dom.Element, dom.Text, B]] =
    new Renderer[F, Component[F, A], ComponentReference[F, dom.Element, dom.Text, B]] {
      override def render(component: Component[F, A]): F[ComponentReference[F, dom.Element, dom.Text, B]] = ???
//        component match {
//          case Component.Element(tag, Component.Element.Type.Void, lifecycle) =>
//            F.delay {
//              val element = dom.createElement(tag.name)
//              tag.attributes.toList.foreach { attribute =>
//                dom.setAttribute(element, attribute.key.value, attribute.value.value)
//              }
//              ComponentReference.Element(Component.Element(tag, Component.Element.Type.Void, lifecycle), element)
//            }
//          case Component.Element(tag, Component.Element.Type.Normal(children), lifecycle) =>
//            F.delay {
//              val element = dom.createElement(tag.name)
//              tag.attributes.toList.foreach { attribute =>
//                dom.setAttribute(element, attribute.key.value, attribute.value.value)
//              }
//              element
//            }.flatMap { element =>
//              children.traverse(renderer.render).tupleLeft(element) <*
//                F.delay(children.indexed.flatMap(extract).foreach(dom.appendChild(element, _)))
//            }.map { case (element, children) =>
//              ComponentReference.Element(
//                Component.Element(tag, Component.Element.Type.Normal(children), lifecycle),
//                element
//              )
//            }
//
//          case Component.Fragment(children, lifecycle) =>
//            ???
////            children
////              .traverse(renderer.render)
////              .map(children => ComponentReference.Fragment(Component.Fragment(children, lifecycle)))
//          case node @ Component.Text(value, _, _) => ??? //dom.createTextNode(value).map(ComponentReference.Text(node, _))
//        }
    }
}
