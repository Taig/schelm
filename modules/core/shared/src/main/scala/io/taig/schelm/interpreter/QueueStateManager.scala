package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.Concurrent
import cats.effect.concurrent.Ref
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.data.{Html, HtmlReference, NodeReference, Path}

final class QueueStateManager[F[_]](
    state: Ref[F, Map[Path, _]],
    updates: Queue[F, StateManager.Update[F, _]]
)(implicit F: Monad[F])
    extends StateManager[F] {
  override def get[A](path: Path): F[Option[A]] = state.get.map(_.get(path).asInstanceOf[Option[A]])

  override def submit[A](path: Path, initial: A, update: A): F[Unit] =
    state
      .modify[Option[(Any, Any)]] { state =>
        val previous = state.getOrElse(path, initial)
        if (previous == update) (state, None)
        else (state + (path -> update), Some((previous, update)))
      }
      .flatMap {
        case Some((previous, next)) => updates.enqueue1(StateManager.Update(path, initial, next.asInstanceOf[A]))
        case None                   => F.unit
      }

  override val subscription: Stream[F, StateManager.Update[F, _]] = updates.dequeue
}

object QueueStateManager {
  def apply[F[_]: Monad](
      state: Ref[F, Map[Path, _]],
      updates: Queue[F, StateManager.Update[F, _]]
  ): StateManager[F] =
    new QueueStateManager[F](state, updates)

  def empty[F[_]: Concurrent]: F[StateManager[F]] =
    for {
      state <- Ref[F].of(Map.empty[Path, Any])
      updates <- Queue.unbounded[F, StateManager.Update[F, _]]
    } yield QueueStateManager[F](state, updates)
}
