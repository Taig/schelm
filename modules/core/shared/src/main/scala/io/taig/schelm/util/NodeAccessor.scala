package io.taig.schelm.util

import cats.implicits._
import cats.{Comonad, Foldable}
import io.taig.schelm.data.{Attributes, Children, Listeners, Node}
import simulacrum.typeclass

@typeclass
trait NodeAccessor[F[_[_], _]] extends NodeModification[F] {
  def node[G[_], A](fga: F[G, A]): Node[G, A]

  def listeners[G[_], A](fga: F[G, A]): Option[Listeners[G]]

  def children[G[_], A](fga: F[G, A]): Option[Children[A]]
}

object NodeAccessor {
  implicit def nested1[F[_[_], _], F1[_]: Comonad: Foldable](
      implicit accessor: NodeAccessor[F]
  ): NodeAccessor[λ[(G[_], A) => F1[F[G, A]]]] =
    new NodeAccessor[λ[(G[_], A) => F1[F[G, A]]]] {
      override def node[G[_], A](fga: F1[F[G, A]]): Node[G, A] =
        accessor.node(fga.extract)

      override def listeners[G[_], A](fga: F1[F[G, A]]): Option[Listeners[G]] =
        fga.map(accessor.listeners).foldl(none[Listeners[G]]) {
          case (Some(accumulator), Some(listeners)) => Some(accumulator ++ listeners)
          case (accumulator, None)                  => accumulator
          case (None, listeners)                    => listeners
        }

      override def children[G[_], A](fga: F1[F[G, A]]): Option[Children[A]] =
        accessor.children(fga.extract)

      override def modifyAttributes[G[_], A](fga: F1[F[G, A]])(f: Attributes => Attributes): F1[F[G, A]] = ???

      override def modifyListeners[G[_], A](fga: F1[F[G, A]])(f: Listeners[G] => Listeners[G]): F1[F[G, A]] = ???

      override def modifyChildren[G[_], A](fga: F1[F[G, A]])(f: Children[A] => Children[A]): F1[F[G, A]] = ???
    }

  @inline
  implicit def nested2[F[_[_], _], F1[_], F2[_]: Comonad: Foldable](
      implicit accessor: NodeAccessor[λ[(G[_], A) => F1[F[G, A]]]]
  ): NodeAccessor[λ[(G[_], A) => F2[F1[F[G, A]]]]] =
    nested1[λ[(G[_], A) => F1[F[G, A]]], F2]

  @inline
  implicit def nested3[F[_[_], _], F1[_], F2[_], F3[_]: Comonad: Foldable](
      implicit accessor: NodeAccessor[λ[(G[_], A) => F2[F1[F[G, A]]]]]
  ): NodeAccessor[λ[(G[_], A) => F3[F2[F1[F[G, A]]]]]] =
    nested1[λ[(G[_], A) => F2[F1[F[G, A]]]], F3]
}
