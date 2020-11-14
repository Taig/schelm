package io.taig.schelm.data

final case class IdentificationLookup[A](root: A, children: Map[Identifier, IdentifierTree[A]])
