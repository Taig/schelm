package io.taig.schelm.util

import io.taig.schelm.data.{Attributes, Children, Html, Node, StateHtml, Stateful, Stateless}
import io.taig.schelm.util.NodeFunctor.ops._
import simulacrum.typeclass

@typeclass
trait FixFunctor[F[_[_]]] {
  def mapNode[G[_]](fa: F[G])(f: Node[G, F[G]] => Node[G, F[G]]): F[G]
}

object FixFunctor {
//  implicit val html: FixFunctor[Html] = new FixFunctor[Html] {
//    override type Listeners[G[_]] = io.taig.schelm.data.Listeners[G]
//
//    override def mapNode[G[_]](
//        fa: Html[G]
//    )(f: Node[G, Listeners[G], Html[G]] => Node[G, Listeners[G], Html[G]]): Html[G] =
//      Html(f(fa.unfix))
//  }
//
//  implicit val stateHtml: FixFunctor[StateHtml] = new FixFunctor[StateHtml] {
//    override type Listeners[G[_]] = io.taig.schelm.data.Listeners[G]
//
//    override def mapNode[G[_]](
//        fa: StateHtml[G]
//    )(f: Node[G, Listeners[G], StateHtml[G]] => Node[G, Listeners[G], StateHtml[G]]): StateHtml[G] =
//      fa.unfix match {
//        case state: Stateful[G, _, Node[G, Listeners[G], StateHtml[G]]] =>
//          StateHtml(state.copy[G, Any, Node[G, Listeners[G], StateHtml[G]]](render = (a, b) => state.render(a, b)))
//        case state: Stateless[Node[G, Listeners[G], StateHtml[G]]] => ???
//      }
//  }
}
