package io.taig.schelm.util

import io.taig.schelm.algebra.Dom
import simulacrum.typeclass

@typeclass
trait NodeReferenceTraverse[A] extends NodeReferenceFunctor[A] with NodeTraverse[A] {
  def dom(fa: A): Vector[Dom.Node]
}
