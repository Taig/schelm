package io.taig.schelm.data

import cats.Traverse
import io.taig.schelm.algebra.Dom
import io.taig.schelm.util.NodeReferenceAccessor

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

  implicit val accessor: NodeReferenceAccessor[NodeReference] = new NodeReferenceAccessor[NodeReference] {
    override def node[G[_], A](fga: NodeReference[G, A]): Node[G, A] = fga match {
      case reference: Element[G, A] => reference.node
      case reference: Fragment[A]   => reference.node
      case reference: Text[G]       => reference.node
    }

    override def listeners[G[_], A](fga: NodeReference[G, A]): Option[Listeners[G]] = ???

    override def children[G[_], A](fga: NodeReference[G, A]): Option[Children[A]] = ???

    override def modifyAttributes[G[_], A](fga: NodeReference[G, A])(f: Attributes => Attributes): NodeReference[G, A] =
      ???

    override def modifyListeners[G[_], A](fga: NodeReference[G, A])(
        f: Listeners[G] => Listeners[G]
    ): NodeReference[G, A] = ???

    override def modifyChildren[G[_], A](fga: NodeReference[G, A])(f: Children[A] => Children[A]): NodeReference[G, A] =
      ???

    override def dom[G[_], A](reference: NodeReference[G, A]): Option[Dom.Node] = reference match {
      case reference: NodeReference.Element[G, A] => Some(reference.dom)
      case _: NodeReference.Fragment[A]           => None
      case reference: NodeReference.Text[G]       => Some(reference.dom)
    }
  }
}
