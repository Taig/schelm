package io.taig.schelm.util

import simulacrum.typeclass

@typeclass
trait NodeReferenceFunctor[A] extends NodeFunctor[A] {}
