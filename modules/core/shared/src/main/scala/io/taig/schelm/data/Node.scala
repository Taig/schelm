package io.taig.schelm.data

import cats.Traverse
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.util.NodeAccessor

sealed abstract class Node[+F[_], +A] extends Product with Serializable {
  final override def toString: String = this match {
    case Node.Element(tag, variant, lifecycle) =>
      s"Node.Element(tag = $tag, variant = $variant, lifecycle = $lifecycle)"
    case Node.Fragment(children) => s"Node.Fragment(children = $children)"
    case Node.Text(value, listeners, lifecycle) =>
      s"Node.Text(value = $value, listeners = $listeners, lifecycle = $lifecycle)"
  }
}

object Node {
  final case class Element[+F[_], +A](
      tag: Tag[F],
      variant: Element.Variant[A],
      lifecycle: Lifecycle.Element[F]
  ) extends Node[F, A]

  object Element {

    /** @see https://dev.w3.org/html5/html-author/#element-type-comparision */
    sealed abstract class Variant[+A] extends Product with Serializable

    object Variant {
      final case class Normal[+A](children: Children[A]) extends Variant[A]
      final case object Void extends Variant[Nothing]
    }

    implicit def instances[F[_]]: NodeElementInstances[F] = new NodeElementInstances[F]
  }

  final case class Fragment[+A](children: Children[A]) extends Node[Nothing, A]

  object Fragment {
    implicit val instances: NodeFragmentInstances.type = NodeFragmentInstances
  }

  final case class Text[+F[_]](
      value: String,
      listeners: Listeners[F],
      lifecycle: Lifecycle.Text[F]
  ) extends Node[F, Nothing]

  implicit def instances[F[_]]: NodeInstances[F] = new NodeInstances[F]

  implicit val accessor: NodeAccessor[Node] = new NodeAccessor[Node] {
    @inline
    override def node[G[_], A](fga: Node[G, A]): Node[G, A] = fga

    override def listeners[G[_], A](node: Node[G, A]): Option[Listeners[G]] = node match {
      case node: Node.Element[G, A] => Some(node.tag.listeners)
      case _: Node.Fragment[A]      => None
      case node: Node.Text[G]       => Some(node.listeners)
    }

    override def children[G[_], A](node: Node[G, A]): Option[Children[A]] = node match {
      case node: Node.Element[G, A] =>
        node.variant match {
          case Variant.Normal(children) => Some(children)
          case Variant.Void             => None
        }
      case node: Node.Fragment[A] => Some(node.children)
      case _: Node.Text[G]        => None
    }

    override def modifyAttributes[G[_], A](node: Node[G, A])(f: Attributes => Attributes): Node[G, A] = ???

    override def modifyListeners[G[_], A](node: Node[G, A])(f: Listeners[G] => Listeners[G]): Node[G, A] = ???

    override def modifyChildren[G[_], A](node: Node[G, A])(f: Children[A] => Children[A]): Node[G, A] = ???
  }
}
