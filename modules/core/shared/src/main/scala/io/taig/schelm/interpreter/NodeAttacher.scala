package io.taig.schelm.interpreter

import cats.Applicative
import cats.syntax.all._
import io.taig.schelm.algebra.Dom.Element
import io.taig.schelm.algebra.{Attacher, Dom}

final class NodeAttacher[F[_]: Applicative](dom: Dom[F])(parent: Dom.Element)
    extends Attacher[F, List[Dom.Node], Dom.Element] {
  override def attach(nodes: List[Dom.Node]): F[Element] =
    nodes.traverse(dom.appendChild(parent, _)).as(parent)
}

object NodeAttacher {
  def apply[F[_]: Applicative](dom: Dom[F])(parent: Dom.Element): Attacher[F, List[Dom.Node], Dom.Element] =
    new NodeAttacher[F](dom)(parent)
}
