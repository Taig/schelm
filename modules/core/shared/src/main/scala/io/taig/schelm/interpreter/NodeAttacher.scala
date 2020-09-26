package io.taig.schelm.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}

object NodeAttacher {

  /** Attach a `List` of `Node`s to a parent `Element`  */
  def apply[F[_]: Applicative](dom: Dom[F])(parent: Dom.Element): Attacher[F, List[Dom.Node], Dom.Element] =
    (nodes: List[Dom.Node]) => nodes.traverse_(dom.appendChild(parent, _)).as(parent)
}
