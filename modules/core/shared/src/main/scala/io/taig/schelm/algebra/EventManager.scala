package io.taig.schelm.algebra

import cats.effect.Async
import fs2.Stream

abstract class EventManager[F[_], Event] {
  def submit(event: Event): F[Unit]

  def subscription: Stream[F, Event]
}

object EventManager {
  def noop[F[_], Event](implicit F: Async[F]): EventManager[F, Event] =
    new EventManager[F, Event] {
      override def submit(event: Event): F[Unit] = F.unit

      override val subscription: Stream[F, Event] = Stream.never
    }
}
