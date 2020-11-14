package io.taig.schelm.util

import io.taig.schelm.data.{Attributes, Children, Fix, Listeners}
import simulacrum.typeclass

@typeclass
trait FixAccessor[F[_[_]]] extends FixModification[F] {
  def listeners[G[_]](fg: F[G]): Option[Listeners[G]]

  def children[G[_]](fg: F[G]): Option[Children[F[G]]]
}

object FixAccessor {
  implicit def fix[F[_[_], _]](implicit accessor: NodeAccessor[F]): FixAccessor[λ[G[_] => Fix[F[G, *]]]] =
    new FixAccessor[λ[G[_] => Fix[F[G, *]]]] {
      override def listeners[G[_]](fg: Fix[F[G, *]]): Option[Listeners[G]] = accessor.listeners(fg.unfix)

      override def children[G[_]](fg: Fix[F[G, *]]): Option[Children[Fix[F[G, *]]]] =
        accessor.children(fg.unfix)

      override def modifyAttributes[G[_]](fg: Fix[F[G, *]])(f: Attributes => Attributes): Fix[F[G, *]] =
        Fix(accessor.modifyAttributes(fg.unfix)(f))

      override def modifyListeners[G[_]](fg: Fix[F[G, *]])(f: Listeners[G] => Listeners[G]): Fix[F[G, *]] = ???

      override def modifyChildren[G[_]](fg: Fix[F[G, *]])(
          f: Children[Fix[F[G, *]]] => Children[Fix[F[G, *]]]
      ): Fix[F[G, *]] = ???
    }

  @inline
  implicit def nested1[F[_[_], _], F1[_]](
      implicit accessor: NodeAccessor[λ[(G[_], A) => F1[F[G, A]]]]
  ): FixAccessor[λ[G[_] => Fix[λ[A => F1[F[G, A]]]]]] =
    fix[λ[(G[_], A) => F1[F[G, A]]]]

  @inline
  implicit def nested2[F[_[_], _], F1[_], F2[_]](
      implicit accessor: NodeAccessor[λ[(G[_], A) => F2[F1[F[G, A]]]]]
  ): FixAccessor[λ[G[_] => Fix[λ[A => F2[F1[F[G, A]]]]]]] =
    fix[λ[(G[_], A) => F2[F1[F[G, A]]]]]
}
