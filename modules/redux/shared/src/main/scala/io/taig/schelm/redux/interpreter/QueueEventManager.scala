package io.taig.schelm.redux.interpreter

import cats.effect.Concurrent
import cats.implicits._
import fs2.concurrent.Queue
import io.taig.schelm.redux.algebra.EventManager
import fs2.Stream

final class QueueEventManager[F[_], Event](queue: Queue[F, Event]) extends EventManager[F, Event] {
  override def submit(event: Event): F[Unit] = queue.enqueue1(event)

  override val subscription: Stream[F, Event] = queue.dequeue
}

object QueueEventManager {
  def apply[F[_], Event](queue: Queue[F, Event]): EventManager[F, Event] =
    new QueueEventManager(queue)

  def unbounded[F[_]: Concurrent, Event]: F[EventManager[F, Event]] =
    Queue.unbounded[F, Event].map(QueueEventManager[F, Event])
}
