package io.taig.schelm.util

import io.taig.schelm.algebra.Dom
import io.taig.schelm.data.{Attributes, Children, Fix, Html, Listeners, Node}
import simulacrum.typeclass
import cats.implicits._
import io.taig.schelm.data.Node.Element.Variant

@typeclass
trait FixReferenceAccessor[F[_[_]]] extends FixAccessor[F] {
  def dom[G[_]](fg: F[G]): Vector[Dom.Node]
}

object FixReferenceAccessor {
  implicit def fix[F[_[_], _]](
      implicit accessor: NodeReferenceAccessor[F]
  ): FixReferenceAccessor[λ[G[_] => Fix[F[G, *]]]] =
    new FixReferenceAccessor[λ[G[_] => Fix[F[G, *]]]] {
      override def html[G[_]](fg: Fix[F[G, *]]): Html[G] = Html(accessor.node(fg.unfix).map(html))

      override def listeners[G[_]](fg: Fix[F[G, *]]): Option[Listeners[G]] = ???

      override def modifyAttributes[G[_]](fg: Fix[F[G, *]])(f: Attributes => Attributes): Fix[F[G, *]] = ???

      override def modifyListeners[G[_]](fg: Fix[F[G, *]])(f: Listeners[G] => Listeners[G]): Fix[F[G, *]] = ???

      override def modifyChildren[G[_]](fg: Fix[F[G, *]])(
          f: Children[Fix[F[G, *]]] => Children[Fix[F[G, *]]]
      ): Fix[F[G, *]] = ???

      override def children[G[_]](fg: Fix[F[G, *]]): Option[Children[Fix[F[G, *]]]] = ???

      override def dom[G[_]](fix: Fix[F[G, *]]): Vector[Dom.Node] = accessor.dom(fix.unfix) match {
        case Some(dom) => Vector(dom)
        case None =>
          children(fix) match {
            case Some(value) => value.values.flatMap(dom[G])
            case None        => Vector.empty
          }
      }
    }
}
