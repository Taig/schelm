package io.taig.schelm.interpreter

import cats.Monad
import cats.effect.Concurrent
import cats.effect.concurrent.Ref
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import io.taig.schelm.algebra.StateManager
import io.taig.schelm.data.{HtmlReference, Identifier, NodeReference}

final class QueueStateManager[F[_]](
    state: Ref[F, Map[Identifier, _]],
    updates: Queue[F, StateManager.Event[F, _]]
)(implicit F: Monad[F])
    extends StateManager[F] {
  override def get(identifier: Identifier): F[Option[Any]] = snapshot.map(_.get(identifier))

  override val snapshot: F[Map[Identifier, Any]] = state.get

  override def submit[A](reference: NodeReference.Stateful[F, HtmlReference[F]], update: A): F[Unit] =
    state
      .modify[Option[(Any, Any)]] { state =>
        val previous = state.getOrElse(reference.identifier, ???)
        if (previous == update) (state, None)
        else (state + (reference.identifier -> update), Some((previous, update)))
      }
      .flatMap {
        case Some((previous, next)) => updates.enqueue1(StateManager.Event(reference, previous, next))
        case None                   => F.unit
      }

  override val subscription: Stream[F, StateManager.Event[F, _]] = updates.dequeue
}

object QueueStateManager {
  def apply[F[_]: Monad](
      state: Ref[F, Map[Identifier, _]],
      updates: Queue[F, StateManager.Event[F, _]]
  ): StateManager[F] =
    new QueueStateManager[F](state, updates)

  def empty[F[_]: Concurrent]: F[StateManager[F]] =
    for {
      state <- Ref[F].of(Map.empty[Identifier, Any])
      updates <- Queue.unbounded[F, StateManager.Event[F, _]]
    } yield QueueStateManager[F](state, updates)
}
