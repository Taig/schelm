package io.taig.schelm.util

import cats.{Applicative, Eval, Id, Traverse}
import io.taig.schelm.data._
import simulacrum.typeclass

@typeclass
trait NodeTraverse[F[_[_], _, _]] extends NodeFunctor[F] {
  override def algebra[G[_], Listeners]: Traverse[F[G, Listeners, *]]

  def traverseWithKey[G[_]: Applicative, H[_], Listeners, A, B](fa: F[H, Listeners, A])(
      f: (Key, A) => G[B]
  ): G[F[H, Listeners, B]]

  def traverseAttributes[G[_]: Applicative, H[_], Listeners, A](
      fa: F[H, Listeners, A]
  )(f: Attributes => G[Attributes]): G[F[H, Listeners, A]]

  def traverseListeners[G[_]: Applicative, H[_], A, B, C](fa: F[H, A, C])(f: A => G[B]): G[F[H, B, C]]

  def traverseChildren[G[_]: Applicative, H[_], Listeners, A, B](fa: F[H, Listeners, A])(
      f: Children[A] => G[Children[B]]
  ): G[F[H, Listeners, B]]

  override def mapWithKey[G[_], Listeners, A, B](fa: F[G, Listeners, A])(f: (Key, A) => B): F[G, Listeners, B] =
    traverseWithKey[Id, G, Listeners, A, B](fa)(f)

  override def mapAttributes[G[_], Listeners, A](fa: F[G, Listeners, A])(
      f: Attributes => Attributes
  ): F[G, Listeners, A] = traverseAttributes[Id, G, Listeners, A](fa)(f)

  override def mapListeners[G[_], A, B, C](fa: F[G, A, C])(f: A => B): F[G, B, C] =
    traverseListeners[Id, G, A, B, C](fa)(f)

  override def mapChildren[G[_], Listeners, A, B](fa: F[G, Listeners, A])(
      f: Children[A] => Children[B]
  ): F[G, Listeners, B] = traverseChildren[Id, G, Listeners, A, B](fa)(f)
}

object NodeTraverse {
//  implicit val node: NodeTraverse[Node] = new NodeTraverse[Node] {
//    override def traverseWithKey[G[_]: Applicative, Listeners, A, B](
//        fa: Node[G, Listeners, A]
//    )(f: (Key, A) => G[B]): G[Node[G, Listeners, B]] =
//      fa match {
//        case node: Node.Element[G, Listeners, A] =>
//          node.variant.traverseWithKey(f).map(variant => node.copy(variant = variant))
//        case node: Node.Fragment[A]        => node.children.traverseWithKey(f).map(children => node.copy(children = children))
//        case node: Node.Text[G, Listeners] => node.pure[G].widen
//      }
//
//    override def modifyAttributes[G[_]: Applicative, Listeners, A](
//        fa: Node[G, Listeners, A]
//    )(f: Attributes => G[Attributes]): G[Node[G, Listeners, A]] =
//      fa match {
//        case node: Node.Element[G, Listeners, A] =>
//          f(node.tag.attributes).map(attributes => node.copy(tag = node.tag.copy(attributes = attributes)))
//        case node: Node.Fragment[A]        => node.pure[G].widen
//        case node: Node.Text[G, Listeners] => node.pure[G].widen
//      }
//
//    override def modifyListeners[G[_]: Applicative, A, B, C](fa: Node[G, A, C])(f: A => G[B]): G[Node[G, B, C]] =
//      fa match {
//        case node: Node.Element[G, A, C] =>
//          f(node.tag.listeners).map(listeners => node.copy(tag = node.tag.copy(listeners = listeners)))
//        case node: Node.Fragment[C] => node.pure[G].widen
//        case node: Node.Text[G, A]  => f(node.listeners).map(listeners => node.copy(listeners = listeners))
//      }
//
//    override def modifyChildren[G[_]: Applicative, Listeners, A, B](
//        fa: Node[G, Listeners, A]
//    )(f: Children[A] => G[Children[B]]): G[Node[G, Listeners, B]] =
//      fa match {
//        case node: Node.Element[G, Listeners, A] =>
//          node.variant match {
//            case Node.Element.Variant.Normal(children) =>
//              f(children).map(children => node.copy(variant = Node.Element.Variant.Normal(children)))
//            case Node.Element.Variant.Void => node.asInstanceOf[Node[G, Listeners, B]].pure[G]
//          }
//        case node: Node.Fragment[A]        => f(node.children).map(children => node.copy(children = children))
//        case node: Node.Text[G, Listeners] => node.pure[G].widen
//      }
//  }
//
//  implicit val reference: NodeTraverse[NodeReference] = new NodeTraverse[NodeReference] {
//    override def modifyAttributes[G[_]: Applicative, Listeners, A](
//        fa: NodeReference[G, Listeners, A]
//    )(f: Attributes => G[Attributes]): G[NodeReference[G, Listeners, A]] =
//      fa match {
//        case reference: NodeReference.Element[G, Listeners, A] =>
//          f(reference.node.tag.attributes).map(attributes =>
//            reference.copy(node = reference.node.copy(tag = reference.node.tag.copy(attributes = attributes)))
//          )
//        case reference: NodeReference.Fragment[A]        => reference.pure[G].widen
//        case reference: NodeReference.Text[G, Listeners] => reference.pure[G].widen
//      }
//
//    override def modifyListeners[G[_]: Applicative, A, B, C](fa: NodeReference[G, A, C])(
//        f: A => G[B]
//    ): G[NodeReference[G, B, C]] = ???
//
//    override def modifyChildren[G[_]: Applicative, Listeners, A, B](fa: NodeReference[G, Listeners, A])(
//        f: Children[A] => G[Children[B]]
//    ): G[NodeReference[G, Listeners, B]] = ???
//  }
}
