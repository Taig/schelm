package io.taig.schelm.interpreter

import cats.Functor
import cats.effect.Ref
import cats.implicits._
import io.taig.schelm.algebra.ListenersManager
import io.taig.schelm.data.{ListenerTree, Listeners, Path}

final class RefListenersManager[F[_]: Functor](state: Ref[F, ListenerTree[F]]) extends ListenersManager[F] {
  override val snapshot: F[ListenerTree[F]] = state.get

  override def update(listeners: ListenerTree[F]): F[Unit] = state.set(listeners)

  override def get(path: Path): F[Option[Listeners[F]]] = snapshot.map(_.find(path).map(_.value))
}

object RefListenersManager {
  def apply[F[_]: Functor](state: Ref[F, ListenerTree[F]]): ListenersManager[F] = new RefListenersManager[F](state)

  def of[F[_]: Functor: Ref.Make](listeners: ListenerTree[F]): F[ListenersManager[F]] =
    Ref[F].of(listeners).map(RefListenersManager[F])

  def empty[F[_]: Functor: Ref.Make]: F[ListenersManager[F]] = of(ListenerTree.Empty)
}
