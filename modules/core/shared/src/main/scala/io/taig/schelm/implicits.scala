package io.taig.schelm

import io.taig.schelm.util._

trait implicits
    extends FixAccessor.ToFixAccessorOps
    with FixModification.ToFixModificationOps
    with FixReferenceAccessor.ToFixReferenceAccessorOps
    with NamespaceFunctor.ToNamespaceFunctorOps
    with NamespaceTraverse.ToNamespaceTraverseOps
    with NodeAccessor.ToNodeAccessorOps
    with NodeModification.ToNodeModificationOps
    with NodeReferenceModification.ToNodeReferenceModificationOps
    with NodeReferenceAccessor.ToNodeReferenceAccessorOps

object implicits extends implicits
