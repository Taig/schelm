package io.taig.schelm.util

import simulacrum.typeclass

@typeclass
trait NodeTraverse[A] extends NodeFunctor[A] {}
