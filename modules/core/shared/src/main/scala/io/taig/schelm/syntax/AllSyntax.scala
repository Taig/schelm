package io.taig.schelm.syntax

import io.taig.schelm.util._

trait AllSyntax
    extends FixAccessor.ToFixAccessorOps
    with FixModification.ToFixModificationOps
    with FixReferenceAccessor.ToFixReferenceAccessorOps
    with NamespaceFunctor.ToNamespaceFunctorOps
    with NamespaceTraverse.ToNamespaceTraverseOps
    with NodeAccessor.ToNodeAccessorOps
    with NodeModification.ToNodeModificationOps
    with NodeReferenceModification.ToNodeReferenceModificationOps
    with NodeReferenceAccessor.ToNodeReferenceAccessorOps

object AllSyntax extends AllSyntax
