package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.Path

abstract class StateManager[F[_], Structure] {
  def get[A](path: Path): F[Option[A]]

  def snapshot: F[Map[Path, _]]

  def submit[A](path: Path, initial: A, state: A, view: Structure): F[Unit]

  def subscription: Stream[F, StateManager.Update[Structure]]
}

object StateManager {
  final case class Update[Structure](path: Path, structure: Structure)
}
