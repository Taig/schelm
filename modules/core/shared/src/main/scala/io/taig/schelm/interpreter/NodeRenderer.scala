//package io.taig.schelm.interpreter
//
//import cats.Monad
//import cats.implicits._
//import io.taig.schelm.algebra.{Dom, Renderer}
//import io.taig.schelm.data._
//
//final class NodeRenderer[F[_]: Monad, Event, Structure, A](
//    dom: Dom[F],
//    renderer: Renderer[F, A, Structure]
//)(
//    lift: Granularity[Dom.Node] => Structure
//) extends Renderer[F, Node[Granularity[Dom.Node], Event, A], Structure] {
//  override def render(node: Node[Granularity[Dom.Node], Event, A]): F[Structure] = node match {
//    case Element(tag, Element.Type.Void) =>
//      // TODO attach listeners
//      for {
//        node <- dom.createElement(tag.name)
//        _ <- tag.attributes.toList.traverse_ {
//          case Attribute(key, value) => dom.setAttribute(node, key.value, value.value)
//        }
//      } yield lift(Granularity.Single(node))
//    case Element(tag, Element.Type.Normal(children)) =>
//      // TODO attach listeners
//      for {
//        node <- dom.createElement(tag.name)
//        _ <- tag.attributes.toList.traverse_ {
//          case Attribute(key, value) => dom.setAttribute(node, key.value, value.value)
//        }
//        _ <- children
//          .traverse(renderer.render)
//          .map(_.indexed.flatMap(_.toList))
//          .map(_.traverse_(dom.appendChild(node, _)))
//      } yield lift(Granularity.Single(node))
//    case Fragment(children) =>
//      children
//        .traverse(renderer.render)
//        .map(children => lift(Granularity.Collection(children.indexed.flatMap(_.toList))))
//    case Text(value, _) =>
//      // TODO attach listeners
//      dom.createTextNode(value).map(node => lift(Granularity.Single(node)))
//  }
//}
