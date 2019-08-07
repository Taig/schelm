package io.taig.schelm

import cats.Applicative

final class ReferenceAttacher[F[_]: Applicative, Event](dom: Dom[F, Event])
    extends Attacher[F, Reference[Event]] {
  override def attach(
      container: Element,
      reference: Reference[Event]
  ): F[Unit] =
    dom.appendChildren(container, reference.root)
}

object ReferenceAttacher {
  def apply[F[_]: Applicative, Event](
      dom: Dom[F, Event]
  ): Attacher[F, Reference[Event]] =
    new ReferenceAttacher[F, Event](dom)
}
