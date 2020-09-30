package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.{Html, HtmlReference, Node, Path}

abstract class StateManager[F[_]] {
  def get(reference: HtmlReference[F]): F[Option[Any]]

  def snapshot: F[Map[HtmlReference[F], Any]]

  def submit[A](event: StateManager.Event[F, A]): F[Unit]

  def subscription: Stream[F, StateManager.Event[F, _]]
}

object StateManager {
  final case class Event[F[_], A](node: Node.Stateful[F, A, Html[F]], reference: HtmlReference[F], value: A)
}