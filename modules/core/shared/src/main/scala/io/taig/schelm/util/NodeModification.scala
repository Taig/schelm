package io.taig.schelm.util

import cats.Applicative
import cats.implicits._
import io.taig.schelm.data._
import simulacrum.typeclass

@typeclass
trait NodeModification[F[_[_], _, _]] {
  def node[G[_], Listeners, A](fa: F[G, Listeners, A]): Node[G, Listeners, A]

  def modifyNode[G[_]: Applicative, A, B, C, D](fa: F[G, A, B])(f: Node[G, A, B] => G[Node[G, C, D]]): G[F[G, C, D]]

  final def attributes(fa: F[Any, _, _]): Option[Attributes] = node(fa) match {
    case Node.Element(Tag(_, attributes, _), _, _) => Some(attributes)
    case _: Node.Fragment[_]                       => None
    case _: Node.Text[Any, _]                      => None
  }

  final def modifyAttributes[G[_]: Applicative, Listeners, A](
      fa: F[G, Listeners, A]
  )(f: Attributes => G[Attributes]): G[F[G, Listeners, A]] =
    modifyNode(fa) {
      case node: Node.Element[G, Listeners, A] =>
        f(node.tag.attributes).map(attributes => node.copy(tag = node.tag.copy(attributes = attributes)))
      case node: Node.Fragment[A]        => node.pure[G].widen
      case node: Node.Text[G, Listeners] => node.pure[G].widen
    }

  final def listeners[Listeners](fa: F[Any, Listeners, _]): Option[Listeners] = node(fa) match {
    case Node.Element(Tag(_, _, listeners), _, _) => Some(listeners)
    case _: Node.Fragment[_]                      => None
    case Node.Text(_, listeners, _)               => Some(listeners)
  }

  final def modifyListeners[G[_]: Applicative, A, B, C](fa: F[G, A, C])(f: A => G[B]): G[F[G, B, C]] =
    modifyNode(fa) {
      case node: Node.Element[G, A, C] =>
        f(node.tag.listeners).map(listeners => node.copy(tag = node.tag.copy(listeners = listeners)))
      case node: Node.Fragment[C] => node.pure[G].widen
      case node: Node.Text[G, A]  => f(node.listeners).map(listeners => node.copy(listeners = listeners))
    }

  final def children[A](fa: F[Any, _, A]): Option[Children[A]] = node(fa) match {
    case Node.Element(_, Node.Element.Variant.Normal(children), _) => Some(children)
    case Node.Element(_, Node.Element.Variant.Void, _)             => None
    case Node.Fragment(children)                                   => Some(children)
    case _: Node.Text[Any, _]                                      => None
  }

  final def modifyChildren[G[_]: Applicative, Listeners, A, B](
      fa: F[G, Listeners, A]
  )(f: Children[A] => G[Children[B]]): G[F[G, Listeners, B]] =
    modifyNode(fa) {
      case node: Node.Element[G, Listeners, A] =>
        node.variant match {
          case Node.Element.Variant.Normal(children) =>
            f(children).map(children => node.copy(variant = Node.Element.Variant.Normal(children)))
          case Node.Element.Variant.Void => node.asInstanceOf[Node[G, Listeners, B]].pure[G]
        }
      case node: Node.Fragment[A]        => f(node.children).map(children => node.copy(children = children))
      case node: Node.Text[G, Listeners] => node.pure[G].widen
    }
}

object NodeModification {
  implicit val node: NodeModification[Node] = new NodeModification[Node] {
    @inline
    override def node[G[_], Listeners, A](fa: Node[G, Listeners, A]): Node[G, Listeners, A] = fa

    @inline
    override def modifyNode[G[_]: Applicative, A, B, C, D](
        fa: Node[G, A, B]
    )(f: Node[G, A, B] => G[Node[G, C, D]]): G[Node[G, C, D]] = f(fa)
  }

  implicit val reference: NodeModification[NodeReference] =
    new NodeModification[NodeReference] {
      override def node[G[_], Listeners, A](fa: NodeReference[G, Listeners, A]): Node[G, Listeners, A] = fa match {
        case reference: NodeReference.Element[G, Listeners, A] => reference.node
        case reference: NodeReference.Fragment[A]              => reference.node
        case reference: NodeReference.Text[G, Listeners]       => reference.node
      }

      override def modifyNode[G[_]: Applicative, A, B, C, D](fa: NodeReference[G, A, B])(
          f: Node[G, A, B] => G[Node[G, C, D]]
      ): G[NodeReference[G, C, D]] = fa match {
        case reference: NodeReference.Element[G, A, B] => f(reference.node).map {
          case node: Node.Element[G, C, D] => reference.copy(node = node)
          case node: Node.Fragment[D] => NodeReference.Fragment(node)
          case node: Node.Text[G, C] => NodeReference.Text(node, ???)
        }
        case reference: NodeReference.Fragment[A]              => ???
        case reference: NodeReference.Text[G, Listeners]       => ???
      }
    }
}
