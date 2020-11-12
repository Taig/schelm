package io.taig.schelm.interpreter

import cats.effect.Concurrent
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import io.taig.schelm.algebra.StateManager

final class QueueStateManager[F[_]](queue: Queue[F, StateManager.Update[_]]) extends StateManager[F] {
  override def submit[A](update: StateManager.Update[A]): F[Unit] = queue.enqueue1(update)

  override val subscription: Stream[F, StateManager.Update[_]] = queue.dequeue
}

object QueueStateManager {
  def apply[F[_]](queue: Queue[F, StateManager.Update[_]]): StateManager[F] = new QueueStateManager[F](queue)

  def empty[F[_]: Concurrent]: F[StateManager[F]] = Queue.unbounded[F, StateManager.Update[_]].map(QueueStateManager[F])
}
