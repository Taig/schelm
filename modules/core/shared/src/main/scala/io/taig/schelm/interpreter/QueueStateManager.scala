package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.Concurrent
import cats.effect.kernel.Ref
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.data.Path

final class QueueStateManager[F[_], View](
    states: Ref[F, Map[Path, _]],
    queue: Queue[F, StateManager.Update[View]]
)(implicit F: Monad[F])
    extends StateManager[F, View] {
  override def get[A](path: Path): F[Option[A]] = snapshot.map(_.get(path).asInstanceOf[Option[A]])

  override val snapshot: F[Map[Path, _]] = states.get

  override def submit[A](path: Path, initial: A, state: A, view: View): F[Unit] =
    states
      .modify[Option[StateManager.Update[View]]] { states =>
        val previous = states.getOrElse(path, initial)
        if (previous == state) (states, None)
        else (states + (path -> state), Some(StateManager.Update(path, view)))
      }
      .flatMap {
        case Some(update) => queue.enqueue1(update)
        case None         => F.unit
      }

  override val subscription: Stream[F, StateManager.Update[View]] = queue.dequeue
}

object QueueStateManager {
  def apply[F[_]: Monad, View](
      states: Ref[F, Map[Path, _]],
      queue: Queue[F, StateManager.Update[View]]
  ): StateManager[F, View] =
    new QueueStateManager[F, View](states, queue)

  def empty[F[_]: Concurrent, View]: F[StateManager[F, View]] =
    for {
      states <- Ref[F].of(Map.empty[Path, Any])
      queue <- Queue.unbounded[F, StateManager.Update[View]]
    } yield QueueStateManager(states, queue)
}
