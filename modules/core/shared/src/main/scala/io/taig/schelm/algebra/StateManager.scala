package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.{Identification, Identifier}

abstract class StateManager[F[_]] {
  def submit[A](update: StateManager.Update[A]): F[Unit]

  def subscription: Stream[F, StateManager.Update[_]]
}

object StateManager {
  final case class Update[A](identification: Identification, identifier: Identifier, default: A, apply: A => A)
}
