package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.{Dom, Renderer}
import io.taig.schelm.data.Component.Element.Type
import io.taig.schelm.data._

object ComponentRenderer {
  def apply[F[_], A, B](dom: Dom[F], renderer: Renderer[F, A, B])(
      extract: B => List[dom.Node]
  )(implicit F: Sync[F]): Renderer[F, Component[A], ComponentReference[dom.Element, dom.Text, B]] =
    new Renderer[F, Component[A], ComponentReference[dom.Element, dom.Text, B]] {
      override def render(component: Component[A]): F[ComponentReference[dom.Element, dom.Text, B]] = ???
//        component match {
//          case component: Component.Element[A] =>
//            F.suspend {
//              val element = dom.createElement(component.tag.name)
//
//              component.tag.attributes.toList.foreach { attribute =>
//                dom.setAttribute(element, attribute.key.value, attribute.value.value)
//              }
//
//              val tpe: F[Component.Element.Type[B]] = component.tpe match {
//                case Component.Element.Type.Normal(children) =>
//                  children
//                    .traverse(renderer.render)
//                    .flatTap { children =>
//                      F.delay(children.indexed.flatMap(extract).foreach(dom.appendChild(element, _)))
//                    }
//                    .map(Component.Element.Type.Normal(_))
//                case Component.Element.Type.Void => Component.Element.Type.Void.pure[F].widen
//              }
//
//              tpe.map(tpe => ComponentReference.Element(component.copy(tpe = tpe), element))
//            }
////          case Component.Fragment(children, lifecycle) =>
////            ???
//////            children
//////              .traverse(renderer.render)
//////              .map(children => ComponentReference.Fragment(Component.Fragment(children, lifecycle)))
//          case component: Component.Text[F] =>
//            F.delay(ComponentReference.Text(component, dom.createTextNode(component.value)))
//        }
    }
}
