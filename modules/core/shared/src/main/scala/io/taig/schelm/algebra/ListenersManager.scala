package io.taig.schelm.algebra

import io.taig.schelm.data.{Listeners, Path}

abstract class ListenersManager[F[_]] {
  def snapshot: F[Map[Path, Listeners[F]]]
}
