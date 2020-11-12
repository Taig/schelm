package io.taig.schelm.algebra

import io.taig.schelm.data.{Identification, Identifier, StateTree}

abstract class StateManager[F[_]] {
  def snapshot: F[StateTree[_]]

  def submit[A](identification: Identification, identifier: Identifier, default: A, update: A => A): F[Unit]
}
