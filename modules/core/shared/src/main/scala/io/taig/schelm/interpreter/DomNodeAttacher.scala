//package io.taig.schelm.interpreter
//
//import cats.{Applicative, Functor}
//import cats.implicits._
//import io.taig.schelm.algebra
//import io.taig.schelm.algebra.{Attacher, Dom}
//import io.taig.schelm.data.Granularity
//
//final class DomNodeAttacher[F[_]: Applicative](dom: Dom[F])(parent: Dom.Element)
//    extends Attacher[F, Granularity[Dom.Node], Dom.Element] {
//  override def attach(nodes: Granularity[algebra.Dom.Node]): F[Dom.Element] =
//    nodes match {
//      case Granularity.Single(node)      => dom.appendChild(parent, node).as(parent)
//      case Granularity.Collection(nodes) => nodes.traverse_(node => dom.appendChild(parent, node)).as(parent)
//    }
//}
//
//object DomNodeAttacher {
//  def apply[F[_]: Applicative](dom: Dom[F])(parent: Dom.Element): Attacher[F, Granularity[Dom.Node], Dom.Element] =
//    new DomNodeAttacher[F](dom)(parent)
//}
