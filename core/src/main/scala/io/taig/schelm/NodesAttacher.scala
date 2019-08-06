package io.taig.schelm

import cats.Monad
import cats.implicits._

final class NodesAttacher[F[_]: Monad, Event, Node](dom: Dom[F, Event, Node])
    extends Attacher[F, Node, List[Node]] {
  override def attach(node: Node, nodes: List[Node]): F[Unit] =
    dom.element(node).flatMap(dom.appendChildren(_, nodes))
}

object NodesAttacher {
  def apply[F[_]: Monad, Event, Node](
      dom: Dom[F, Event, Node]
  ): Attacher[F, Node, List[Node]] =
    new NodesAttacher[F, Event, Node](dom)
}
