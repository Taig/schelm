package io.taig.schelm.data

import cats.Traverse
import io.taig.schelm.algebra.Dom

sealed abstract class NodeReference[+F[_], +A] extends Product with Serializable {
  final override def toString: String = this match {
    case NodeReference.Element(node, dom) => s"NodeReference.Element(node = $node, dom = $dom)"
    case NodeReference.Fragment(node)     => s"NodeReference.Fragment(node = $node)"
    case NodeReference.Text(node, dom)    => s"NodeReference.Text(node = $node, dom = $dom)"
  }
}

object NodeReference {
  final case class Element[F[_], A](node: Node.Element[F, A], dom: Dom.Element) extends NodeReference[F, A]

  object Element {
    implicit def traverse[F[_]]: Traverse[Element[F, *]] = new NodeReferenceElementInstances[F]
  }

  final case class Fragment[A](node: Node.Fragment[A]) extends NodeReference[Nothing, A]

  object Fragment {
    implicit val traverse: Traverse[Fragment] = NodeReferenceFragmentInstances
  }

  final case class Text[F[_]](node: Node.Text[F], dom: Dom.Text) extends NodeReference[F, Nothing]

  implicit def traverse[F[_]]: Traverse[NodeReference[F, *]] = new NodeReferenceInstances[F]
}
