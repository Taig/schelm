package io.taig.schelm.interpreter

import cats.Functor
import cats.effect.Concurrent
import cats.effect.std.Queue
import cats.syntax.all._
import fs2.Stream
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.data.{Identification, Tree}

final class QueueStateManager[F[_]: Functor](queue: Queue[F, StateManager.Update[_]]) extends StateManager[F] {
  override def submit[A](update: StateManager.Update[A]): F[Unit] = queue.offer(update)

  override def get[A](identification: Identification): F[Option[A]] = ???

  override val subscription: Stream[F, StateManager.Update[_]] = Stream.fromQueueUnterminated(queue)

  override def snapshot: F[Tree[Any]] = ???
}

object QueueStateManager {
  def unbounded[F[_]: Concurrent]: F[StateManager[F]] =
    Queue.unbounded[F, StateManager.Update[_]].map(new QueueStateManager[F](_))
}
