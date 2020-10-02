package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.Concurrent
import cats.effect.concurrent.Ref
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.data.{Html, Path}

final class QueueStateManager[F[_]](
    states: Ref[F, Map[Path, _]],
    updates: Queue[F, StateManager.Update[F]]
)(implicit F: Monad[F])
    extends StateManager[F] {
  override def get[A](path: Path): F[Option[A]] = states.get.map(_.get(path).asInstanceOf[Option[A]])

  override def submit[A](path: Path, initial: A, state: A, html: Html[F]): F[Unit] =
    states
      .modify[Option[StateManager.Update[F]]] { states =>
        val previous = states.getOrElse(path, initial)
        if (previous == state) (states, None)
        else (states + (path -> state), Some(StateManager.Update(path, html)))
      }
      .flatMap {
        case Some(update) => updates.enqueue1(update)
        case None         => F.unit
      }

  override val subscription: Stream[F, StateManager.Update[F]] = updates.dequeue
}

object QueueStateManager {
  def apply[F[_]: Monad](
      states: Ref[F, Map[Path, _]],
      updates: Queue[F, StateManager.Update[F]]
  ): StateManager[F] =
    new QueueStateManager[F](states, updates)

  def empty[F[_]: Concurrent]: F[StateManager[F]] =
    for {
      states <- Ref[F].of(Map.empty[Path, Any])
      updates <- Queue.unbounded[F, StateManager.Update[F]]
    } yield QueueStateManager[F](states, updates)
}
