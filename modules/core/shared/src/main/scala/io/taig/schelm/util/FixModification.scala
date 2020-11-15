package io.taig.schelm.util

import io.taig.schelm.data.{Attributes, Children, Fix, Listeners, Node}
import simulacrum.typeclass

@typeclass
trait FixModification[F[_[_]]] {
  def modifyAttributes[G[_]](fg: F[G])(f: Attributes => Attributes): F[G]

  def modifyListeners[G[_]](fg: F[G])(f: Listeners[G] => Listeners[G]): F[G]

  def modifyChildren[G[_]](fg: F[G])(f: Children[F[G]] => Children[F[G]]): F[G]
}

object FixModification {
  implicit def fixModification[F[_[_], _]](
      implicit modification: NodeModification[F]
  ): FixModification[Lambda[G[_] => Fix[F[G, *]]]] =
    new FixModification[Lambda[G[_] => Fix[F[G, *]]]] {
      override def modifyAttributes[G[_]](fix: Fix[F[G, *]])(f: Attributes => Attributes): Fix[F[G, *]] =
        Fix(modification.modifyAttributes(fix.unfix)(f))

      override def modifyListeners[G[_]](fix: Fix[F[G, *]])(f: Listeners[G] => Listeners[G]): Fix[F[G, *]] =
        Fix(modification.modifyListeners(fix.unfix)(f))

      override def modifyChildren[G[_]](
          fix: Fix[F[G, *]]
      )(f: Children[Fix[F[G, *]]] => Children[Fix[F[G, *]]]): Fix[F[G, *]] =
        Fix(modification.modifyChildren(fix.unfix)(f))
    }
}
