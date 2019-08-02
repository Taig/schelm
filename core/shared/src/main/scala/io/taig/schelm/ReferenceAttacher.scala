package io.taig.schelm

import cats._
import cats.implicits._

final class ReferenceAttacher[F[_]: Monad, Event, Node](
    dom: Dom[F, Event, Node]
) extends Attacher[F, Node, Reference[Event, Node]] {
  override def attach(parent: Node, child: Reference[Event, Node]): F[Unit] =
    dom.element(parent).flatMap(dom.appendChildren(_, child.root))
}

object ReferenceAttacher {
  def apply[F[_]: Monad, Event, Node](
      dom: Dom[F, Event, Node]
  ): Attacher[F, Node, Reference[Event, Node]] =
    new ReferenceAttacher[F, Event, Node](dom)
}
