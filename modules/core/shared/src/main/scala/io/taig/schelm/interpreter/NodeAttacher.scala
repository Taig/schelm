package io.taig.schelm.interpreter

import cats.effect.Sync
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}

object NodeAttacher {

  /** Attach a `List` of `Node`s to a parent `Element`  */
  def apply[F[_]](dom: Dom)(parent: Dom.Element)(implicit F: Sync[F]): Attacher[F, List[Dom.Node], Dom.Element] =
    new Attacher[F, List[Dom.Node], Dom.Element] {
      override def attach(nodes: List[Dom.Node]): F[Dom.Element] =
        F.delay(nodes.foreach(dom.appendChild(parent, _))).as(parent)
    }
}
