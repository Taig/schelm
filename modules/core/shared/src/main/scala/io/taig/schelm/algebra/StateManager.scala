package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.{HtmlReference, Identifier, NodeReference}

abstract class StateManager[F[_]] {
  def get(id: Identifier): F[Option[Any]]

  def snapshot: F[Map[Identifier, Any]]

  def submit[A](reference: NodeReference.Stateful[F, HtmlReference[F]], update: A): F[Unit]

  def subscription: Stream[F, StateManager.Event[F, _]]
}

object StateManager {
  final case class Event[F[_], A](reference: NodeReference.Stateful[F, HtmlReference[F]], previous: A, next: A)
}
