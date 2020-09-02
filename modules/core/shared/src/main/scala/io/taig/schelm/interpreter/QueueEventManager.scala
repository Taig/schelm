package io.taig.schelm.interpreter

import cats.effect.{ConcurrentEffect, Effect}
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import io.taig.schelm.algebra.EventManager

final class QueueEventManager[F[_], Event](queue: Queue[F, Event]) extends EventManager[F, Event] {
  override def submit(event: Event): F[Unit] = queue.enqueue1(event)

  override val subscription: Stream[F, Event] = queue.dequeue
}

object QueueEventManager {
  def apply[F[_]: Effect, Event](queue: Queue[F, Event]): EventManager[F, Event] =
    new QueueEventManager[F, Event](queue)

  def unbounded[F[_]: ConcurrentEffect, Event]: F[EventManager[F, Event]] =
    Queue.unbounded[F, Event].map(QueueEventManager[F, Event])
}
