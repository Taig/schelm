package io.taig.schelm.util

import scala.annotation.tailrec

import cats.Applicative
import cats.implicits._
import io.taig.schelm.data.Path./
import io.taig.schelm.data._
import simulacrum.typeclass

@typeclass
trait PathTraversal[A] {
  def find(value: A)(path: Path): Option[A]

  def modify[F[_]: Applicative](value: A)(path: Path)(f: A => F[A]): F[A]

  def children(value: A): Option[Children[A]]

  def modifyChildren[F[_]: Applicative](value: A)(f: Children[A] => F[Children[A]]): F[A]
}

object PathTraversal {
  implicit def html[F[_]]: PathTraversal[Html[F]] = ??? // new PathTraversal[Html[F]] {
//    val Modification: NodeModification[Node[F, *, *]] = NodeModification[Node[F, *, *]]
//
//    @tailrec
//    override def find(html: Html[F])(path: Path): Option[Html[F]] = path match {
//      case Path.Root => Some(html)
//      case head / tail =>
//        children(html).flatMap(_.get(head)) match {
//          case Some(child) => find(child)(tail)
//          case None        => None
//        }
//    }
//
//    override def modify[G[_]: Applicative](html: Html[F])(path: Path)(f: Html[F] => G[Html[F]]): G[Html[F]] =
//      path match {
//        case Path.Root => f(html)
//        case head / tail =>
//          modifyChildren(html) { children =>
//            children.get(head) match {
//              case Some(child) => modify(child)(tail)(f).map(children.updated(head, _))
//              case None        => children.pure[G]
//            }
//          }
//      }
//
//    override def children(html: Html[F]): Option[Children[Html[F]]] = Modification.children(html.unfix)
//
//    override def modifyChildren[G[_]: Applicative](
//        html: Html[F]
//    )(f: Children[Html[F]] => G[Children[Html[F]]]): G[Html[F]] =
//      Modification.modifyChildren(html.unfix)(f).map(Html.apply)
//  }
}
