package io.taig.schelm.util

import scala.annotation.tailrec

import cats.Applicative
import cats.implicits._
import io.taig.schelm.data.Node.Element.Variant
import io.taig.schelm.data.Path./
import io.taig.schelm.data._
import simulacrum.typeclass

@typeclass
trait PathTraversal[A] {
  def children(value: A): Option[Children[A]]

  def find(value: A)(path: Path): Option[A]

  def modify[F[_]: Applicative](value: A)(path: Path)(f: A => F[A]): F[A]

  final def imap[B, C](fa: PathTraversal[B])(f: B => C)(g: C => B): PathTraversal[C] =
    new PathTraversal[C] {
      override def children(value: C): Option[Children[C]] = fa.children(g(value)).map(_.map(f))

      override def find(value: C)(path: Path): Option[C] = fa.find(g(value))(path).map(f)

      override def modify[F[_]: Applicative](value: C)(path: Path)(h: C => F[C]): F[C] =
        fa.modify(g(value))(path)(a => h(f(a)).map(g)).map(f)
    }
}

object PathTraversal {
  implicit def html[F[_]]: PathTraversal[Html[F]] = ofNode[F, Listeners[F], Html[F]](_.unfix, (_, node) => Html(node))

  def ofNode[F[_], Listeners, A](
      extract: A => Node[F, Listeners, A],
      lift: (A, Node[F, Listeners, A]) => A
  ): PathTraversal[A] =
    new PathTraversal[A] {
      override def children(reference: A): Option[Children[A]] = extract(reference) match {
        case Node.Element(_, Node.Element.Variant.Normal(children), _) => Some(children)
        case Node.Element(_, Node.Element.Variant.Void, _)             => None
        case Node.Fragment(children)                                   => Some(children)
        case _: Node.Text[_, _]                                        => None
      }

      @tailrec
      override def find(reference: A)(path: Path): Option[A] = path match {
        case Path.Root => Some(reference)
        case head / tail =>
          children(reference).flatMap(_.get(head)) match {
            case Some(child) => find(child)(tail)
            case None        => None
          }
      }

      override def modify[G[_]: Applicative](reference: A)(path: Path)(f: A => G[A]): G[A] = path match {
        case Path.Root => f(reference)
        case head / tail =>
          extract(reference) match {
            case node: Node.Element[F, Listeners, A] =>
              node.variant match {
                case Variant.Normal(children) =>
                  children.get(head) match {
                    case Some(child) =>
                      modify(child)(tail)(f).map { update =>
                        val variant = Node.Element.Variant.Normal(children.updated(head, update))
                        lift(reference, node.copy(variant = variant))
                      }
                    case None => reference.pure[G]
                  }
                case Variant.Void => reference.pure[G]
              }
            case node @ Node.Fragment(children) =>
              children.get(head) match {
                case Some(child) =>
                  modify(child)(tail)(f).map { update =>
                    lift(reference, node.copy(children = children.updated(head, update)))
                  }
                case None => reference.pure[G]
              }
            case _: Node.Text[F, Listeners] => reference.pure[G]
          }
      }
    }
}
