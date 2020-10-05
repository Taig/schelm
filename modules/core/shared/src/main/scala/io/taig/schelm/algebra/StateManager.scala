package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.{Html, Path}

abstract class StateManager[F[_], View] {
  def get[A](path: Path): F[Option[A]]

  def snapshot: F[Map[Path, _]]

  def submit[A](path: Path, initial: A, state: A, view: View): F[Unit]

  def subscription: Stream[F, StateManager.Update[View]]
}

object StateManager {
  final case class Update[View](path: Path, view: View)
}
