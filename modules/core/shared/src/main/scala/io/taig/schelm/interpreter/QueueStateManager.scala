package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.Concurrent
import cats.effect.concurrent.Ref
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.data.{Html, HtmlReference, Node, Path}

final class QueueStateManager[F[_]](
    state: Ref[F, Map[HtmlReference[F], _]],
    updates: Queue[F, StateManager.Event[F, _]]
)(implicit F: Monad[F])
    extends StateManager[F] {
  override def get(reference: HtmlReference[F]): F[Option[Any]] = snapshot.map(_.get(reference))

  override val snapshot: F[Map[HtmlReference[F], Any]] = state.get

  override def submit[A](event: StateManager.Event[F, A]): F[Unit] =
    state
      .modify[Option[Any]] { state =>
        val previous = state.get(event.reference)
        if (previous.contains(event.state)) (state, None)
        else (state + (event.reference -> event.state), Some(event.state))
      }
      .flatMap {
        case Some(value) => updates.enqueue1(event)
        case None        => F.unit
      }

  override val subscription: Stream[F, StateManager.Event[F, _]] = updates.dequeue
}

object QueueStateManager {
  def apply[F[_]: Monad](
      state: Ref[F, Map[HtmlReference[F], _]],
      updates: Queue[F, StateManager.Event[F, _]]
  ): StateManager[F] =
    new QueueStateManager[F](state, updates)

  def empty[F[_]: Concurrent]: F[StateManager[F]] =
    for {
      state <- Ref[F].of(Map.empty[HtmlReference[F], Any])
      updates <- Queue.unbounded[F, StateManager.Event[F, _]]
    } yield QueueStateManager[F](state, updates)
}
