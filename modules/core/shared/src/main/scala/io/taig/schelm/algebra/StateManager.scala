package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.{Path, StateTree}

abstract class StateManager[F[_], Structure] {
  def snapshot: F[StateTree[_]]

  def submit[A](path: Path, initial: A, state: A, view: Structure): F[Unit]

  def subscription: Stream[F, StateManager.Update[Structure]]
}

object StateManager {
  final case class Update[Structure](path: Path, structure: Structure)
}
