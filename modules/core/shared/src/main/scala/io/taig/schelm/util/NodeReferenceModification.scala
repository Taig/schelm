package io.taig.schelm.util

import simulacrum.typeclass

@typeclass
trait NodeReferenceModification[F[_[_], _]] extends NodeModification[F] {}
