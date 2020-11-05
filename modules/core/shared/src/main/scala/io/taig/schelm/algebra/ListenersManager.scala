package io.taig.schelm.algebra

import io.taig.schelm.data.{ListenerTree, Listeners, Path}

abstract class ListenersManager[F[_]] {
  def snapshot: F[ListenerTree[F]]

  def update(listeners: ListenerTree[F]): F[Unit]

  def get(path: Path): F[Option[Listeners[F]]]
}
