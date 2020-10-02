package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.data.{Html, Path}

abstract class StateManager[F[_]] {
  def get[A](path: Path): F[Option[A]]

  def submit[A](path: Path, initial: A, state: A, html: Html[F]): F[Unit]

  def subscription: Stream[F, StateManager.Update[F]]
}

object StateManager {
  final case class Update[F[_]](path: Path, html: Html[F])
}
