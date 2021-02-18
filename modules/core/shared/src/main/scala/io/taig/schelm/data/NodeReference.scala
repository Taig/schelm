package io.taig.schelm.data

import io.taig.schelm.algebra.Dom
import io.taig.schelm.instance.{NodeReferenceElementInstances, NodeReferenceFragmentInstances, NodeReferenceInstances}

sealed abstract class NodeReference[+F[_], +A] extends Product with Serializable

object NodeReference extends NodeReferenceInstances {
  final case class Element[F[_], A](node: Node.Element[F, A], dom: Dom.Element) extends NodeReference[F, A]

  object Element extends NodeReferenceElementInstances

  final case class Fragment[F[_], A](node: Node.Fragment[F, A]) extends NodeReference[F, A]

  object Fragment extends NodeReferenceFragmentInstances

  final case class Text[F[_]](node: Node.Text[F], dom: Dom.Text) extends NodeReference[F, Nothing]

  final case class Portal[F[_], A](node: Node.Portal[F, A]) extends NodeReference[F, A]
}
