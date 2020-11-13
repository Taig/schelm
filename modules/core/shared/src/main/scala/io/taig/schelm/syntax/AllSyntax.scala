package io.taig.schelm.syntax

import io.taig.schelm.util._

trait AllSyntax
    extends NodeFunctor.ToNodeFunctorOps
    with NodeTraverse.ToNodeTraverseOps
    with NodeReferenceFunctor.ToNodeReferenceFunctorOps
    with NodeReferenceTraverse.ToNodeReferenceTraverseOps
    with NamespaceFunctor.ToNamespaceFunctorOps
    with NamespaceTraverse.ToNamespaceTraverseOps

object AllSyntax extends AllSyntax
