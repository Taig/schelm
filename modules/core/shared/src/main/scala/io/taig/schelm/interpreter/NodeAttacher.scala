package io.taig.schelm.interpreter

import cats.Applicative
import cats.implicits._
import io.taig.schelm.algebra.{Attacher, Dom}

object NodeAttacher {

  /** Attach a `List` of `Node`s to a parent `Element`  */
  def apply[F[_]: Applicative](dom: Dom[F])(root: Dom.Element): Attacher[F, Vector[Dom.Node], Dom.Element] =
    (nodes: Vector[Dom.Node]) => nodes.traverse_(dom.appendChild(root, _)).as(root)
}
