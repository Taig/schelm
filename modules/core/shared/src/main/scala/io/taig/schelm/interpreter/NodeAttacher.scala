//package io.taig.schelm.interpreter
//
//import cats.Applicative
//import cats.implicits._
//import io.taig.schelm.algebra.{Attacher, Dom}
//
//object NodeAttacher {
//
//  /** Attach a `List` of `Node`s to a parent `Element`  */
//  def apply[F[+_]: Applicative](dom: Dom[F])(parent: dom.Element): Attacher[F, List[dom.Node], dom.Element] =
//    new Attacher[F, List[dom.Node], dom.Element] {
//      override def attach(nodes: List[dom.Node]): F[dom.Element] =
//        nodes.traverse_(dom.appendChild(parent, _)).as(parent)
//    }
//}
