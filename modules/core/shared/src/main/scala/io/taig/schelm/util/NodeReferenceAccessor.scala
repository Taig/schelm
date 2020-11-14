package io.taig.schelm.util

import io.taig.schelm.algebra.Dom
import simulacrum.typeclass

@typeclass
trait NodeReferenceAccessor[F[_[_], _]] extends NodeReferenceModification[F] with NodeAccessor[F] {
  def dom[G[_], A](fga: F[G, A]): Option[Dom.Node]
}
