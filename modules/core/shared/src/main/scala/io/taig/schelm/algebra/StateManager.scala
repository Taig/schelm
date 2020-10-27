package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.{Path, StateTree}

abstract class StateManager[F[_], Structure] {
  def snapshot: F[StateTree[_]]

  def submit[A](path: Path, view: Structure, initial: A, state: A, index: Int): F[Unit]

  def subscription: Stream[F, StateManager.Update[Structure]]
}

object StateManager {
  final case class Update[Structure](path: Path, structure: Structure)
}
