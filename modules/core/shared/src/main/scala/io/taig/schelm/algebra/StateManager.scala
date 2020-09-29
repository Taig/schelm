package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.Path

abstract class StateManager[F[_]] {
  def get(path: Path): F[Option[Any]]

  def snapshot: F[Map[Path, Any]]

  def submit[A](path: Path, value: A): F[Unit]

  def subscription: Stream[F, (Path, Any)]
}
