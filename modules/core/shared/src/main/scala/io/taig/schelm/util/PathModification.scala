package io.taig.schelm.util

import cats.Applicative
import cats.implicits._
import io.taig.schelm.data.Path./
import io.taig.schelm.data._
import io.taig.schelm.util.NodeTraverse.ops._
import simulacrum.typeclass

@typeclass
trait PathModification[F[_[_]]] {
  final def modify[G[_]: Applicative](value: F[G])(path: Path)(f: F[G] => G[F[G]]): G[F[G]] = path match {
    case Path.Root => f(value)
    case head / tail =>
      modifyChildren(value) { children =>
        children.get(head) match {
          case Some(child) => modify(child)(tail)(f).map(children.updated(head, _))
          case None        => children.pure[G]
        }
      }
  }

  def modifyChildren[G[_]: Applicative](value: F[G])(f: Children[F[G]] => G[Children[F[G]]]): G[F[G]]
}

object PathModification {
//  implicit val html: PathModification[Html] = new PathModification[Html] {
//    override def modifyChildren[G[_]: Applicative](
//        html: Html[G]
//    )(f: Children[Html[G]] => G[Children[Html[G]]]): G[Html[G]] =
//      html.unfix.modifyChildren(f).map(Html.apply)
//  }
//
//  implicit val stateHtml: PathModification[StateHtml] = new PathModification[StateHtml] {
//    override def modifyChildren[G[_]: Applicative](
//        html: StateHtml[G]
//    )(f: Children[StateHtml[G]] => G[Children[StateHtml[G]]]): G[StateHtml[G]] =
//      html.unfix match {
//        case state: Stateful[G, _, Node[G, Listeners[G], StateHtml[G]]] => ???
//        case state: Stateless[Node[G, Listeners[G], StateHtml[G]]]      => ???
//      }
//  }
}
