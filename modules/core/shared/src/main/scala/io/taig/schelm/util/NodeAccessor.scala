package io.taig.schelm.util

import io.taig.schelm.data.{Children, Listeners, Node}
import simulacrum.typeclass

@typeclass
trait NodeAccessor[F[_[_], _]] extends NodeModification[F] {
  def node[G[_], A](fga: F[G, A]): Node[G, A]

  def listeners[G[_], A](fga: F[G, A]): Option[Listeners[G]]

  def children[G[_], A](fga: F[G, A]): Option[Children[A]]
}
