package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.Path

abstract class StateManager[F[_]] {
  def get[A](path: Path): F[Option[A]]

  def submit[A](path: Path, initial: A, update: A): F[Unit]

  def subscription: Stream[F, StateManager.Update[F, _]]
}

object StateManager {
  final case class Update[F[_], A](path: Path, initial: A, state: A)
}
