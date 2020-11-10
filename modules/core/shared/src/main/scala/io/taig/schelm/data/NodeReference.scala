package io.taig.schelm.data

import cats.{Applicative, Eval}
import io.taig.schelm.algebra.Dom
import io.taig.schelm.util.NodeTraverse

sealed abstract class NodeReference[+F[_], +Listeners, +A] extends Product with Serializable {
  final override def toString: String = this match {
    case NodeReference.Element(node, dom) => s"NodeReference.Element(node = $node, dom = $dom)"
    case NodeReference.Fragment(node)     => s"NodeReference.Fragment(node = $node)"
    case NodeReference.Text(node, dom)    => s"NodeReference.Text(node = $node, dom = $dom)"
  }
}

object NodeReference {
  final case class Element[F[_], Listeners, A](node: Node.Element[F, Listeners, A], dom: Dom.Element)
      extends NodeReference[F, Listeners, A]

  final case class Fragment[A](node: Node.Fragment[A]) extends NodeReference[Nothing, Nothing, A]

  final case class Text[F[_], Listeners](node: Node.Text[F, Listeners], dom: Dom.Text)
      extends NodeReference[F, Listeners, Nothing]

  implicit def reference[F[_], Listeners]: NodeTraverse[NodeReference[F, Listeners, *]] =
    new NodeTraverse[NodeReference[F, Listeners, *]] {
      override def traverse[G[_]: Applicative, A, B](
          fa: NodeReference[F, Listeners, A]
      )(f: A => G[B]): G[NodeReference[F, Listeners, B]] =
        ???

      override def foldLeft[A, B](fa: NodeReference[F, Listeners, A], b: B)(f: (B, A) => B): B = ???

      override def foldRight[A, B](fa: NodeReference[F, Listeners, A], lb: Eval[B])(
          f: (A, Eval[B]) => Eval[B]
      ): Eval[B] = ???

      override def traverseWithKey[G[_]: Applicative, A, B](fa: NodeReference[F, Listeners, A])(
          f: (Key, A) => G[B]
      ): G[NodeReference[F, Listeners, B]] = ???
    }
}
