package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.algebra.Dom

sealed abstract class NodeReference[+F[_], +A] extends Product with Serializable {
  def node: Node[F, ListenerReferences[F], A]

  final override def toString: String = this match {
    case NodeReference.Element(node, dom) => s"NodeReference.Element(node = $node, dom = $dom)"
    case NodeReference.Fragment(node)     => s"NodeReference.Fragment(node = $node)"
    case NodeReference.Text(node, dom)    => s"NodeReference.Text(node = $node, dom = $dom)"
  }
}

object NodeReference {
  final case class Element[F[_], A](node: Node.Element[F, ListenerReferences[F], A], dom: Dom.Element)
      extends NodeReference[F, A]

  final case class Fragment[A](node: Node.Fragment[A]) extends NodeReference[Nothing, A]

  final case class Text[F[_]](node: Node.Text[F, ListenerReferences[F]], dom: Dom.Text)
      extends NodeReference[F, Nothing]

  implicit def traverse[F[_]]: Traverse[NodeReference[F, *]] = new Traverse[NodeReference[F, *]] {
    override def traverse[G[_]: Applicative, A, B](fa: NodeReference[F, A])(f: A => G[B]): G[NodeReference[F, B]] =
      fa match {
        case reference: NodeReference.Element[F, A] =>
          reference.node.traverse(f).map(node => reference.copy(node = node))
        case reference @ NodeReference.Fragment(node) =>
          node.traverse(f).map(node => reference.copy(node = node))
        case reference @ NodeReference.Text(_, _) => reference.pure[G].widen
      }

    override def foldLeft[A, B](fa: NodeReference[F, A], b: B)(f: (B, A) => B): B =
      fa match {
        case NodeReference.Element(node, _) => node.foldl(b)(f)
        case NodeReference.Fragment(node)   => node.foldl(b)(f)
        case _: NodeReference.Text[F]       => b
      }

    override def foldRight[A, B](fa: NodeReference[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
      fa match {
        case NodeReference.Element(node, _) => node.foldr(lb)(f)
        case NodeReference.Fragment(node)   => node.foldr(lb)(f)
        case _: NodeReference.Text[F]       => lb
      }
  }
}
