package io.taig.schelm.data

import cats.implicits._
import cats.{Applicative, Eval, Traverse}
import io.taig.schelm.algebra.Dom

sealed abstract class NodeReference[F[_], +A] extends Product with Serializable

object NodeReference {
  final case class Element[F[_], A](node: Node.Element[F, A], dom: Dom.Element) extends NodeReference[F, A]
  final case class Fragment[F[_], A](node: Node.Fragment[F, A]) extends NodeReference[F, A]
  final case class Text[F[_]](node: Node.Text[F], dom: Dom.Text) extends NodeReference[F, Nothing]

  implicit def traverse[F[_]]: Traverse[NodeReference[F, *]] =
    new Traverse[NodeReference[F, *]] {
      override def traverse[G[_]: Applicative, A, B](
          fa: NodeReference[F, A]
      )(f: A => G[B]): G[NodeReference[F, B]] =
        fa match {
          case reference @ NodeReference.Element(component, _) =>
            component.traverse(f).map(component => reference.copy(node = component))
          case reference @ NodeReference.Fragment(component) =>
            component.traverse(f).map(component => reference.copy(node = component))
          case reference @ NodeReference.Text(_, _) => reference.pure[G].widen
        }

      override def foldLeft[A, B](fa: NodeReference[F, A], b: B)(f: (B, A) => B): B =
        fa match {
          case NodeReference.Element(component, _) => component.foldl(b)(f)
          case NodeReference.Fragment(component)   => component.foldl(b)(f)
          case _: NodeReference.Text[F]            => b
        }

      override def foldRight[A, B](fa: NodeReference[F, A], lb: Eval[B])(
          f: (A, Eval[B]) => Eval[B]
      ): Eval[B] =
        fa match {
          case NodeReference.Element(component, _) => component.foldr(lb)(f)
          case NodeReference.Fragment(component)   => component.foldr(lb)(f)
          case _: NodeReference.Text[F]            => lb
        }
    }
}
