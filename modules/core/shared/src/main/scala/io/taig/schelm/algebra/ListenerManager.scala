package io.taig.schelm.algebra

import fs2.Stream
import io.taig.schelm.algebra
import io.taig.schelm.data.{Listener, Path}

abstract class ListenerManager[F[_]] {
  def submit(update: ListenerManager.Update[F]): F[Unit]

  def subscription: Stream[F, algebra.ListenerManager.Update[F]]
}

object ListenerManager {
  final case class Update[F[_]](path: Path, listener: Listener[F])
}
