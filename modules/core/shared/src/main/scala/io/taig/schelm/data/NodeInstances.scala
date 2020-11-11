package io.taig.schelm.data

import cats._
import cats.implicits._
import io.taig.schelm.data.Node.Element.Variant

final class NodeInstances[F[_]] extends Traverse[Node[F, *]] {
  override def map[A, B](node: Node[F, A])(f: A => B): Node[F, B] = node match {
    case node: Node.Element[F, A] => node.map(f)
    case node: Node.Fragment[A]   => node.map(f)
    case node: Node.Text[F]       => node
  }

  override def traverse[G[_]: Applicative, A, B](node: Node[F, A])(f: A => G[B]): G[Node[F, B]] = node match {
    case node: Node.Element[F, A] => node.traverse(f).widen
    case node: Node.Fragment[A]   => node.traverse(f).widen
    case node: Node.Text[F]       => (node: Node[F, B]).pure[G]
  }

  override def foldLeft[A, B](node: Node[F, A], b: B)(f: (B, A) => B): B =
    node match {
      case node: Node.Element[F, A] => node.foldl(b)(f)
      case node: Node.Fragment[A]   => node.foldl(b)(f)
      case _: Node.Text[F]          => b
    }

  override def foldRight[A, B](node: Node[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    node match {
      case node: Node.Element[F, A] => node.foldr(lb)(f)
      case node: Node.Fragment[A]   => node.foldr(lb)(f)
      case _: Node.Text[F]          => lb
    }
}

final class NodeElementInstances[F[_]] extends Traverse[Node.Element[F, *]] {
  override def map[A, B](element: Node.Element[F, A])(f: A => B): Node.Element[F, B] = element.variant match {
    case Variant.Normal(children) => element.copy(variant = Node.Element.Variant.Normal(children.map(f)))
    case Variant.Void             => element.asInstanceOf[Node.Element[F, B]]
  }

  override def traverse[G[_]: Applicative, A, B](
      element: Node.Element[F, A]
  )(f: A => G[B]): G[Node.Element[F, B]] = element.variant match {
    case Variant.Normal(children) =>
      children.traverse(f).map(children => element.copy(variant = Node.Element.Variant.Normal(children)))
    case Variant.Void => element.asInstanceOf[Node.Element[F, B]].pure[G]
  }

  override def foldLeft[A, B](element: Node.Element[F, A], b: B)(f: (B, A) => B): B = element.variant match {
    case Variant.Normal(children) => children.foldl(b)(f)
    case Variant.Void             => b
  }

  override def foldRight[A, B](element: Node.Element[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    element.variant match {
      case Variant.Normal(children) => children.foldr(lb)(f)
      case Variant.Void             => lb
    }
}

object NodeFragmentInstances extends Traverse[Node.Fragment] {
  override def map[A, B](fragment: Node.Fragment[A])(f: A => B): Node.Fragment[B] =
    fragment.copy(children = fragment.children.map(f))

  override def traverse[G[_]: Applicative, A, B](fragment: Node.Fragment[A])(f: A => G[B]): G[Node.Fragment[B]] =
    fragment.children.traverse(f).map(children => fragment.copy(children = children))

  override def foldLeft[A, B](fragment: Node.Fragment[A], b: B)(f: (B, A) => B): B = fragment.children.foldl(b)(f)

  override def foldRight[A, B](fragment: Node.Fragment[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    fragment.children.foldr(lb)(f)
}
