package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.Concurrent
import cats.effect.concurrent.Ref
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.data.Path

final class QueueStateManager[F[_]](state: Ref[F, Map[Path, Any]], updates: Queue[F, (Path, Any)])(implicit F: Monad[F])
    extends StateManager[F] {
  override def get(path: Path): F[Option[Any]] = snapshot.map(_.get(path))

  override val snapshot: F[Map[Path, Any]] = state.get

  override def submit[A](path: Path, value: A): F[Unit] = state.modify[Option[Any]] { state =>
    val previous = state.get(path)
    if(previous.contains(value)) (state, None) else (state + (path -> value), Some(value))
  }.flatMap {
    case Some(value) => updates.enqueue1(path -> value)
    case None =>  F.unit
  }

  override val subscription: Stream[F, (Path, Any)] = updates.dequeue
}

object QueueStateManager {
  def apply[F[_]: Monad](state: Ref[F, Map[Path, Any]], updates: Queue[F, (Path, Any)]): StateManager[F] =
    new QueueStateManager[F](state, updates)

  def empty[F[_]: Concurrent]: F[StateManager[F]] =
    for {
      state <- Ref[F].of(Map.empty[Path, Any])
      updates <- Queue.unbounded[F, (Path, Any)]
    } yield QueueStateManager[F](state, updates)
}
