package io.taig.schelm.data

import cats._
import cats.implicits._

final class NodeReferenceInstances[F[_]] extends Traverse[NodeReference[F, *]] {
  override def map[A, B](reference: NodeReference[F, A])(f: A => B): NodeReference[F, B] =
    reference match {
      case reference: NodeReference.Element[F, A] => reference.map(f)
      case reference: NodeReference.Fragment[A]   => reference.map(f)
      case reference: NodeReference.Text[F]       => reference
    }

  override def traverse[G[_]: Applicative, A, B](reference: NodeReference[F, A])(f: A => G[B]): G[NodeReference[F, B]] =
    reference match {
      case reference: NodeReference.Element[F, A] => reference.traverse(f).widen
      case reference: NodeReference.Fragment[A]   => reference.traverse(f).widen
      case reference: NodeReference.Text[F]       => reference.pure[G].widen
    }

  override def foldLeft[A, B](reference: NodeReference[F, A], b: B)(f: (B, A) => B): B =
    reference match {
      case reference: NodeReference.Element[F, A] => reference.foldl(b)(f)
      case reference: NodeReference.Fragment[A]   => reference.foldl(b)(f)
      case _: NodeReference.Text[F]               => b
    }

  override def foldRight[A, B](reference: NodeReference[F, A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    reference match {
      case reference: NodeReference.Element[F, A] => reference.foldr(lb)(f)
      case reference: NodeReference.Fragment[A]   => reference.foldr(lb)(f)
      case _: NodeReference.Text[F]               => lb
    }
}

final class NodeReferenceElementInstances[F[_]] extends Traverse[NodeReference.Element[F, *]] {
  override def map[A, B](reference: NodeReference.Element[F, A])(f: A => B): NodeReference.Element[F, B] =
    reference.copy(node = reference.node.map(f))

  override def traverse[G[_]: Applicative, A, B](reference: NodeReference.Element[F, A])(
      f: A => G[B]
  ): G[NodeReference.Element[F, B]] =
    reference.node.traverse(f).map(node => reference.copy(node = node))

  override def foldLeft[A, B](reference: NodeReference.Element[F, A], b: B)(f: (B, A) => B): B =
    reference.node.foldl(b)(f)

  override def foldRight[A, B](reference: NodeReference.Element[F, A], lb: Eval[B])(
      f: (A, Eval[B]) => Eval[B]
  ): Eval[B] =
    reference.node.foldr(lb)(f)
}

object NodeReferenceFragmentInstances extends Traverse[NodeReference.Fragment] {
  override def map[A, B](reference: NodeReference.Fragment[A])(f: A => B): NodeReference.Fragment[B] =
    reference.copy(node = reference.node.map(f))

  override def traverse[G[_]: Applicative, A, B](
      reference: NodeReference.Fragment[A]
  )(f: A => G[B]): G[NodeReference.Fragment[B]] =
    reference.node.traverse(f).map(node => reference.copy(node = node))

  override def foldLeft[A, B](reference: NodeReference.Fragment[A], b: B)(f: (B, A) => B): B =
    reference.node.foldl(b)(f)

  override def foldRight[A, B](reference: NodeReference.Fragment[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
    reference.node.foldr(lb)(f)
}
