package io.taig.schelm.util

import io.taig.schelm.data.{Attributes, Children, Listeners, Node}
import simulacrum.typeclass

@typeclass
trait NodeModification[F[_[_], _]] {
  def modifyAttributes[G[_], A](fga: F[G, A])(f: Attributes => Attributes): F[G, A]

  def modifyListeners[G[_], A](fga: F[G, A])(f: Listeners[G] => Listeners[G]): F[G, A]

  def modifyChildren[G[_], A](fga: F[G, A])(f: Children[A] => Children[A]): F[G, A]
}
