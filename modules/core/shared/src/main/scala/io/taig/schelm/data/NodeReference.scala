package io.taig.schelm.data

import io.taig.schelm.algebra.Dom

sealed abstract class NodeReference[+F[_], +A] extends Product with Serializable {
  final override def toString: String = this match {
    case NodeReference.Element(node, dom, _) => s"NodeReference.Element(node = $node, dom = $dom)"
    case NodeReference.Fragment(node)     => s"NodeReference.Fragment(node = $node)"
    case NodeReference.Text(node, dom, _)    => s"NodeReference.Text(node = $node, dom = $dom)"
  }
}

object NodeReference {
  final case class Element[F[_], A](node: Node.Element[F, A], dom: Dom.Element, listeners: ListenerReferences) extends NodeReference[F, A]

  final case class Fragment[A](node: Node.Fragment[A]) extends NodeReference[Nothing, A]

  final case class Text[F[_]](node: Node.Text[F], dom: Dom.Text, listeners: ListenerReferences) extends NodeReference[F, Nothing]
}
