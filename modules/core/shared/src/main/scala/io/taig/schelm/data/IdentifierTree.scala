package io.taig.schelm.data

import scala.annotation.tailrec

import cats.implicits._
import cats.{Eq, Functor, Monoid}
import io.taig.schelm.data.Identification./

final case class IdentifierTree[+A](value: A, children: Map[Identifier, IdentifierTree[A]]) {
  @inline
  def get(key: Identifier): Option[IdentifierTree[A]] = children.get(key)

  @tailrec
  def find(identification: Identification): Option[IdentifierTree[A]] = identification match {
    case Identification.Root => Some(this)
    case head / tail =>
      get(head) match {
        case Some(child) => child.find(tail)
        case None        => None
      }
  }

  def delete(identification: Identification): IdentifierTree[A] = identification match {
    case head / Identification.Root => copy(children = children.removed(head))
    case head / tail                => get(head).fold(this)(child => updated(head, child.delete(tail)))
    case Identification.Root        => copy(children = IdentifierTree.EmptyChildren)
  }

  def updated[B >: A](identifier: Identifier, tree: IdentifierTree[B]): IdentifierTree[B] =
    copy(children = children.updated(identifier, tree))

  def merge[B >: A](tree: IdentifierTree[B])(implicit monoid: Monoid[B]): IdentifierTree[B] = {
    val keys = children.keySet ++ tree.children.keySet

    val builder = Map.newBuilder[Identifier, IdentifierTree[B]]

    keys.foreach { key =>
      (get(key), tree.get(key)) match {
        case (Some(x), Some(y)) => builder.addOne(key -> x.merge(y))
        case (Some(x), None)    => builder.addOne(key -> x)
        case (None, Some(y))    => builder.addOne(key -> y)
        case (None, None)       => ()
      }
    }

    IdentifierTree(monoid.combine(value, tree.value), builder.result())
  }
}

object IdentifierTree {
  val EmptyChildren: Map[Identifier, IdentifierTree[Nothing]] = Map.empty

  def empty[A: Monoid]: IdentifierTree[A] = IdentifierTree(Monoid[A].empty, EmptyChildren)

  def of[A](value: A, children: (Identifier, IdentifierTree[A])*): IdentifierTree[A] =
    IdentifierTree(value, children.toMap)

  def leaf[A](value: A): IdentifierTree[A] = IdentifierTree(value, EmptyChildren)

  implicit def monoid[A: Monoid]: Monoid[IdentifierTree[A]] = new Monoid[IdentifierTree[A]] {
    override def empty: IdentifierTree[A] = IdentifierTree.empty[A]

    override def combine(x: IdentifierTree[A], y: IdentifierTree[A]): IdentifierTree[A] = x merge y
  }

  implicit def functor: Functor[IdentifierTree] = new Functor[IdentifierTree] {
    override def map[A, B](fa: IdentifierTree[A])(f: A => B): IdentifierTree[B] =
      IdentifierTree(f(fa.value), fa.children.fmap(map(_)(f)))
  }

  implicit def eq[A: Eq]: Eq[IdentifierTree[A]] = new Eq[IdentifierTree[A]] {
    override def eqv(x: IdentifierTree[A], y: IdentifierTree[A]): Boolean =
      x.value === y.value && x.children === y.children
  }
}
